package com.bitvalue.health.ui.activity;

import static com.bitvalue.health.util.Constants.APK_LOCAL_PATH;
import static com.bitvalue.health.util.Constants.APK_URL;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.base.BaseActivity;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.AppUpdateContract;
import com.bitvalue.health.presenter.homepersenter.AppUpdatePersenter;
import com.bitvalue.health.service.DownApkService;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.CrashHandler;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.MLog;
import com.bitvalue.health.util.MessageDialog;
import com.bitvalue.health.util.PermissionUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.VersionUtils;
import com.bitvalue.health.util.customview.dialog.AppUpdateDialog;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.FileUtil;


import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * 欢迎界面
 */
public class SplashActivity extends BaseActivity<AppUpdatePersenter> implements AppUpdateContract.View {

    @BindView(R.id.tv_jump)
    TextView tv_jump;

    private int recLen = 3;
    private Disposable subscription;
    private AppUpdateDialog appUpdateDialog;

    //初始化权限
    @Override
    protected void initView() {
        boolean ifPermit = PermissionUtil.checkPermission(this);
        if (ifPermit) {
            startSubscribe();
        }
        int code = VersionUtils.getVersionCode(this);
        String name = VersionUtils.getPackgeVersion(this);
        String apkid = VersionUtils.getPackgeAppId(this);
        String old_apk_path = SharedPreManager.getString(APK_LOCAL_PATH, this);
        boolean isDeleted = FileUtil.deleteFile(old_apk_path);
//        CrashHandler.getInstance().handleException("删除旧安装包:" + isDeleted, Constants.LOG_LOG);
//        mPresenter.getAppDownUrl("HealthManage_v1.2.2_debug.apk");
//        Log.e(TAG, "code: " + code + "  name:" + name + " app id: " + apkid + " 是否删除旧文件：" + isDeleted);
//        appUpdateDialog = new AppUpdateDialog(this).setOnExecuteClickListener(new AppUpdateDialog.OnExecuteClickListener() {
//            @Override
//            public void onPositiveClick() {
//                Intent intent = new Intent(SplashActivity.this, DownApkService.class);
//                intent.putExtra(APK_URL, "http://192.168.1.122:8124/appManager/downloadApp/HealthManage_v1.2.2_debug.apk"); //url
//                SplashActivity.this.startService(intent);
//                Log.e(TAG, "更新----");
//                CrashHandler.getInstance().handleException("用户选择更新应用", Constants.LOG_LOG);
//            }
//
//            @Override
//            public void onNegativeClick() {
//                Log.e(TAG, "忽略----");
//                CrashHandler.getInstance().handleException("用户选择忽略更新", Constants.LOG_LOG);
//            }
//        });
//        appUpdateDialog.show();
//        appUpdateDialog.setUpdateImformation("1.2.3", "2022-05-25", "修复已知bug");
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
        subscription = Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .take(4)
                .subscribe(aLong -> {
                            tv_jump.setText("跳过 " + (3 - aLong));
                            tv_jump.setOnClickListener(v -> jumpActivity());

                        },
                        MLog::e, () -> {
                            tv_jump.setVisibility(View.GONE);//倒计时到0隐藏字体
                            jumpActivity();
                        });
    }


    //根据用户是否处于登录状态 的界面跳转方法  跳转至主界面
    private void jumpActivity() {
        if (appUpdateDialog != null && appUpdateDialog.isShowing()) {
            appUpdateDialog.dismiss();
        }

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
    protected AppUpdatePersenter createPresenter() {
        return new AppUpdatePersenter();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    public void getAppDownUrlSuccess(String url) {

    }

    @Override
    public void getAppDownUrlFaile(String messageFail) {

    }
}
