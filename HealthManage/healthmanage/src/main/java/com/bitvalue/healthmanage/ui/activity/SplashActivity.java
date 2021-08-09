package com.bitvalue.healthmanage.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.aop.SingleClick;
import com.bitvalue.healthmanage.app.AppActivity;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.response.LoginBean;
import com.bitvalue.healthmanage.util.DemoLog;
import com.bitvalue.healthmanage.util.SharedPreManager;
import com.bitvalue.healthmanage.util.Utils;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;

import java.util.Timer;
import java.util.TimerTask;

//import butterknife.OnClick;

public class SplashActivity extends AppActivity {

    private TextView tv_jump;
    private int recLen = 3;
    private Timer timer = new Timer();
    private Handler handler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        tv_jump = findViewById(R.id.tv_jump);
        setOnClickListener(R.id.tv_jump);
        Utils.checkPermission(this);
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_jump:
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
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
            case Utils.REQ_PERMISSION_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
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
        startCountAndJump();
    }

    private void startCountAndJump() {
        timer.schedule(task, 1000, 1000);//等待时间一秒，停顿时间一秒
        /**
         * 正常情况下不点击跳过
         */
        handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            jumpActivity();
        }
    };

    private void jumpActivity() {
//        if (!SharedPreManager.getString(Constants.KEY_TOKEN).isEmpty() && SharedPreManager.getBoolean(Constants.KEY_IM_AUTO_LOGIN, false, this)) {
        if (!SharedPreManager.getString(Constants.KEY_TOKEN).isEmpty()) {
//            loginIM();
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

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    recLen--;
                    tv_jump.setText("跳过 " + recLen);
                    if (recLen < 0) {
                        timer.cancel();
                        tv_jump.setVisibility(View.GONE);//倒计时到0隐藏字体
                    }
                }
            });
        }

    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
