package com.bitvalue.health.net;

import android.util.Log;

import com.bitvalue.health.api.CommonService;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.sdk.collab.helper.CustomVideoCallMessage;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @ClassName: NetEngine
 * @Author: pxk
 * @CreateDate: 2021/10/25
 * @Version: 1.0
 */
public class NetEngine {
    private static final String TAG = "NetEngine";

    private static Retrofit mRetrofit;
    protected static CommonService newService;

    private static volatile NetEngine mInstance;

    // 使用https
    private static final boolean useHttps = false; // 生产环境只有https.仅针对commonservice
    private static SSLSocketFactory sslSocketFactory = null;
    private static X509TrustManager trustManager = null;

    private NetEngine() {
        httpsInit();
        mRetrofit = new Retrofit.Builder().
                baseUrl(Constants.BASE_URL).
                client(getCommonServiceClient()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                build();

    }


    public OkHttpClient getOkHttpClient() {
        // 网络请求框架初始化
        return new OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("OkHttp", true))
                .addInterceptor(new ExceptionInterceptor())
                //其他配置
                .build();
    }


    private OkHttpClient getCommonServiceClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggerInterceptor("OkHttp", true));
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", SharedPreManager.getString(Constants.KEY_TOKEN)).
                                build();
                return chain.proceed(request);
            }
        });
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);
        if (sslSocketFactory != null && trustManager != null) {
            Log.e(TAG, "ZMKM 使用https");
            builder.hostnameVerifier((hostname, session) -> true)
                    .sslSocketFactory(sslSocketFactory, trustManager);
        }
        return builder.build();
    }

    private void httpsInit() {
        if (!useHttps) {
            return;
        }
        // https请求，不作证书校验
        trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                // 不验证
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        try {
            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            Log.e(TAG, "httpsInit: " + e);
        }
    }


    public static NetEngine getInstance() {
        if (mInstance == null) {
            synchronized (NetEngine.class) {
                if (mInstance == null) {
                    mInstance = new NetEngine();
                }
            }
        }
        return mInstance;
    }

    public CommonService getService() {
        if (newService == null) {
            newService = mRetrofit.create(CommonService.class);
        }
        return newService;
    }


    public void getDataDetail(CustomVideoCallMessage messageData, Callback callback) throws Throwable {
        if (!EmptyUtil.isEmpty(messageData)) {
            newService.getVideoPatientStatus(messageData.id).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        if (!EmptyUtil.isEmpty(result.getData())) {
                            callback.onSuccess(result.getData(), 1000);
                        }

                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }

            }, error -> {
                callback.onFailedLog(error.getMessage(), 1001);
            });

        } else {
            throw new Throwable("CustomVideoCallMessage = null !!!");
        }
    }


    /**
     * 清除NetEngine
     */
    public static void Clear() {
        mInstance = null;
    }


}
