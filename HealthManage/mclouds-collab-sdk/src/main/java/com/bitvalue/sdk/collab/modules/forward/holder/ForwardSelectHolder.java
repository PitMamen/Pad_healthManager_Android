package com.bitvalue.sdk.collab.modules.forward.holder;

import android.view.View;
import android.widget.TextView;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.modules.conversation.base.ConversationInfo;


public class ForwardSelectHolder extends ForwardBaseHolder {
    private TextView titleView;
    public ForwardSelectHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.forward_title);
    }

    @Override
    public void layoutViews(ConversationInfo conversationInfo, int position) {

    }

    public void refreshTitile(boolean isCreateGroup){
        if (titleView == null)return;

        if (isCreateGroup){
            titleView.setText(TUIKit.getAppContext().getString(R.string.forward_select_new_chat));
        } else {
            titleView.setText(TUIKit.getAppContext().getString(R.string.forward_select_from_contact));
        }
    }
}
