package com.bitvalue.sdk.collab.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.bitvalue.sdk.collab.utils.ToastUtil;

public class CustomHealthMessageController {

    private static final String TAG = CustomHealthMessageController.class.getSimpleName();

    public static void onDraw(ICustomMessageViewGroup parent, final CustomHealthMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        final View view = LayoutInflater.from(TUIKitImpl.getAppContext()).inflate(R.layout.layout_health_message, null, false);
        parent.addMessageContentView(view);
//        parent.addMessageItemView(view);

        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_content = view.findViewById(R.id.tv_content);
        final String text = TUIKitImpl.getAppContext().getString(R.string.no_support_msg);
        if (data == null) {
            tv_title.setText(text);
        } else {
            tv_title.setText(data.title);
        }
        tv_content.setText(data.content);
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null){
                    onItemLongClickListener.onMessageLongClick(v, position, info);
                }
                return false;
            }
        });
    }
}
