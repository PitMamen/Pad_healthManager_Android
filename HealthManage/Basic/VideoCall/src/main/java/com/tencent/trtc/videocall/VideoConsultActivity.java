package com.tencent.trtc.videocall;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.tencent.trtc.util.DataUtil;
import com.example.basic.TRTCBaseActivity;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.liteav.device.TXDeviceManager;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
import com.tencent.trtc.debug.Constant;
import com.tencent.trtc.debug.GenerateTestUserSig;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * TRTC视频通话的主页面
 * <p>
 * 包含如下简单功能：
 * - 进入视频通话房间{@link VideoCallingActivity#enterRoom()}
 * - 退出视频通话房间{@link VideoCallingActivity#exitRoom()}
 * - 切换前置/后置摄像头{@link VideoCallingActivity#switchCamera()}
 * - 打开/关闭摄像头{@link VideoCallingActivity#muteVideo()}
 * - 打开/关闭麦克风{@link VideoCallingActivity#muteAudio()}
 * - 显示房间内其他用户的视频画面（当前示例最多可显示6个其他用户的视频画面）{@link TRTCCloudImplListener#refreshRemoteVideoViews()}
 * <p>
 * - 详见接入文档{https://cloud.tencent.com/document/product/647/42045}
 */

/**
 * Video Call
 * <p>
 * Features:
 * - Enter a video call room: {@link VideoConsultActivity#enterRoom()}
 * - Exit a video call room: {@link VideoConsultActivity#exitRoom()}
 * - Switch between the front and rear cameras: {@link VideoConsultActivity#switchCamera()}
 * - Turn on/off the camera: {@link VideoConsultActivity#muteVideo()}
 * - Turn on/off the mic: {@link VideoConsultActivity#muteAudio()}
 * - Display the video of other users (max. 6) in the room: {@link TRTCCloudImplListener#refreshRemoteVideoViews()}
 * <p>
 * - For more information, please see the integration document {https://cloud.tencent.com/document/product/647/42045}.
 */
public class VideoConsultActivity extends TRTCBaseActivity implements View.OnClickListener {

    private static final String TAG = "VideoCallingActivity";
    private static final int OVERLAY_PERMISSION_REQ_CODE = 1234;

    private TextView mTextTitle;
    private TXCloudVideoView mTXCVVLocalPreviewView;
    private ImageView mImageBack;

    private TRTCCloud mTRTCCloud;
    private TXDeviceManager mTXDeviceManager;
    private boolean mIsFrontCamera = true;
    private List<String> mRemoteUidList;
    private List<TXCloudVideoView> mRemoteViewList;
    private int mUserCount = 0;
    private String mRoomId;
    private String mUserId;
    private boolean mAudioRouteFlag = true;
    private FloatingView mFloatingView;
    private LinearLayout layout_switch_camera, layout_close_camera, layout_switch_audio, layout_close_audio, layout_handle, layout_end;
    private TextView tv_switch_camera, tv_close_camera, tv_switch_audio, tv_close_audio;
    private boolean isCameraOpen = true;
    private boolean isAudioOpen = true;
    private Subscription hideSubscribe;
    private TXCloudVideoView trtc_view_1;
    private String mRemoteUid;
    private boolean isSwitched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_consult);
//        getSupportActionBar().hide();
        handleIntent();

        if (checkPermission()) {
            initView();
            enterRoom();
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.getStringExtra(Constant.USER_ID) != null) {
                mUserId = intent.getStringExtra(Constant.USER_ID);
            }
            if (intent.getStringExtra(Constant.ROOM_ID) != null) {
                mRoomId = intent.getStringExtra(Constant.ROOM_ID);
            }
        }
    }

    private void initView() {
        mTextTitle = findViewById(R.id.tv_room_number);
        mImageBack = findViewById(R.id.iv_back);
        mTXCVVLocalPreviewView = findViewById(R.id.txcvv_main);

        layout_switch_camera = findViewById(R.id.layout_switch_camera);
        layout_close_camera = findViewById(R.id.layout_close_camera);
        layout_switch_audio = findViewById(R.id.layout_switch_audio);
        layout_close_audio = findViewById(R.id.layout_close_audio);
        layout_end = findViewById(R.id.layout_end);
        layout_handle = findViewById(R.id.layout_handle);
        tv_switch_camera = findViewById(R.id.tv_switch_camera);
        tv_close_camera = findViewById(R.id.tv_close_camera);
        tv_switch_audio = findViewById(R.id.tv_switch_audio);
        tv_close_audio = findViewById(R.id.tv_close_audio);

        if (!TextUtils.isEmpty(mRoomId)) {
//            mTextTitle.setText(getString(R.string.videocall_roomid) + mRoomId);
            mTextTitle.setText("当前房间号：" + mRoomId);
        }
        mImageBack.setOnClickListener(this);

        layout_switch_camera.setOnClickListener(this);
        layout_close_camera.setOnClickListener(this);
        layout_switch_audio.setOnClickListener(this);
        mTXCVVLocalPreviewView.setOnClickListener(this);
        layout_close_audio.setOnClickListener(this);
        layout_end.setOnClickListener(this);

        mRemoteUidList = new ArrayList<>();
        mRemoteViewList = new ArrayList<>();
        trtc_view_1 = (TXCloudVideoView) findViewById(R.id.trtc_view_1);
        mRemoteViewList.add(trtc_view_1);
        trtc_view_1.setOnClickListener(this);
        mRemoteViewList.add((TXCloudVideoView) findViewById(R.id.trtc_view_2));
        mRemoteViewList.add((TXCloudVideoView) findViewById(R.id.trtc_view_3));
        mRemoteViewList.add((TXCloudVideoView) findViewById(R.id.trtc_view_4));
        mRemoteViewList.add((TXCloudVideoView) findViewById(R.id.trtc_view_5));
        mRemoteViewList.add((TXCloudVideoView) findViewById(R.id.trtc_view_6));

        mFloatingView = new FloatingView(getApplicationContext(), R.layout.videocall_view_floating_default);
        mFloatingView.setPopupWindow(R.layout.videocall_popup_layout);
        mFloatingView.setOnPopupItemClickListener(this);
    }

    private void enterRoom() {
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setListener(new TRTCCloudImplListener(VideoConsultActivity.this));
        mTXDeviceManager = mTRTCCloud.getDeviceManager();

        TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        trtcParams.userId = mUserId;
        trtcParams.roomId = Integer.parseInt(mRoomId);
        trtcParams.userSig = GenerateTestUserSig.genTestUserSig(trtcParams.userId);

        mTRTCCloud.startLocalPreview(mIsFrontCamera, mTXCVVLocalPreviewView);
        mTRTCCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_SPEECH);
        mTRTCCloud.enterRoom(trtcParams, TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);
        startCountAndHide();
    }

    @Override
    protected void onStop() {
        super.onStop();
        requestDrawOverLays();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null && mFloatingView.isShown()) {
            mFloatingView.dismiss();
        }
        exitRoom();
    }

    private void exitRoom() {
        if (mTRTCCloud != null) {
            mTRTCCloud.stopLocalAudio();
            mTRTCCloud.stopLocalPreview();
            mTRTCCloud.exitRoom();
            mTRTCCloud.setListener(null);
        }
        mTRTCCloud = null;
        TRTCCloud.destroySharedInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFloatingView != null && mFloatingView.isShown()) {
            mFloatingView.dismiss();
        }
    }

    @Override
    protected void onPermissionGranted() {
        initView();
        enterRoom();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            DataUtil.showNormalDialog(this, "温馨提示", "确定退出并挂断吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                @Override
                public void onPositive() {
                    finish();
                }

                @Override
                public void onNegative() {

                }
            });
        } else if (id == R.id.iv_return) {
            floatViewClick();
        } else if (id == R.id.layout_close_camera) {
            muteVideo();
        } else if (id == R.id.layout_switch_camera) {
            switchCamera();
        } else if (id == R.id.layout_close_audio) {
            muteAudio();
        } else if (id == R.id.layout_switch_audio) {
            audioRoute();
        } else if (id == R.id.txcvv_main) {
            //点击的时候展示控制栏
            layout_handle.setVisibility(View.VISIBLE);

            startCountAndHide();
        } else if (id == R.id.layout_end) {
            DataUtil.showNormalDialog(this, "温馨提示", "确定挂断吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                @Override
                public void onPositive() {
                    finish();
                }

                @Override
                public void onNegative() {

                }
            });

        } else if (id == R.id.trtc_view_1) {//TODO 处理点击切换大小窗逻辑
            if (mRemoteUid == null || mRemoteUid.isEmpty()) {
                return;
            }
            if (!isSwitched) {
                mTRTCCloud.startLocalPreview(mIsFrontCamera, trtc_view_1);

//            mTRTCCloud.startRemoteView(mRemoteUid, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SMALL, mTXCVVLocalPreviewView);
                mTRTCCloud.startRemoteView(mRemoteUid, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG, mTXCVVLocalPreviewView);
            } else {
                mTRTCCloud.startLocalPreview(mIsFrontCamera, mTXCVVLocalPreviewView);
                mTRTCCloud.startRemoteView(mRemoteUid, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SMALL, trtc_view_1);
            }
            isSwitched = true;
        }
    }

    private void startCountAndHide() {
        if (null != hideSubscribe) {
            hideSubscribe.unsubscribe();
        }
        hideSubscribe = Observable.timer(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                layout_handle.setVisibility(View.GONE);
            }
        });
    }

    private void floatViewClick() {
        Intent intent = new Intent(this, VideoConsultActivity.class);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void muteVideo() {
        isCameraOpen = !isCameraOpen;
        if (!isCameraOpen) {
            mTRTCCloud.stopLocalPreview();
            tv_close_camera.setText("打开摄像头");
        } else {
            mTRTCCloud.startLocalPreview(mIsFrontCamera, mTXCVVLocalPreviewView);
            tv_close_camera.setText("关闭摄像头");
        }
    }

    private void muteAudio() {
        isAudioOpen = !isAudioOpen;
        if (!isAudioOpen) {
            mTRTCCloud.muteLocalAudio(true);
            tv_close_audio.setText("打开麦克风");
        } else {
            mTRTCCloud.muteLocalAudio(false);
            tv_close_audio.setText("关闭麦克风");
        }
    }

    private void switchCamera() {
        mIsFrontCamera = !mIsFrontCamera;
        mTXDeviceManager.switchCamera(mIsFrontCamera);
        if (mIsFrontCamera) {
            tv_switch_camera.setText("切换后摄像头");
        } else {
            tv_switch_camera.setText("切换前摄像头");
        }
    }

    private void audioRoute() {
        if (mAudioRouteFlag) {
            mAudioRouteFlag = false;
            mTXDeviceManager.setAudioRoute(TXDeviceManager.TXAudioRoute.TXAudioRouteEarpiece);
            tv_switch_audio.setText("切换扬声器");
        } else {
            mAudioRouteFlag = true;
            mTXDeviceManager.setAudioRoute(TXDeviceManager.TXAudioRoute.TXAudioRouteSpeakerphone);
            tv_switch_audio.setText("切换听筒");
        }
    }

    private class TRTCCloudImplListener extends TRTCCloudListener {

        private WeakReference<VideoConsultActivity> mContext;

        public TRTCCloudImplListener(VideoConsultActivity activity) {
            super();
            mContext = new WeakReference<>(activity);
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            Log.d(TAG, "onUserVideoAvailable userId " + userId + ", mUserCount " + mUserCount + ",available " + available);
            int index = mRemoteUidList.indexOf(userId);
            if (available) {
                if (index != -1) {
                    return;
                }
                mRemoteUidList.add(userId);
                refreshRemoteVideoViews();
            } else {
                if (index == -1) {
                    return;
                }
                mTRTCCloud.stopRemoteView(userId);
                mRemoteUidList.remove(index);
                refreshRemoteVideoViews();
            }

        }

        private void refreshRemoteVideoViews() {
            for (int i = 0; i < mRemoteViewList.size(); i++) {
                if (i < mRemoteUidList.size()) {
                    String remoteUid = mRemoteUidList.get(i);
                    mRemoteViewList.get(i).setVisibility(View.VISIBLE);
                    mRemoteUid = remoteUid;
                    mTRTCCloud.startRemoteView(remoteUid, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SMALL, mRemoteViewList.get(i));
                } else {
                    mRemoteViewList.get(i).setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onError");
            VideoConsultActivity activity = mContext.get();
            if (activity != null) {
                Toast.makeText(activity, "onError: " + errMsg + "[" + errCode + "]", Toast.LENGTH_SHORT).show();
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    activity.exitRoom();
                }
            }
        }
    }

    public void requestDrawOverLays() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N && !Settings.canDrawOverlays(VideoConsultActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + VideoConsultActivity.this.getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            showFloatingView();
        }
    }

    private void showFloatingView() {
        if (mFloatingView != null && !mFloatingView.isShown()) {
            if ((null != mTRTCCloud)) {
                mFloatingView.show();
                mFloatingView.setOnPopupItemClickListener(this);
            }
        }
    }
}
