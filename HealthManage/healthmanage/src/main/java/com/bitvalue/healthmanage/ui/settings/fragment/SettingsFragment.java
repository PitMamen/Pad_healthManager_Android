package com.bitvalue.healthmanage.ui.settings.fragment;

import android.os.Bundle;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.ui.fragment.NewsFragment;

public class SettingsFragment extends AppFragment {

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

    }

    @Override
    protected void initData() {

    }
}
