package com.bitvalue.health.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.base.BaseActivity;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.MLog;
import com.bitvalue.health.util.PermissionUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.healthmanage.R;


import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * 欢迎界面
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.tv_jump)
    TextView tv_jump;

    private int recLen = 3;
    private Disposable subscription;

    //初始化权限
    @Override
    protected void initView() {
        boolean ifPermit = PermissionUtil.checkPermission(this);
        if (ifPermit) {
            startSubscribe();
        }
    }

    //“跳过”按钮的点击事件
    @OnClick({R.id.tv_jump})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_jump:
                if (null != subscription) {
                    subscription.dispose();
                }
                jumpActivity();
                break;
        }
    }

    @Override
    protected void initData() {

    }


    /**
     * 系统请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQ_PERMISSION_CODE:
                if (grantResults.length > 0 && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        startSubscribe();
    }


    //右下角倒计时显示
    private void startSubscribe() {
        subscription =  Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .take(4)
                .subscribe(aLong -> {
                            tv_jump.setText("跳过 " + (3 - aLong));
                            Log.d("interval", aLong + "");

                        },
                        MLog::e, () -> {
                            tv_jump.setVisibility(View.GONE);//倒计时到0隐藏字体
                            Log.d("interval", "Action0");
                            jumpActivity();
                        });
    }


    //根据用户是否处于登录状态 的界面跳转方法  跳转至主界面
    private void jumpActivity() {
        if (!SharedPreManager.getString(Constants.KEY_TOKEN).isEmpty()) {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        } else {
            //从闪屏界面跳转到登录界面
            Intent intent = new Intent(SplashActivity.this, LoginHealthActivity.class);
            startActivity(intent);
            finish();
        }

    }


    @Override
    protected void onDestroy() {
        if (null != subscription) {
            subscription.dispose();
        }
        super.onDestroy();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }
}
