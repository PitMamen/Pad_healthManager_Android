package com.bitvalue.health.util.chatUtil;

import static com.bitvalue.health.util.Constants.ACTION_QUESTION_DETAIL;
import static com.bitvalue.health.util.Constants.QUESTIONDETAIL;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.QuestionResultBean;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
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
            Application appApplication = (Application) view.getContext();
            /**
             * 如果集合大于一个 则跳转至列表界面  如果等于1 则直接跳转至webView 问卷详情界面
             */
            if (!EmptyUtil.isEmpty(data.url)){
                //跳转至问卷详情界面
                QuestionResultBean.ListDTO listDTO = new QuestionResultBean.ListDTO();
                listDTO.questUrl = data.url;
                Application.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_QUESTION_DETAIL, listDTO);

//                Intent intent = new Intent();
//                intent.setAction(ACTION_QUESTION_DETAIL);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(QUESTIONDETAIL, data.url);
//                appApplication.startActivity(intent);
            }else {
                Log.e(TAG, "data.url = null!!!");
            }

//            if (data.questionlistDTOS != null && data.questionlistDTOS.size() > 0) {
//                if (data.questionlistDTOS.size() > 1) {
//                    Intent intent = new Intent();
//                    intent.setAction(ACTION_QUESTION);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(QUESTIONLIST, (Serializable) data.questionlistDTOS);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtras(bundle);
//                    appApplication.startActivity(intent);
//                } else {
//                    //跳转至问卷详情界面
//                    Intent intent = new Intent();
//                    intent.setAction(ACTION_QUESTION_DETAIL);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra(QUESTIONDETAIL, data.url);
//                    appApplication.startActivity(intent);
//                }
//            } else {
//                Log.e(TAG, "data.questionlistDTOS = null!!!");
//            }
        });
        view.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onMessageLongClick(v, position, info);
            }
            return false;
        });
    }
}
