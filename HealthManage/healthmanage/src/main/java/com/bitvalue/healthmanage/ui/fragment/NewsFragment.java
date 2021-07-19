package com.bitvalue.healthmanage.ui.fragment;

import android.os.Bundle;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;

public class NewsFragment extends AppFragment {
    private boolean is_need_toast;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    public static NewsFragment getInstance(boolean is_need_toast) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();//TODO 参数可改
        bundle.putBoolean("is_need_toast", is_need_toast);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    protected void initView() {
        is_need_toast = getArguments().getBoolean("is_need_toast");
    }

    @Override
    protected void initData() {

    }
}
