package com.bitvalue.health.net;

import android.content.Intent;
import android.util.Log;

import com.bitvalue.health.util.ActivityManager;
import com.bitvalue.health.Application;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.sdk.collab.utils.NetWorkUtils;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjq.toast.ToastUtils;

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
    public static final String TAG = ExceptionInterceptor.class.getSimpleName();
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetWorkUtils.isNetworkAvailable(Application.instance())) {
            ToastUtil.toastShortMessage("网络不可用，请打开网络或稍后重试");
        }
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (response.code() != 200) {
            ToastUtils.show("服务器异常，请稍后再试");
        }

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
                ApiResult<String> responseEntity = null;
                try {
//                    new Gson().fromJson(re)
                    responseEntity = new Gson().fromJson(responseStr, new TypeToken<ApiResult<String>>() {
                    }.getType());
                } catch (Exception e) {
                    Log.e(TAG, "httpintercept: "+e.getMessage() );
                }

                if (responseEntity != null) {
                    int result = responseEntity.getCode();
                    // result为10001时，接口需要登录
                    if (result == 10001) {
                        Intent intent = new Intent(Application.instance(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.NEED_TOAST, true);
                        Application.instance().startActivity(intent);
                        SharedPreManager.putString(Constants.KEY_TOKEN, "");
                        // 进行内存优化，销毁除登录页之外的所有界面
                        ActivityManager.getInstance().finishAllActivities(HomeActivity.class);
                        return response;
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
