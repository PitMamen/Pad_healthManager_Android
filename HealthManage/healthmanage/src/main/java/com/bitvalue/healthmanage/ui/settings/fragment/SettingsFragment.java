package com.bitvalue.healthmanage.ui.settings.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.aop.SingleClick;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.LogoutApi;
import com.bitvalue.healthmanage.http.request.TaskDetailApi;
import com.bitvalue.healthmanage.http.response.TaskDetailBean;
import com.bitvalue.healthmanage.manager.ActivityManager;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.activity.LoginHealthActivity;
import com.bitvalue.healthmanage.ui.fragment.HealthUploadDataFragment;
import com.bitvalue.healthmanage.util.SharedPreManager;
import com.bitvalue.healthmanage.widget.DataUtil;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.tencent.trtc.videocall.VideoCallingEnterActivity;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class SettingsFragment extends AppFragment {

    @BindView(R.id.layout_logout)
    RelativeLayout layout_logout;

    private RelativeLayout layout_plans;
    private HomeActivity homeActivity;

    public static SettingsFragment getInstance(boolean is_need_toast) {
        SettingsFragment newsFragment = new SettingsFragment();
        Bundle bundle = new Bundle();//TODO 参数可改
        bundle.putBoolean("is_need_toast", is_need_toast);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initView() {
        homeActivity = (HomeActivity) getActivity();
        layout_plans = getView().findViewById(R.id.layout_plans);
        setOnClickListener(R.id.layout_plans);
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_plans:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN, "");
                break;
        }
    }

    @OnClick({R.id.layout_logout, R.id.layout_rtc})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.layout_logout:
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定退出登录吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        logOut();
                    }

                    @Override
                    public void onNegative() {

                    }
                });
                break;
            case R.id.layout_rtc:
                Intent intent = new Intent(homeActivity, VideoCallingEnterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void logOut() {
        LogoutApi logoutApi = new LogoutApi();
        EasyHttp.get(this).api(logoutApi).request(new HttpCallback<HttpData<String>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<String> result) {
                super.onSucceed(result);
                //增加判空
                if (result == null){
                    return;
                }
                if (result.getCode() == 0) {
                    ToastUtil.toastShortMessage("退出登录成功");
                    Intent intent = new Intent(AppApplication.instance(), LoginHealthActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    AppApplication.instance().startActivity(intent);
                    SharedPreManager.putString(Constants.KEY_TOKEN, "");
//                    SharedPreManager.putObject(Constants.KYE_USER_BEAN, null);
                    // 进行内存优化，销毁除登录页之外的所有界面
                    ActivityManager.getInstance().finishAllActivities(LoginHealthActivity.class);
                } else {
                    ToastUtil.toastShortMessage(result.getMessage());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
