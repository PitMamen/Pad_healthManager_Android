package com.bitvalue.healthmanage.ui.fragment;

import android.os.Bundle;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;

public class ChatFragment extends AppFragment {
    private boolean is_need_toast;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    public static ChatFragment getInstance(boolean is_need_toast) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();//TODO 参数可改
        bundle.putBoolean("is_need_toast", is_need_toast);
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Override
    protected void initView() {
        is_need_toast = getArguments().getBoolean("is_need_toast");
    }

    @Override
    protected void initData() {

    }
}
