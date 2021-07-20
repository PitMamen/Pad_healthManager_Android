package com.bitvalue.sdk.collab.modules.conversation.holder;

import android.view.View;


import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.sdk.collab.modules.conversation.ConversationListAdapter;
import com.bitvalue.sdk.collab.modules.conversation.base.ConversationInfo;

public abstract class ConversationBaseHolder extends RecyclerView.ViewHolder {

    protected View rootView;
    protected ConversationListAdapter mAdapter;

    public ConversationBaseHolder(View itemView) {
        super(itemView);
        rootView = itemView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = (ConversationListAdapter) adapter;
    }

    public abstract void layoutViews(ConversationInfo conversationInfo, int position);

}
