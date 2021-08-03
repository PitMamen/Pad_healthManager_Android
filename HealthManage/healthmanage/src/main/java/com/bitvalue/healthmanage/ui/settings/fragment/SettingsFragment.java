package com.bitvalue.healthmanage.ui.settings.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.aop.SingleClick;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;

public class SettingsFragment extends AppFragment {
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
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH,"");
                break;
        }
    }

    @Override
    protected void initData() {

    }
}
