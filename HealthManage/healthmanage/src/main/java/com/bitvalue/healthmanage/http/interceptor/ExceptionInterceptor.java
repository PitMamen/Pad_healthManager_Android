package com.bitvalue.healthmanage.http.interceptor;

import android.content.Intent;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.manager.ActivityManager;
import com.bitvalue.healthmanage.ui.activity.LoginHealthActivity;
import com.bitvalue.healthmanage.util.SharedPreManager;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @Description 异常处理 拦截器.
 */

public class ExceptionInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        if (!bodyEncoded(response.headers())) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
                return response;
            }

            if (contentLength != 0) {
                String responseStr = buffer.clone().readString(charset);
                HttpData<String> responseEntity = null;
                try {
//                    new Gson().fromJson(re)
                    responseEntity = new Gson().fromJson(responseStr, new TypeToken<HttpData<String>>() {
                    }.getType());
                } catch (Exception e) {
                    //兼容如下的后台错误：
                    /*
                    <html>
                    <head><title>502 Bad Gateway</title></head>
                    <body bgcolor="white">
                    <center><h1>502 Bad Gateway</h1></center>
                    <hr><center>nginx/1.12.2</center>
                    </body>
                    </html>
                    */

                    //FIXME 先注释掉，Toast无法弹出，报错：java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
                    //ToastUtil.showToastLong("网络异常，请稍后再试！\n" + e.getMessage());
                }

                if (responseEntity != null) {
                    int result = responseEntity.getCode();
                    // result为10001时，接口需要登录
                    if (result == 10001) {
                        ToastUtil.toastShortMessage("账号已过期，请重新登录");
                        Intent intent = new Intent(AppApplication.instance(), LoginHealthActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        AppApplication.instance().startActivity(intent);
                        SharedPreManager.putString(Constants.KEY_TOKEN, "");
                        // 进行内存优化，销毁除登录页之外的所有界面
                        ActivityManager.getInstance().finishAllActivities(LoginHealthActivity.class);
                    }
                }
            }

        }

        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

}
