package com.bitvalue.health.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author created by bitvalue
 * @data : 03/31
 */
public class NetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request  = chain.request().newBuilder().addHeader("Connection", "close").build(); //"Authorization", SharedPreManager.getString(Constants.KEY_TOKEN)
        return chain.proceed(request);
    }
}
