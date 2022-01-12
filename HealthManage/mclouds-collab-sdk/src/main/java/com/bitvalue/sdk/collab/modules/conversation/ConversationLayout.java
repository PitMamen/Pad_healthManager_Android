package com.bitvalue.sdk.collab.modules.conversation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.bitvalue.sdk.collab.component.TitleBarLayout;
import com.bitvalue.sdk.collab.modules.conversation.base.ConversationInfo;
import com.bitvalue.sdk.collab.modules.conversation.interfaces.IConversationAdapter;
import com.bitvalue.sdk.collab.modules.conversation.interfaces.IConversationLayout;

public class ConversationLayout extends RelativeLayout implements IConversationLayout {

    private ConversationListLayout mConversationList;

    public ConversationLayout(Context context) {
        super(context);
        init();
    }

    public ConversationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConversationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化相关UI元素
     */
    private void init() {
        inflate(getContext(), R.layout.conversation_layout, this);
        mConversationList = findViewById(R.id.conversation_list);
    }

    public void initDefault() {

        final IConversationAdapter adapter = new ConversationListAdapter();
        mConversationList.setAdapter(adapter);
        mConversationList.loadConversation(0);
    }


    @Override
    public TitleBarLayout getTitleBar() {
        return null;
    }

    @Override
    public void setParentLayout(Object parent) {

    }

    @Override
    public ConversationListLayout getConversationList() {
        return mConversationList;
    }

    public void addConversationInfo(int position, ConversationInfo info) {
        mConversationList.getAdapter().addItem(position, info);
    }

    public void removeConversationInfo(int position) {
        mConversationList.getAdapter().removeItem(position);
    }

    @Override
    public void setConversationTop(ConversationInfo conversation, IUIKitCallBack callBack) {
        ConversationManagerKit.getInstance().setConversationTop(conversation, callBack);
    }

    @Override
    public void deleteConversation(int position, ConversationInfo conversation) {
        ConversationManagerKit.getInstance().deleteConversation(position, conversation);
    }

    @Override
    public void clearConversationMessage(int position, ConversationInfo conversation) {
        ConversationManagerKit.getInstance().clearConversationMessage(position, conversation);
    }
}
