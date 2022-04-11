package com.bitvalue.health.util.chatUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;

public class CustomQuestionMessageController {

    private static final String TAG = CustomQuestionMessageController.class.getSimpleName();

    public static void onDraw(ICustomMessageViewGroup parent, final CustomWenJuanMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        final View view = LayoutInflater.from(Application.instance()).inflate(R.layout.layout_question_message, null, false);
        parent.addMessageContentView(view);

        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView tv_questionContent = view.findViewById(R.id.tv_questioncontent);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_content = view.findViewById(R.id.tv_content);
        final String text = TUIKitImpl.getAppContext().getString(R.string.no_support_msg);
        if (tv_content == null || tv_title == null) {
            return;
        }
        if (data == null) {
            tv_title.setText(text);
        } else {
            tv_title.setText("问卷内容");
        }
        tv_content.setText("问卷：点击查看问卷");
        tv_questionContent.setText("内容:" + data.name);
        view.setClickable(true);
        view.setOnClickListener(v -> {
            /**
             * 如果集合大于一个 则跳转至列表界面  如果等于1 则直接跳转至webView 问卷详情界面
             */
            if (!EmptyUtil.isEmpty(data.url)) {
                //跳转至问卷详情界面
                QuestionResultBean.ListDTO listDTO = new QuestionResultBean.ListDTO();
                listDTO.questUrl = data.url + "?userId=" + data.userId + "&execTime=" + TimeUtils.getCurrenTimeYMDHMS() + "&showsubmitbtn=hide";
                Application.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_QUESTION_DETAIL, listDTO);
            } else {
                Log.e(TAG, "data.url = null!!!");
            }
        });
        view.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onMessageLongClick(v, position, info);
            }
            return false;
        });
    }
}
