package com.bitvalue.uvccamera;

import static com.bitvalue.uvccamera.render.opengl.OpenGlUtils.NO_TEXTURE;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.usb.UsbDevice;
import android.media.AudioManager;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bitvalue.uvccamera.model.FrameBuffer;
import com.bitvalue.uvccamera.model.TextureFrame;
import com.bitvalue.uvccamera.render.EglCore;
import com.bitvalue.uvccamera.render.opengl.GPUImageFilter;
import com.bitvalue.uvccamera.render.opengl.GPUImageFilterGroup;
import com.bitvalue.uvccamera.render.opengl.OesInputFilter;
import com.bitvalue.uvccamera.render.opengl.OpenGlUtils;
import com.bitvalue.uvccamera.render.opengl.Rotation;
import com.serenegiant.usb.DeviceFilter;
import com.serenegiant.usb.Size;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UVCCameraCapturer implements SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = UVCCameraCapturer.class.getSimpleName();

    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;

    private SurfaceTexture mSurfaceTexture;
    private EglCore mEglCore;
    private FrameBuffer mFrameBuffer;
    private OesInputFilter mOesInputFilter;
    private GPUImageFilterGroup mGpuImageFilterGroup;

    private final FloatBuffer mGLCubeBuffer;
    private final FloatBuffer mGLTextureBuffer;
    private final float[] mTextureTransform = new float[16]; // OES纹理转换为2D纹理
    private int mSurfaceTextureId = NO_TEXTURE;

    private Camera mCamera2;
    private UVCCamera mCamera;
    private CameraEventListener mCameraEventListener;

    private volatile VideoFrameReadListener mVideoFrameReadListener;
    private HandlerThread mCaptureHandlerThread;
    private CaptureHandler mCaptureHandler;

    private USBMonitor mUSBMonitor;
    private USBMonitor.OnDeviceConnectListener mOnDeviceConnectListener = new USBMonitor.OnDeviceConnectListener() {
        @Override
        public void onAttach(UsbDevice usbDevice) {
            Log.d(TAG, "OnDeviceConnectListener onAttach: device = " + usbDevice.getDeviceName());
            if (mUSBMonitor.hasPermission(usbDevice)) {
                mUSBMonitor.openDevice(usbDevice);
            } else {
                mUSBMonitor.requestPermission(usbDevice);
            }

            Log.d(TAG, "OnDeviceConnectionListener onAttach: exiting.");
        }

        @Override
        public void onDettach(UsbDevice usbDevice) {
            Log.d(TAG, "OnDeviceConnectListener onDettach: device = " + usbDevice.getDeviceName());
        }

        @Override
        public void onConnect(UsbDevice usbDevice, USBMonitor.UsbControlBlock usbControlBlock, boolean createNew) {
            Log.d(TAG, "OnDeviceConnectListener onConnect, createNew = " + createNew + ", fd = " + usbControlBlock.getFileDescriptor());
            final UVCCamera camera = new UVCCamera();

            try {
                Log.d(TAG, "OnDeviceConnectListener onConnect: camera.open start");
                camera.open(usbControlBlock);

                Log.d(TAG, "Trying to find desired preview sizes.");
                List<Size> sizes = findPreviewSizes(camera);

                boolean hasSetPreviewSize = false;
                for (Size size : sizes) {
                    try {
                        Log.d(TAG, "Trying to set up preview size " + size);
                        camera.setPreviewSize(size.width, size.height, 1, 60,
                                size.type == 4 ? UVCCamera.FRAME_FORMAT_YUYV : UVCCamera.FRAME_FORMAT_MJPEG, 1.0f);
                        hasSetPreviewSize = true;
                        break;
                    } catch (final IllegalArgumentException e) {
                        Log.w(TAG, "Failed to set preview size " + size);
                    }
                }
                if (!hasSetPreviewSize) {
                    throw new IllegalArgumentException("No preview size is available.");
                }

                // notify capture thread to be prepared for the new camera.
                mCaptureHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        stopInternal();
                        mCamera = camera;

                        // notify ui thread to prompt user that uvccamera is ready to use.
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (mCameraEventListener != null) {
                                    mCameraEventListener.onCameraReady();
                                }
                            }
                        });
                    }
                });

            } catch (Exception exp) {
                Log.e(TAG, "Failed to open UVC Camera.", exp);
                camera.destroy();
            }
        }

        private List<Size> findPreviewSizes(UVCCamera camera) {
            Log.d(TAG, "Camera supported sizes: " + camera.getSupportedSize());
            List<Size> sizes = UVCCamera.getSupportedSize(-1, camera.getSupportedSize());
            if (sizes == null || sizes.isEmpty()) {
                throw new IllegalArgumentException("No supported size found for this device.");
            }
            List<Size> desired = new ArrayList<>();
            for (Size size : sizes) {
                Log.d(TAG, "trying " + size.width + "x" + size.height + " of type " + size.type);
                if (size.width == WIDTH && size.height == HEIGHT) {
                    desired.add(size);
                }
            }
            Collections.sort(desired, new Comparator<Size>() {
                @Override
                public int compare(Size o1, Size o2) {
                    return o1.type - o2.type; // YUYV first
                }
            });
            return desired;
        }


        @Override
        public void onDisconnect(UsbDevice usbDevice, USBMonitor.UsbControlBlock usbControlBlock) {
            Log.d(TAG, "OnDeviceConnectListener onDisconnect: device = " + usbDevice.getDeviceName());
            // notify capture thread to stop capturing
            mCaptureHandler.post(new Runnable() {
                @Override
                public void run() {
                    stopInternal();

                    // notify ui thread to prompt user that uvccamera is offline.
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (mCameraEventListener != null) {
                                mCameraEventListener.onCameraDetached();
                            }
                        }
                    });
                }
            });
        }

        @Override
        public void onCancel(UsbDevice usbDevice) {
            Log.d(TAG, "OnDeviceConnectListener onCancel: device = " + usbDevice.getDeviceName());
        }

    };

    public interface CameraEventListener {
        void onCameraReady();
        void onCameraDetached();
    }

    public interface VideoFrameReadListener {
        void onFrameAvailable(EGLContext eglContext, int textureId, int width, int height);
    }

    public UVCCameraCapturer(Context applicationContext) {
        Pair<float[], float[]> cubeAndTextureBuffer = OpenGlUtils.calcCubeAndTextureBuffer(ImageView.ScaleType.CENTER, Rotation.NORMAL,
                false, WIDTH, HEIGHT, WIDTH, HEIGHT);
        mGLCubeBuffer = ByteBuffer.allocateDirect(OpenGlUtils.CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mGLCubeBuffer.put(cubeAndTextureBuffer.first);
        mGLTextureBuffer = ByteBuffer.allocateDirect(OpenGlUtils.TEXTURE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mGLTextureBuffer.put(cubeAndTextureBuffer.second);
        mCaptureHandlerThread = new HandlerThread("CaptureHandlerThread");
        mCaptureHandlerThread.start();
        mCaptureHandler = new CaptureHandler(mCaptureHandlerThread.getLooper(), this);
        mUSBMonitor = new USBMonitor(applicationContext, mOnDeviceConnectListener);
        mUSBMonitor.setDeviceFilter(DeviceFilter.getDeviceFilters(applicationContext, R.xml.device_filter));

        Log.d(TAG, "UVCCameraCapturer created.");
    }

    public void listen(CameraEventListener cameraEventListener) {
        mCameraEventListener = cameraEventListener;
        mUSBMonitor.register();
        List<UsbDevice> devices = mUSBMonitor.getDeviceList();
        if (devices.size() > 0) {
            mUSBMonitor.requestPermission(devices.get(0));
        }
    }

    public synchronized void startCapture(final VideoFrameReadListener videoFrameReadListener) {
        // Fixme: it is possible that a quick start after last stop
        //        will deliver previous frames to the new listener.
        Log.d(TAG, "startCapture");
        mVideoFrameReadListener = videoFrameReadListener;
        mCaptureHandler.sendEmptyMessage(CaptureHandler.WHAT_START);
    }

    public synchronized void stopCapture() {
        Log.d(TAG, "stopCapture");
        mVideoFrameReadListener = null;
        mCaptureHandler.sendEmptyMessage(CaptureHandler.WHAT_STOP);
    }

    public void destroy() {
        Log.d(TAG, "destroy");
        mCaptureHandler.sendEmptyMessage(CaptureHandler.WHAT_DESTROY);

        if (mCaptureHandlerThread != null) {
            mCaptureHandlerThread.quitSafely();
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // Log.d(TAG, "onFrameAvailable");
        mCaptureHandler.sendEmptyMessage(CaptureHandler.WHAT_UPDATE);
    }

    // running in render thread
    private void startInternal() {
        Log.d(TAG, "startInternal");

        if (mCamera == null) {
            Log.w(TAG, "Camera not ready, failed to start capture.");
            return;
        }

        mEglCore = new EglCore(WIDTH, HEIGHT);
        mEglCore.makeCurrent();

        mFrameBuffer = new FrameBuffer(WIDTH, HEIGHT);
        mFrameBuffer.initialize();

        mGpuImageFilterGroup = new GPUImageFilterGroup();
        mOesInputFilter = new OesInputFilter();
        mGpuImageFilterGroup.addFilter(mOesInputFilter);
        mGpuImageFilterGroup.addFilter(new GPUImageFilter(true));
        mGpuImageFilterGroup.init();
        mGpuImageFilterGroup.onOutputSizeChanged(WIDTH, HEIGHT);

        mSurfaceTextureId = OpenGlUtils.generateTextureOES();
        mSurfaceTexture = new SurfaceTexture(mSurfaceTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(UVCCameraCapturer.this);

        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "Failed to start camera preview", e);
        }

//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    mCamera2 = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
//                    mCamera2.setPreviewTexture(mSurfaceTexture);
//                    //mCamera2.setDisplayOrientation(90);
//                    Camera.Parameters parameters = mCamera2.getParameters();
//                    parameters.setPreviewSize(WIDTH, HEIGHT);
//                    parameters.setPreviewFrameRate(15);
//                    mCamera2.setParameters(parameters);
//                    mCamera2.startPreview();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private void stopInternal() {
        Log.d(TAG, "stopInternal");

        if (mCamera != null) {
            mCamera.stopPreview();
        }

        // debug-only
        if (mCamera2 != null) {
            mCamera2.stopPreview();
            mCamera2 = null;
        }

        if (mGpuImageFilterGroup != null) {
            mGpuImageFilterGroup.destroy();
            mGpuImageFilterGroup = null;
        }

        if (mFrameBuffer != null) {
            mFrameBuffer.uninitialize();
            mFrameBuffer = null;
        }

        if (mSurfaceTextureId != NO_TEXTURE) {
            OpenGlUtils.deleteTexture(mSurfaceTextureId);
            mSurfaceTextureId = NO_TEXTURE;
        }

        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }

        if (mEglCore != null) {
            mEglCore.unmakeCurrent();
            mEglCore.destroy();
            mEglCore = null;
        }
    }

    private void destroyInternal() {
        Log.d(TAG, "destroyInternal");
        if (mUSBMonitor != null) {
            mUSBMonitor.unregister();
            mUSBMonitor.destroy();
            mUSBMonitor = null;
        }

        if (mCamera != null) {
            mCamera.destroy();
            mCamera = null;
        }
    }

    // running in capture thread
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void updateTexture() {
        // Log.d(TAG, "Trying to update texture.");

        try {
            if (mSurfaceTexture != null) {
                mSurfaceTexture.updateTexImage();
                mSurfaceTexture.getTransformMatrix(mTextureTransform);
                mOesInputFilter.setTexutreTransform(mTextureTransform);
                mGpuImageFilterGroup.draw(mSurfaceTextureId, mFrameBuffer.getFrameBufferId(), mGLCubeBuffer, mGLTextureBuffer);

                GLES20.glFinish();

                synchronized (this) {
                    if (mVideoFrameReadListener != null) {
                        TextureFrame textureFrame = new TextureFrame();
                        textureFrame.eglContext = (EGLContext) mEglCore.getEglContext();
                        textureFrame.textureId = mFrameBuffer.getTextureId();
                        textureFrame.width = WIDTH;
                        textureFrame.height = HEIGHT;
                        // Log.d(TAG, "Trying to call listener.onFrameAvailable().");

                        mVideoFrameReadListener.onFrameAvailable(textureFrame.eglContext, textureFrame.textureId, textureFrame.width, textureFrame.height);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to render texture frame, reason: " + e.getMessage(), e);
        }
    }

    private static class CaptureHandler extends Handler {
        public static final int WHAT_START = 0;
        public static final int WHAT_UPDATE = 1;
        public static final int WHAT_STOP = 2;
        public static final int WHAT_DESTROY = 3;

        private final WeakReference<UVCCameraCapturer> capturerReference;

        public CaptureHandler(@NonNull Looper looper, UVCCameraCapturer UVCCameraCapturer) {
            super(looper);
            capturerReference = new WeakReference<>(UVCCameraCapturer);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void handleMessage(@NonNull Message msg) {
            UVCCameraCapturer capturer = capturerReference.get();
            if (capturer != null) {
                switch (msg.what) {
                    case WHAT_START:
                        capturer.startInternal();
                        break;
                    case WHAT_UPDATE:
                        capturer.updateTexture();
                        break;
                    case WHAT_STOP:
                        capturer.stopInternal();
                        break;
                    case WHAT_DESTROY:
                        capturer.destroyInternal();
                        break;
                }
            }
        }
    }
}
