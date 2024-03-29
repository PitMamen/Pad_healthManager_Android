package com.bitvalue.sdk.collab.modules.chat.layout.message.holder;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.bitvalue.sdk.collab.utils.TUIKitConstants;


public class MessageTipsHolder extends MessageEmptyHolder {

    protected TextView mChatTipsTv;

    public MessageTipsHolder(View itemView) {
        super(itemView);
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_tips;
    }

    @Override
    public void initVariableViews() {
        mChatTipsTv = rootView.findViewById(R.id.chat_tips_tv);
    }

    @Override
    public void layoutViews(MessageInfo msg, int position) {
        super.layoutViews(msg, position);

        if (properties.getTipsMessageBubble() != null) {
            mChatTipsTv.setBackground(properties.getTipsMessageBubble());
        }
        if (properties.getTipsMessageFontColor() != 0) {
            mChatTipsTv.setTextColor(properties.getTipsMessageFontColor());
        }
        if (properties.getTipsMessageFontSize() != 0) {
            mChatTipsTv.setTextSize(properties.getTipsMessageFontSize());
        }

        if (msg.getStatus() == MessageInfo.MSG_STATUS_REVOKE) {
            if (msg.isSelf()) {
                msg.setExtra(TUIKit.getAppContext().getString(R.string.revoke_tips_you));
            } else if (msg.isGroup()) {
                String message = TUIKitConstants.covert2HTMLString(
                        (TextUtils.isEmpty(msg.getGroupNameCard())
                                ? msg.getFromUser()
                                : msg.getGroupNameCard()));
                msg.setExtra(message + TUIKit.getAppContext().getString(R.string.revoke_tips));
            } else {
                msg.setExtra(TUIKit.getAppContext().getString(R.string.revoke_tips_other));
            }
        }

        if (msg.getStatus() == MessageInfo.MSG_STATUS_REVOKE
                || (msg.getMsgType() >= MessageInfo.MSG_TYPE_GROUP_CREATE
                && msg.getMsgType() <= MessageInfo.MSG_TYPE_GROUP_MODIFY_NOTICE)) {
            if (msg.getExtra() != null) {
                mChatTipsTv.setText(Html.fromHtml(msg.getExtra().toString()));
            }
        }
    }

}
