package com.bitvalue.sdk.collab.modules.forward.message;

import android.view.View;
import android.widget.TextView;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.component.face.FaceManager;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;

public class ForwardMessageTextHolder extends ForwardMessageBaseHolder {

    private TextView msgBodyText;

    public ForwardMessageTextHolder(View itemView) {
        super(itemView);
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_text;
    }

    @Override
    public void initVariableViews() {
        msgBodyText = rootView.findViewById(R.id.msg_body_tv);
    }

    @Override
    public void layoutVariableViews(MessageInfo msg, int position) {
        msgBodyText.setVisibility(View.VISIBLE);
        if (msg.getExtra() != null) {
            FaceManager.handlerEmojiText(msgBodyText, msg.getExtra().toString(), false);
        }
        if (properties.getChatContextFontSize() != 0) {
            msgBodyText.setTextSize(properties.getChatContextFontSize());
        }
        if (msg.isSelf()) {
            if (properties.getRightChatContentFontColor() != 0) {
                msgBodyText.setTextColor(properties.getRightChatContentFontColor());
            }
        } else {
            if (properties.getLeftChatContentFontColor() != 0) {
                msgBodyText.setTextColor(properties.getLeftChatContentFontColor());
            }
        }
    }

}
