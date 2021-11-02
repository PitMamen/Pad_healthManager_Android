package com.bitvalue.health.ui.fragment.function.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.healthmanage.R;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 个人设置界面
 */

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.layout_plans)
    RelativeLayout layout_plans;
    @BindView(R.id.layout_log)
    RelativeLayout layout_logs;
    @BindView(R.id.layout_personal_data)
    RelativeLayout layout_person;
    private HomeActivity homeActivity;

    public static SettingsFragment getInstance(boolean is_need_toast) {
        SettingsFragment newsFragment = new SettingsFragment();
        Bundle bundle = new Bundle();//TODO 参数可改
        bundle.putBoolean("is_need_toast", is_need_toast);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }


    @Override
    public void initView(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        homeActivity = (HomeActivity) getActivity();
    }

    @OnClick({R.id.layout_rtc, R.id.layout_personal_data, R.id.layout_log, R.id.layout_plans})
    public void onViewClick(View view) {
        switch (view.getId()) {
            //个人信息
            case R.id.layout_personal_data:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_PERSONAL_DATA, "");
                break;

            //套餐配置
            case R.id.layout_plans:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN, "");
                break;

            //看诊记录
            case R.id.layout_log:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT_LOG, "");
                break;

            //书写病历
            case R.id.layout_rtc:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_MEDICINE_GUIDE, "");
                break;


            default:
                break;

        }
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_settings;
    }
}
