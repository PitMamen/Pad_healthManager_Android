package com.bitvalue.sdk.collab.modules.forward;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.bitvalue.sdk.collab.component.TitleBarLayout;
import com.bitvalue.sdk.collab.modules.conversation.ConversationManagerKit;
import com.bitvalue.sdk.collab.modules.conversation.ConversationProvider;
import com.bitvalue.sdk.collab.modules.conversation.base.ConversationInfo;
import com.bitvalue.sdk.collab.modules.conversation.interfaces.IConversationAdapter;
import com.bitvalue.sdk.collab.modules.conversation.interfaces.IConversationLayout;
import com.bitvalue.sdk.collab.modules.conversation.interfaces.ILoadConversationCallback;
import com.bitvalue.sdk.collab.utils.ToastUtil;

public class ForwardSelectLayout extends RelativeLayout implements IConversationLayout {

    private TitleBarLayout mTitleBarLayout;
    private ForwardSelectListLayout mConversationList;

    public ForwardSelectLayout(Context context) {
        super(context);
        init();
    }

    public ForwardSelectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ForwardSelectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化相关UI元素
     */
    private void init() {
        inflate(getContext(), R.layout.forward_layout, this);
        mTitleBarLayout = findViewById(R.id.conversation_title);
        mConversationList = findViewById(R.id.conversation_list);
    }

    public void initDefault() {
        mTitleBarLayout.setTitle(getResources().getString(R.string.conversation_title), TitleBarLayout.POSITION.MIDDLE);
        mTitleBarLayout.getLeftGroup().setVisibility(View.GONE);
        mTitleBarLayout.setRightIcon(R.drawable.conversation_more);

        final IConversationAdapter adapter = new ForwardSelectListAdapter();
        mConversationList.setAdapter(adapter);
        ConversationManagerKit.getInstance().loadConversation(0, new ILoadConversationCallback() {
            @Override
            public void onSuccess(ConversationProvider provider, boolean isFinished, long nextSeq) {
                adapter.setDataProvider(provider);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage("加载消息失败");
            }
        });
    }

    public TitleBarLayout getTitleBar() {
        return mTitleBarLayout;
    }

    @Override
    public void setParentLayout(Object parent) {

    }

    @Override
    public ForwardSelectListLayout getConversationList() {
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
        //ConversationManagerKit.getInstance().setConversationTop(position, conversation);
    }

    @Override
    public void deleteConversation(int position, ConversationInfo conversation) {
        //ConversationManagerKit.getInstance().deleteConversation(position, conversation);
    }

    @Override
    public void clearConversationMessage(int position, ConversationInfo conversation) {

    }
}
