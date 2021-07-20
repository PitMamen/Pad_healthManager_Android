package com.bitvalue.sdk.collab.modules.forward.holder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.sdk.collab.modules.conversation.base.ConversationInfo;
import com.bitvalue.sdk.collab.modules.conversation.holder.ConversationBaseHolder;
import com.bitvalue.sdk.collab.modules.forward.ForwardSelectListAdapter;
import com.tencent.imsdk.message.CustomElement;
import com.tencent.imsdk.message.FaceElement;
import com.tencent.imsdk.message.FileElement;
import com.tencent.imsdk.message.GroupTipsElement;
import com.tencent.imsdk.message.ImageElement;
import com.tencent.imsdk.message.LocationElement;
import com.tencent.imsdk.message.MergerElement;
import com.tencent.imsdk.message.SoundElement;
import com.tencent.imsdk.message.TextElement;
import com.tencent.imsdk.message.VideoElement;
import com.tencent.imsdk.v2.V2TIMMessage;

public class ForwardBaseHolder extends ConversationBaseHolder {

    protected View rootView;
    protected ForwardSelectListAdapter mAdapter;

    public ForwardBaseHolder(View itemView) {
        super(itemView);
        rootView = itemView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = (ForwardSelectListAdapter) adapter;
    }

    public void layoutViews(ConversationInfo conversationInfo, int position){

    }

}
