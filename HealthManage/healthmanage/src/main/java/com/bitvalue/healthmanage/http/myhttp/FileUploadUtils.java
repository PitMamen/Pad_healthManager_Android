package com.bitvalue.healthmanage.http.myhttp;

import androidx.lifecycle.LifecycleOwner;

import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.UpdateImageApi;
import com.bitvalue.healthmanage.http.request.UploadFileApi;
import com.bitvalue.healthmanage.http.response.AudioUploadResultBean;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;

import okhttp3.Call;

public enum FileUploadUtils implements OnHttpListener<Object> {
    INSTANCE;

    public void uploadAudio(LifecycleOwner lifecycleOwner, UploadFileApi uploadFileApi, OnAudioUploadCallback onAudioUploadCallback) {
        EasyHttp.post(lifecycleOwner).api(uploadFileApi).request(new HttpCallback<HttpData<AudioUploadResultBean>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<AudioUploadResultBean> result) {
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

        void onSuccess(HttpData<AudioUploadResultBean> result);

        void onFail();

    }

    public void uploadPic(LifecycleOwner lifecycleOwner, UpdateImageApi updateImageApi, OnAudioUploadCallback onAudioUploadCallback) {
        EasyHttp.post(lifecycleOwner).api(updateImageApi).request(new HttpCallback<HttpData<AudioUploadResultBean>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<AudioUploadResultBean> result) {
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

    public interface OnPicUploadCallback {
        void onSuccess(HttpData<AudioUploadResultBean> result);

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
