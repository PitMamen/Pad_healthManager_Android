package com.bitvalue.sdk.collab.modules.chat.base;


import com.bitvalue.sdk.collab.base.BaseFragment;
import com.bitvalue.sdk.collab.modules.chat.interfaces.IChatLayout;

public class BaseInputFragment extends BaseFragment {

    private IChatLayout mChatLayout;

    public IChatLayout getChatLayout() {
        return mChatLayout;
    }

    public BaseInputFragment setChatLayout(IChatLayout layout) {
        mChatLayout = layout;
        return this;
    }
}
