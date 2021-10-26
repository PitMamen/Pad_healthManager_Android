package com.bitvalue.healthmanage.ui.activity.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.healthmanage.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.base.AppActivity;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.bean.LoginBean;
import com.bitvalue.healthmanage.util.DemoLog;
import com.bitvalue.healthmanage.util.MLog;
import com.bitvalue.healthmanage.widget.manager.SharedPreManager;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.bitvalue.sdk.collab.utils.PermissionUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class SplashActivity extends AppActivity {

    @BindView(R.id.tv_jump)
    TextView tv_jump;

    private int recLen = 3;
    private Subscription subscription;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        boolean ifPermit = PermissionUtils.checkPermission(this);    //权限检测
        if (ifPermit) {
            startSubscribe();
        }
    }

    @OnClick({R.id.tv_jump})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_jump:
                if (null != subscription) {
                    subscription.unsubscribe();
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
            case PermissionUtils.REQ_PERMISSION_CODE:
                if (grantResults.length > 0 && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
//                    ToastUtils.show("未全部授权，部分功能可能无法使用！");
//                    new AlertDialog.Builder(SplashActivity.this)
//                            .setMessage("请先进行相关授权，再重启APP！")
//                            .setPositiveButton("进入设置", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    Intent intent = new Intent();
//                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
//                                    intent.setData(uri);
//                                    startActivity(intent);
//                                }
//                            })
//                            .setNegativeButton("退出", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    SplashActivity.this.finish();
//                                }
//                            })
//                            .setCancelable(false)
//                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        startSubscribe();
    }

    //右下角倒计时
    private void startSubscribe() {
        subscription = Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
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

    //界面跳转至主界面
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

    private void loginIM() {
        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, this);
        TUIKit.login(loginBean.getAccount().user.userId + "", loginBean.getAccount().user.userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        DemoLog.e("TUIKit.login", "登录聊天失败" + ", errCode = " + code + ", errInfo = " + desc);
                    }
                });
            }

            @Override
            public void onSuccess(Object data) {
                // 跳转到首页
                // HomeActivity.start(getContext(), MeFragment.class);
                SharedPreManager.putBoolean(Constants.KEY_IM_AUTO_LOGIN, true, AppApplication.instance());
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (null != subscription) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }
}
