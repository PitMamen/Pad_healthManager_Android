package com.bitvalue.health.util.chatUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.health.Application;
import com.bitvalue.health.util.customview.dialog.SummaryDialog;
import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;

public class CustomAnalyseMessageController {

    private static final String TAG = CustomAnalyseMessageController.class.getSimpleName();

    public static void onDraw(ICustomMessageViewGroup parent, final CustomAnalyseMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        final View view = LayoutInflater.from(Application.instance()).inflate(R.layout.layout_analyse_message, null, false);
        parent.addMessageContentView(view);
        Application application = (Application) view.getContext();
        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_content = view.findViewById(R.id.tv_content);
        final String text = TUIKitImpl.getAppContext().getString(R.string.no_support_msg);
        if (tv_content == null || tv_title == null) {
            return;
        }

        tv_title.setText(data == null ? text : data.title);
        tv_content.setText("内容:" + data.content);
        view.setClickable(true);
        view.setOnClickListener(v -> {
            SummaryDialog summaryDialog = new SummaryDialog(application.getHomeActivity());
            summaryDialog.setCanceledOnTouchOutside(true);
            summaryDialog.show();
            summaryDialog.setVisibleBotomButton(false);
            summaryDialog.setEditeTextString(data.content);
            summaryDialog.setDocNameAndSummaryTime(data.dealName, data.time);
        });
//        view.setOnLongClickListener(v -> {
//            if (onItemLongClickListener != null) {
//                onItemLongClickListener.onMessageLongClick(v, position, info);
//            }
//            return false;
//        });
    }
}
