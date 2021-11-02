package com.bitvalue.health.base;

import static com.hjq.http.EasyUtils.postDelayed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.ui.activity.LoginHealthActivity;
import com.bitvalue.health.util.ActivityManager;
import com.bitvalue.health.util.BaseDialog;
import com.bitvalue.health.util.BundleAction;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.KeyboardAction;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.WaitDialog;
import com.bitvalue.health.util.aop.TitleBarAction;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IMEventListener;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/***
 *
 * @param <P>
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IView, KeyboardAction, BundleAction, TitleBarAction {

    protected String TAG = this.getClass().getSimpleName();
    /**
     * 标题栏对象
     */
    private TitleBar mTitleBar;
    protected P mPresenter;
    private Unbinder unbinder;
    /**
     * 状态栏沉浸
     */
    private ImmersionBar mImmersionBar;

    /**
     * Activity 回调集合
     */
    private SparseArray<OnActivityCallback> mActivityCallbacks;

    /**
     * 加载对话框
     */
    private BaseDialog mDialog;
    /**
     * 对话框数量
     */
    private int mDialogTotal;
    public Handler handler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        unbinder = ButterKnife.bind(this);
        ininPresenter();
        initView();
        initData();
        TUIKit.addIMEventListener(mIMEventListener);
    }


    // 监听做成静态可以让每个子类重写时都注册相同的一份。
    private static IMEventListener mIMEventListener = new IMEventListener() {
        @Override
        public void onForceOffline() {
            ToastUtil.toastLongMessage(Application.instance().getString(R.string.repeat_login_tip));
            logout();
        }

        @Override
        public void onUserSigExpired() {
            /**
             * 加判断解决多次toast和启动页面的问题
             */
            LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, Application.instance());
            if (null != loginBean) {
                ToastUtil.toastLongMessage(Application.instance().getString(R.string.expired_login_tip));
                logout();
            }
        }
    };


    public static void logout() {
        SharedPreManager.putObject(Constants.KYE_USER_BEAN, null);
        SharedPreManager.putString(Constants.KEY_TOKEN, "");
        Intent intent = new Intent(Application.instance(), LoginHealthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Application.instance().startActivity(intent);
        // 进行内存优化，销毁除登录页之外的所有界面
        ActivityManager.getInstance().finishAllActivities(LoginHealthActivity.class);
    }


    protected boolean isLightStatusBar() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private void ininPresenter() {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void showLoading() {
        showDialog();
    }


    @Override
    public void dismissLoading() {
        hideDialog();
    }


    @Override
    public void onFail(Throwable ex, String code, String msg) {

        dismissLoading();
    }


    /**
     * 获取状态栏沉浸的配置对象
     */
    @NonNull
    public ImmersionBar getStatusBarConfig() {
        if (mImmersionBar == null) {
            mImmersionBar = createStatusBarConfig();
        }
        return mImmersionBar;
    }

    /**
     * 初始化沉浸式状态栏
     */
    @NonNull
    protected ImmersionBar createStatusBarConfig() {
        return ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(isStatusBarDarkFont())
                // 指定导航栏背景颜色
                .navigationBarColor(android.R.color.black)
                // 状态栏字体和导航栏内容自动变色，必须指定状态栏颜色和导航栏颜色才可以自动变色
                .autoDarkModeEnable(true, 0.2f);
    }


    /**
     * 状态栏字体深色模式
     */
    protected boolean isStatusBarDarkFont() {
        return true;
    }


    @Override
    public void onNetError() {
        dismissLoading();
        ToastUtils.show("网络似乎出了点问题");
    }

    /**
     * 创建Presenter
     *
     * @return Presenter
     */
    protected abstract P createPresenter();

    /**
     * 获取当前activity的id
     *
     * @return 当前xml的布局res ID
     */
    protected abstract int getLayoutID();


    /**
     * 初始化view控件
     */
    protected abstract void initView();


    protected abstract void initData();


    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void JumpActivity(Class<?> clz, String activityName) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        intent.putExtra("activity_name", activityName);
        startActivity(intent);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        // 隐藏软键，避免内存泄漏
        hideKeyboard(getCurrentFocus());
        // 查看源码得知 startActivity 最终也会调用 startActivityForResult
        super.startActivityForResult(intent, requestCode, options);
    }

    /**
     * startActivityForResult 方法优化
     */

    public void startActivityForResult(Class<? extends Activity> clazz, OnActivityCallback callback) {
        startActivityForResult(new Intent(this, clazz), null, callback);
    }

    public void startActivityForResult(Intent intent, OnActivityCallback callback) {
        startActivityForResult(intent, null, callback);
    }

    public void startActivityForResult(Intent intent, @Nullable Bundle options, OnActivityCallback callback) {
        if (mActivityCallbacks == null) {
            mActivityCallbacks = new SparseArray<>(1);
        }
        // 请求码必须在 2 的 16 次方以内
        int requestCode = new Random().nextInt((int) Math.pow(2, 16));
        mActivityCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        OnActivityCallback callback;
        if (mActivityCallbacks != null && (callback = mActivityCallbacks.get(requestCode)) != null) {
            callback.onActivityResult(resultCode, data);
            mActivityCallbacks.remove(requestCode);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        return mDialog != null && mDialog.isShowing();
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        mDialogTotal++;
        postDelayed(() -> {
            if (mDialogTotal <= 0 || isFinishing() || isDestroyed()) {
                return;
            }

            if (mDialog == null) {
                mDialog = new WaitDialog.Builder(this)
                        .setCancelable(false)
                        .create();
            }
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }, 1);
    }






    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        if (mDialogTotal > 0) {
            mDialogTotal--;
        }

        if (mDialogTotal == 0 && mDialog != null && mDialog.isShowing() && !isFinishing()) {
            mDialog.dismiss();
        }
    }
    /**
     * 和 setContentView 对应的方法
     */
    public ViewGroup getContentView() {
        return findViewById(Window.ID_ANDROID_CONTENT);
    }

    @Override
    @Nullable
    public TitleBar getTitleBar() {
        if (mTitleBar == null) {
            mTitleBar = obtainTitleBar(getContentView());
        }
        return mTitleBar;
    }

    @Override
    public void onLeftClick(View view) {
        Log.e(TAG, "---------------" );
        onBackPressed();
    }


    public void showToast(String messagContent) {
        ToastUtils.show(messagContent);
    }

    @Nullable
    @Override
    public Bundle getBundle() {
        return getIntent().getExtras();
    }

    public interface OnActivityCallback {

        /**
         * 结果回调
         *
         * @param resultCode 结果码
         * @param data       数据
         */
        void onActivityResult(int resultCode, @Nullable Intent data);
    }
}
