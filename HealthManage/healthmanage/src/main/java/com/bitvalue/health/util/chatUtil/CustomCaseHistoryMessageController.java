package com.bitvalue.health.util.chatUtil;

import static com.bitvalue.health.util.Constants.ACTION_ARTICLE_DETAIL;
import static com.bitvalue.health.util.Constants.ARTICLEDETAIL;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;

public class CustomCaseHistoryMessageController {

    private static final String TAG = CustomCaseHistoryMessageController.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void onDraw(ICustomMessageViewGroup parent, final CustomCaseHistoryMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        final View view = LayoutInflater.from(Application.instance()).inflate(R.layout.layout_msg_case_history, null, false);
        parent.addMessageContentView(view);

        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_content = view.findViewById(R.id.tv_content);
//        TextView tv_suggestion = view.findViewById(R.id.tv_suggestion);
        final String text = TUIKitImpl.getAppContext().getString(R.string.no_support_msg);
        if (tv_content == null || tv_title == null) {
            return;
        }
        tv_title.setText(data == null ? text : data.title);
        tv_content.setText(data.content);
//        tv_suggestion.setText(data.suggestion);
        view.setClickable(true);
        view.setOnClickListener(v -> {
            Application appApplication = (Application) view.getContext();

            /**
             * 如果集合大于一个 则跳转至列表界面  如果等于1 则直接跳转至webView 问卷详情界面
             */
            if (!EmptyUtil.isEmpty(data.url)) {
                //跳转至文章详情界面

                ArticleBean articleBean = new ArticleBean();
                articleBean.previewUrl = data.url;
                appApplication.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, articleBean);

            } else {
                Log.e(TAG, "onDraw: data.articleBeanurl = null");
            }


//            if (data.maparticleList != null && data.maparticleList.size() > 0) {
//                if (data.maparticleList.size() > 1) {
//                    Intent intent = new Intent();
//                    intent.setAction(ACTION_ARTICLE);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra(ARTICLE_LIST, (Serializable) data.beanList);
//                    appApplication.startActivity(intent);
//                } else {
//                    //跳转至文章详情界面
//                    Intent intent = new Intent();
//                    intent.setAction(ACTION_ARTICLE_DETAIL);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra(ARTICLEDETAIL, data.url);
//
//                    appApplication.startActivity(intent);
//                }
//            } else {
//                Log.e(TAG, "onDraw: data.articleBeanList = null");
//            }


//                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
//                msgData.userIds =  data.userId;
//                msgData.id = data.msgDetailId;
//                msgData.appointmentId = data.appointmentId;
//                appApplication.getHomeActivity().switchSecondFragment(Constants.FRAGMENT_HEALTH_HISTORY_PREVIEW, msgData);
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onMessageLongClick(v, position, info);
                }
                return false;
            }
        });
    }
}
