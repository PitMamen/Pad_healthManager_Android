package com.bitvalue.sdk.base.util.net;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

public class RequestUtil {

    public static void postWithJson(String tag, String url, String json, StringCallback stringCallback) {
        OkHttpUtils.postString().tag(tag).url(url).content(json).build().execute(stringCallback);
    }

    public static void certificateUser(String url, Map<String, String> map, Callback callback) {
        OkHttpUtils.get().url(url).params(map).build().execute(callback);
    }
}
