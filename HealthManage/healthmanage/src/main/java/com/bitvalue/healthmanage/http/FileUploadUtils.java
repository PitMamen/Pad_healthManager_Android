package com.bitvalue.healthmanage.http;

import android.os.Environment;

import androidx.lifecycle.LifecycleOwner;

import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.model.ApiResult;
import com.bitvalue.healthmanage.http.api.UpdateImageApi;
import com.bitvalue.healthmanage.http.api.UploadFileApi;
import com.bitvalue.healthmanage.http.bean.AudioUploadResultBean;
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

    public interface OnAudioUploadCallback {

        void onSuccess(ApiResult<AudioUploadResultBean> result);

        void onFail();

    }

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
            Luban.with(AppApplication.instance())
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

    public interface CompressCallBack {
        void onSuccess(String compressPath) throws IOException;

        void onFail(String oldPath);
    }

    public interface OnPicUploadCallback {
        void onSuccess(ApiResult<AudioUploadResultBean> result);

        void onFail();
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
