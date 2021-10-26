package com.bitvalue.healthmanage.ui.fragment.function.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bitvalue.healthmanage.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.base.AppFragment;
import com.bitvalue.healthmanage.ui.activity.main.HomeActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsFragment extends AppFragment {

    @BindView(R.id.layout_plans)
    RelativeLayout layout_plans;
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
    }


    @OnClick({R.id.layout_rtc, R.id.layout_personal_data, R.id.layout_log,R.id.layout_plans})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.layout_rtc:
                //书写病历
                homeActivity.switchSecondFragment(Constants.FRAGMENT_MEDICINE_GUIDE, "");
                break;

            /***
             * 第三级界面跳转至个人信息界面
             */
            case R.id.layout_personal_data:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_PERSONAL_DATA, "");
                break;

            /**
             * 我的看诊记录
             */
            case R.id.layout_log:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT_LOG, "");
                break;

            /**
             * 套餐配置
             */
            case R.id.layout_plans:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN, "");
                break;

            default:
                break;
        }
    }

    @Override
    protected void initData() {

    }
}
