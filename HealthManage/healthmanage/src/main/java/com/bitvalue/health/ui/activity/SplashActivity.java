package com.bitvalue.health.ui.activity;

import static com.bitvalue.health.util.Constants.APK_LOCAL_PATH;
import static com.bitvalue.health.util.Constants.APK_URL;
import static com.bitvalue.health.util.Constants.ISCLICKUPDATE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.api.responsebean.CheckNewVersionBean;
import com.bitvalue.health.base.BaseActivity;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.AppUpdateContract;
import com.bitvalue.health.presenter.homepersenter.AppUpdatePersenter;
import com.bitvalue.health.service.DownApkService;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.CrashHandler;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.MLog;
import com.bitvalue.health.util.MessageDialog;
import com.bitvalue.health.util.PermissionUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.VersionUtils;
import com.bitvalue.health.util.customview.dialog.AppUpdateDialog;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.FileUtil;


import java.math.BigDecimal;
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
    @BindView(R.id.tv_copyright)
    TextView tv_tv_copyright_show;

    private Disposable subscription;
    private AppUpdateDialog appUpdateDialog;
    private String cureentVersionname;
    private int cureentVersioncode;
    private String packgeAppId;

    //初始化权限
    @Override
    protected void initView() {
        boolean ifPermit = PermissionUtil.checkPermission(this);
        if (ifPermit) {
            startSubscribe();
        }
        cureentVersioncode = VersionUtils.getVersionCode(this);
        cureentVersionname = VersionUtils.getPackgeVersion(this);
        packgeAppId = VersionUtils.getPackgeAppId(this);
        tv_tv_copyright_show.setText(String.format("Copyright ©2016-2022 bitValue.All Rights Reserved \r\r\rv_%s", cureentVersionname));
        String old_apk_path = SharedPreManager.getString(APK_LOCAL_PATH, this);
        boolean isDeleted = FileUtil.deleteFile(old_apk_path);
        mPresenter.checkNewAppVersion();
        CrashHandler.getInstance().handleException("删除旧安装包:" + isDeleted, Constants.LOG_LOG);

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
                .take(5)
                .subscribe(aLong -> {
                            tv_jump.setText("跳过 " + (4 - aLong));
                            tv_jump.setOnClickListener(v -> jumpActivity());

                        },
                        MLog::e, () -> {
                            tv_jump.setVisibility(View.GONE);//倒计时到0隐藏字体
                            jumpActivity();
                        });
    }


    //根据用户是否处于登录状态 的界面跳转方法  跳转至主界面
    private void jumpActivity() {
        if (!SharedPreManager.getString(Constants.KEY_TOKEN).isEmpty()) {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            this.finish();
        } else {
            //从闪屏界面跳转到登录界面
            Intent intent = new Intent(SplashActivity.this, LoginHealthActivity.class);
            startActivity(intent);
            this.finish();
        }
        if (appUpdateDialog != null && appUpdateDialog.isShowing()) {
            appUpdateDialog.dismiss();
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

    /**
     * 检测app 新版本成功回调
     *
     * @param newVersionBean
     */
    @Override
    public void checkNewAppVersionSuccess(CheckNewVersionBean newVersionBean) {
        runOnUiThread(() -> {
            if (!EmptyUtil.isEmpty(newVersionBean)) {
                String updateTime = TimeUtils.getTimeToDay(newVersionBean.getUpdatedTime());
                String newVersionName = newVersionBean.getVersionCode();
                if (!EmptyUtil.isEmpty(newVersionName)){
                    newVersionName = newVersionName.contains("v") ? newVersionName.replace("v", "") : newVersionName;
                }
                int newVersionCode = newVersionBean.getVersionNumber();
                String updateContent = newVersionBean.getVersionDescription();
                String apkDownLoadUrl = newVersionBean.getDownloadUrl();
                if (newVersionCode > cureentVersioncode) {   //  版本 大于  当前版本
                    Log.e(TAG, "大于当前版本");
                    appUpdateDialog = new AppUpdateDialog(this).setOnExecuteClickListener(new AppUpdateDialog.OnExecuteClickListener() {
                        @Override
                        public void onPositiveClick() {
                            SharedPreManager.putBoolean(ISCLICKUPDATE,true,SplashActivity.this); //用户点击操作保存下来 HomeActivity中所用
                            Intent intent = new Intent(SplashActivity.this, DownApkService.class);
                            intent.putExtra(APK_URL, apkDownLoadUrl); //url
                            SplashActivity.this.startService(intent);
                            Log.e(TAG, "更新----");
                            CrashHandler.getInstance().handleException("用户选择更新应用", Constants.LOG_LOG);
                        }

                        @Override
                        public void onNegativeClick() {
                            Log.e(TAG, "忽略----");
                            SharedPreManager.putBoolean(ISCLICKUPDATE,false,SplashActivity.this);
                            CrashHandler.getInstance().handleException("用户选择忽略更新", Constants.LOG_LOG);
                        }
                    });
                    appUpdateDialog.show();
                    appUpdateDialog.setUpdateImformation(newVersionName, updateTime, !EmptyUtil.isEmpty(updateContent) ? updateContent : "");
                } else {
                    Log.e(TAG, "小于当前版本");
                }
            }
        });
    }


    /**
     * 检测app 新版本失败回调
     *
     * @param failMessage
     */
    @Override
    public void checkNewAppVersionFail(String failMessage) {

    }
}
