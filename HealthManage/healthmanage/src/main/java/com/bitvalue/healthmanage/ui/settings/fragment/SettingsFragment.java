package com.bitvalue.healthmanage.ui.settings.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.aop.SingleClick;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.fragment.NewsFragment;

public class SettingsFragment extends AppFragment {
    private TextView tv_health;
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
        tv_health = getView().findViewById(R.id.tv_health);
        setOnClickListener(R.id.tv_health);
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_health:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH,"");
                break;
        }
    }

    @Override
    protected void initData() {

    }
}
