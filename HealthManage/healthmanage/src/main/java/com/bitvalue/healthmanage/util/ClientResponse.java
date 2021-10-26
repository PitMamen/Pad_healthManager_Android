package com.bitvalue.healthmanage.util;

import androidx.lifecycle.LifecycleOwner;

import com.bitvalue.healthmanage.http.model.ApiResult;
import com.hjq.http.EasyHttp;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;

import java.util.List;

import okhttp3.Call;

public class ClientResponse<T>  {


    public  void httpRequest(LifecycleOwner conetxt, IRequestApi url, OnHttpListener<?> listener){
        EasyHttp.get(conetxt).api(url).request(new HttpCallback<ApiResult<T>>(listener){
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<T> result) {
                super.onSucceed(result);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

}
