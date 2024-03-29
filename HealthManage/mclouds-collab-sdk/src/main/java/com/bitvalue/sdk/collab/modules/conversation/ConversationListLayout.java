package com.bitvalue.sdk.collab.modules.conversation;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.component.CustomLinearLayoutManager;
import com.bitvalue.sdk.collab.modules.conversation.base.ConversationInfo;
import com.bitvalue.sdk.collab.modules.conversation.interfaces.IConversationAdapter;
import com.bitvalue.sdk.collab.modules.conversation.interfaces.IConversationListLayout;
import com.bitvalue.sdk.collab.modules.conversation.interfaces.ILoadConversationCallback;
import com.bitvalue.sdk.collab.utils.ToastUtil;

public class ConversationListLayout extends RecyclerView implements IConversationListLayout {

    public static final String TAG = ConversationListLayout.class.getSimpleName();

    private ConversationListAdapter mAdapter;
    private long mNextSeq = 0;
    private boolean isLoadFinished = false;
    public ConversationListLayout(Context context) {
        super(context);
        init();
    }

    public ConversationListLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConversationListLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        setLayoutFrozen(false);
        setItemViewCacheSize(0);
        setHasFixedSize(true);
        setFocusableInTouchMode(false);
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(linearLayoutManager);
    }

    @Override
    public void setBackground(int resId) {
        setBackgroundColor(resId);
    }

    @Override
    public void disableItemUnreadDot(boolean flag) {
        mAdapter.disableItemUnreadDot(flag);
    }

    @Override
    public void setItemAvatarRadius(int radius) {
        mAdapter.setItemAvatarRadius(radius);
    }

    @Override
    public void setItemTopTextSize(int size) {
        mAdapter.setItemTopTextSize(size);
    }

    @Override
    public void setItemBottomTextSize(int size) {
        mAdapter.setItemBottomTextSize(size);
    }

    @Override
    public void setItemDateTextSize(int size) {
        mAdapter.setItemDateTextSize(size);
    }

    @Override
    public ConversationListLayout getListLayout() {
        return this;
    }

    @Override
    public ConversationListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(IConversationAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (ConversationListAdapter) adapter;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mAdapter.setOnItemClickListener(listener);
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mAdapter.setOnItemLongClickListener(listener);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, ConversationInfo messageInfo);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(View view, int position, ConversationInfo messageInfo);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();

            if (mAdapter != null) {
                if (lastPosition == mAdapter.getItemCount() - 1 && !isLoadCompleted()) {
                    mAdapter.setIsLoading(true);
                    loadConversation(mNextSeq);
                }
            }
        }
    }

    public void loadConversation(long nextSeq) {
        ConversationManagerKit.getInstance().loadConversation(nextSeq, new ILoadConversationCallback() {
            @Override
            public void onSuccess(ConversationProvider provider, boolean isFinished, long nextSeq) {
                if (mAdapter != null) {
                    mAdapter.setDataProvider(provider);
                    mAdapter.setIsLoading(false);
                }
                ConversationListLayout.this.isLoadFinished = isFinished;
                if (!isFinished) {
                    ConversationListLayout.this.mNextSeq = nextSeq;
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage(getContext().getString(R.string.load_msg_error));
                if (mAdapter != null) {
                    mAdapter.setIsLoading(false);
                }
            }
        });
    }

    boolean isLoadCompleted(){
        return isLoadFinished;
    }
}