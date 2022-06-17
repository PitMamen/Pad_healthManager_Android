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
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    private static final boolean useHttps = true; // 生产环境只有https.仅针对commonservice
    private static SSLSocketFactory sslSocketFactory = null;
    private static X509TrustManager trustManager = null;

    private NetEngine() {
        httpsInit();
        mRetrofit = new Retrofit.Builder().
                baseUrl(Constants.HOST_URL).
                client(getCommonServiceClient()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                build();

    }


    /***
     * 这里是为了配合使用EasyHttp网络请求框架
     * * @return
     */
    public OkHttpClient getOkHttpClient() {
        // 网络请求框架初始化
        httpsInit();
        return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory,trustManager)
                .connectTimeout(30 * 1000L, TimeUnit.MILLISECONDS)
                .readTimeout(30 * 1000L, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new NetInterceptor())
                .addInterceptor(new LoggerInterceptor("OkHttp", true))
                .addInterceptor(reInterceptor)
                .addInterceptor(new ExceptionInterceptor())
                .retryOnConnectionFailure(true)
                //其他配置
                .build();
    }

//    private String HOST_URL_LIST = "http://36.158.225.181:24702/ehr/v1/list";   //获取病历列表  正式
//    private String HOST_URL_LIST_RECORD = "http://36.158.225.181:24702/ehr/v1/getRecord";   //获取病历详情 正式
//
    private String HOST_URL_LIST = "http://develop.mclouds.org.cn:24702/ehr/v1/list";   //获取病历列表  测试
    private String HOST_URL_LIST_RECORD = "http://develop.mclouds.org.cn:24702/ehr/v1/getRecord";   //获取病历详情 测试

    private Interceptor reInterceptor = chain -> {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        HttpUrl oldHttpUrl = request.url();   //从request中获取原有的HttpUrl实例oldHttpUrl
        HttpUrl newBaseUrl = HttpUrl.parse(Constants.HOST_URL);
        if (oldHttpUrl.url().toString().contains("/ehr/v1/list")) {
            newBaseUrl = HttpUrl.parse(HOST_URL_LIST);
        }else if (oldHttpUrl.url().toString().contains("/ehr/v1/getRecord")){
            newBaseUrl = HttpUrl.parse(HOST_URL_LIST_RECORD);
        }
        //重建新的HttpUrl，配置成我们需要的
        HttpUrl newFullUrl = oldHttpUrl
                .newBuilder()
                .scheme(newBaseUrl.scheme())
                .host(newBaseUrl.host())    //新的url的域名
                .port(newBaseUrl.port())
                .build();
        Log.e(TAG, "intercept111: "+newFullUrl );
        return chain.proceed(builder.url(newFullUrl).build());
    };


    private OkHttpClient getCommonServiceClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addNetworkInterceptor(new NetInterceptor());
        builder.addInterceptor(new LoggerInterceptor("OkHttp", true));
        builder.retryOnConnectionFailure(false);
        builder.addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", SharedPreManager.getString(Constants.KEY_TOKEN))
                    .build();

            return chain.proceed(request);
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
                        callback.onSuccess(result.getData(), 1000);

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
