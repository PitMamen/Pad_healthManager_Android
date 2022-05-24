package com.bitvalue.health;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.multidex.MultiDex;

import com.bitvalue.health.net.NetEngine;
import com.bitvalue.health.net.RequestHandler;
import com.bitvalue.health.net.RequestServer;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.ActivityManager;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.SmartBallPulseFooter;
import com.bitvalue.health.util.chatUtil.HelloChatController;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.TUIKitListenerManager;
import com.bitvalue.sdk.collab.config.ConfigHelper;
import com.hjq.bar.TitleBar;
import com.hjq.bar.initializer.LightBarInitializer;
import com.hjq.http.EasyConfig;
import com.hjq.http.config.IRequestInterceptor;
import com.hjq.http.model.HttpHeaders;
import com.hjq.http.model.HttpParams;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastInterceptor;
import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastBlackStyle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.liteav.debug.GenerateTestUserSig;

import org.xutils.x;

/**
 * author : pxk
 * time   : 2021/10/27
 * desc   : 应用入口
 */
public final class Application extends android.app.Application {
    private static Application instance;
    private HomeActivity homeActivity;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MultiDex.install(this);//配置app方法数无限制
        initSdk(this);
    }

    public static Application instance() {
        return instance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 清理所有图片内存缓存
//        GlideApp.get(this).onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // 根据手机内存剩余情况清理图片内存缓存
//        GlideApp.get(this).onTrimMemory(level);
    }

    /**
     * 初始化一些第三方框架
     */
    @SuppressLint("MissingPermission")
    public void initSdk(android.app.Application application) {
        // 设置调试模式
        XXPermissions.setDebugMode(AppConfig.isDebug());

        // 初始化吐司
        ToastUtils.init(application, new ToastBlackStyle(application) {

            @Override
            public int getCornerRadius() {
                return (int) application.getResources().getDimension(R.dimen.button_round_size);
            }
        });

        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor());

        // 设置标题栏初始化器
        TitleBar.setDefaultInitializer(new LightBarInitializer() {

            @Override
            public Drawable getBackgroundDrawable(Context context) {
                return new ColorDrawable(ContextCompat.getColor(application, R.color.common_primary_color));
            }

            @Override
            public Drawable getBackIcon(Context context) {
                return ContextCompat.getDrawable(context, R.drawable.arrows_left_ic);
            }

            @Override
            protected TextView createTextView(Context context) {
                return new AppCompatTextView(context);
            }
        });

        // 本地异常捕捉
//        CrashHandler.register(application);

        // 友盟统计、登录、分享 SDK
        //UmengClient.init(application);

        // Bugly 异常捕捉
        CrashReport.initCrashReport(application, AppConfig.getBuglyId(), AppConfig.isDebug());

        // 设置全局的 Header 构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) ->
                new MaterialHeader(context).setColorSchemeColors(ContextCompat.getColor(context, R.color.common_accent_color)));
        // 设置全局的 Footer 构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new SmartBallPulseFooter(context));
        // 设置全局初始化器
        SmartRefreshLayout.setDefaultRefreshInitializer((context, layout) -> {
            // 刷新头部是否跟随内容偏移
            layout.setEnableHeaderTranslationContent(true)
                    // 刷新尾部是否跟随内容偏移
                    .setEnableFooterTranslationContent(true)
                    // 加载更多是否跟随内容偏移
                    .setEnableFooterFollowWhenNoMoreData(true)
                    // 内容不满一页时是否可以上拉加载更多
                    .setEnableLoadMoreWhenContentNotFull(false)
                    // 仿苹果越界效果开关
                    .setEnableOverScrollDrag(false);
        });

        //图片选择(添加问题)预览缩略图用到的ImageLoader,在这要初始化一下
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//				.showImageOnFail(R.drawable.test)
//				.showImageOnFail(R.drawable.test)
                .cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions).build();

        ImageLoader.getInstance().init(config);

        // Activity 栈管理初始化
        ActivityManager.getInstance().init(application);


        // 初始化日志打印
//        if (AppConfig.isLogEnable()) {
//            Timber.plant(new DebugLoggerTree());
//        }

        // 注册网络状态变化监听
        ConnectivityManager connectivityManager = ContextCompat.getSystemService(application, ConnectivityManager.class);
        if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onLost(@NonNull Network network) {
                    Activity topActivity = ActivityManager.getInstance().getTopActivity();
                    if (topActivity instanceof LifecycleOwner) {
                        LifecycleOwner lifecycleOwner = ((LifecycleOwner) topActivity);
                        if (lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                            ToastUtils.show(R.string.common_network_error);
                        }
                    }
                }
            });
        }

        ToastUtils.init(instance);

        iniEasyHttp(); // 暂时顶用一下，后面改成Retrofit
        initTencentIM(instance());
        x.Ext.init(this);  //初始化 xUtils 框架


        initWidthAndHeight();
    }




    private void iniEasyHttp(){
        EasyConfig.with(NetEngine.getInstance().getOkHttpClient())
                // 是否打印日志
                .setLogEnabled(AppConfig.isLogEnable())
                .addHeader("Authorization", SharedPreManager.getString(Constants.KEY_TOKEN))
                // 设置服务器配置
                .setServer(new RequestServer())
                .setInterceptor((url, tag, params, headers) -> {

                })
                // 设置请求处理策略
                .setHandler(new RequestHandler(instance()))
                // 设置请求重试次数
                .setRetryCount(1)
                .into();
    }


    private void initTencentIM(Application application){
        TUIKit.init(application, GenerateTestUserSig.SDKAPPID, new ConfigHelper().getConfigs(application));
        registerCustomListeners();
    }

    private  void registerCustomListeners() {
        TUIKitListenerManager.getInstance().addChatListener(new HelloChatController());
        TUIKitListenerManager.getInstance().addConversationListener(new HelloChatController.HelloConversationController());
    }




    private void initWidthAndHeight() {
        /**
         * 获取屏幕宽高
         */
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.getDefaultDisplay().getRealMetrics(dm);
            Constants.screenWidth = dm.widthPixels; // 宽度（PX）
            Constants.screenHeight = dm.heightPixels; // 高度（PX）
        } else {
            windowManager.getDefaultDisplay().getMetrics(dm);
            Constants.screenWidth = dm.widthPixels;
            Constants.screenHeight = dm.heightPixels;
        }
    }





    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    public HomeActivity getHomeActivity() {
        return homeActivity;
    }

}