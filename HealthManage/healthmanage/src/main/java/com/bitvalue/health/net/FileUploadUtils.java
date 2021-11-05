package com.bitvalue.health.net;

import android.os.Environment;

import androidx.lifecycle.LifecycleOwner;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.requestbean.UpdateImageApi;
import com.bitvalue.health.api.requestbean.UploadFileApi;
import com.bitvalue.health.api.responsebean.AudioUploadResultBean;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public enum FileUploadUtils implements OnHttpListener<Object> {
    INSTANCE;


    /**
     * 上传语音
     * @param lifecycleOwner
     * @param uploadFileApi
     * @param onAudioUploadCallback
     */
    public void uploadAudio(LifecycleOwner lifecycleOwner, UploadFileApi uploadFileApi, OnAudioUploadCallback onAudioUploadCallback) {
        EasyHttp.post(lifecycleOwner).api(uploadFileApi).request(new HttpCallback<ApiResult<AudioUploadResultBean>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<AudioUploadResultBean> result) {
                super.onSucceed(result);
                onAudioUploadCallback.onSuccess(result);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                onAudioUploadCallback.onFail();
            }
        });

    }

    /**
     * 上传图片
     * @param lifecycleOwner
     * @param updateImageApi
     * @param onAudioUploadCallback
     */
    public void uploadPic(LifecycleOwner lifecycleOwner, UpdateImageApi updateImageApi, OnAudioUploadCallback onAudioUploadCallback) {
        compressPicture(updateImageApi.file.getAbsolutePath(), new CompressCallBack() {
            @Override
            public void onSuccess(String compressPath) throws IOException {
                updateImageApi.file = new File(compressPath);
                EasyHttp.post(lifecycleOwner).api(updateImageApi).request(new HttpCallback<ApiResult<AudioUploadResultBean>>(FileUploadUtils.this) {
                    @Override
                    public void onStart(Call call) {
                        super.onStart(call);
                    }

                    @Override
                    public void onSucceed(ApiResult<AudioUploadResultBean> result) {
                        super.onSucceed(result);
                        onAudioUploadCallback.onSuccess(result);
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        onAudioUploadCallback.onFail();
                    }
                });
            }

            @Override
            public void onFail(String oldPath) {

            }
        });
    }


    /**
     *图片压缩
     * @param filePath
     * @param compressCallBack
     */
    public static void compressPicture(String filePath, CompressCallBack compressCallBack) {
        try {
            String compressPath = Environment.getExternalStorageDirectory() + "/lunbanCompress/";
            File mkdir = new File(compressPath);
            if (!mkdir.exists()) {
                mkdir.mkdirs();
            }
            /**
             * 上传前先压缩图片
             */
            Luban.with(Application.instance())
                    .load(filePath) //added 图片在手机本地的储存绝对路径
                    .ignoreBy(100)// 忽略不压缩图片的大小
                    .setTargetDir(compressPath)// 设置压缩后文件存储位置
                    .setCompressListener(new OnCompressListener() {//设置回调
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
//                            uploadImg(file.getAbsolutePath(), true);
                            try {
                                compressCallBack.onSuccess(file.getAbsolutePath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            compressCallBack.onFail(filePath);
//                            uploadImg(imgPath, true);
                        }
                    }).launch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 语音上传回调
     */
    public interface OnAudioUploadCallback {

        void onSuccess(ApiResult<AudioUploadResultBean> result);

        void onFail();

    }

    /**
     * 图片压缩上产、语音上产回调
     */
    public interface CompressCallBack {
        void onSuccess(String compressPath) throws IOException;

        void onFail(String oldPath);
    }


    @Override
    public void onStart(Call call) {

    }

    @Override
    public void onSucceed(Object result) {

    }

    @Override
    public void onFail(Exception e) {

    }

    @Override
    public void onEnd(Call call) {

    }
}
