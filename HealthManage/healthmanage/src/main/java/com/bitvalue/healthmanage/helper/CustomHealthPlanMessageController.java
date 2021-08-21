package com.bitvalue.healthmanage.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.ui.fragment.ChatFragment;
import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.helper.CustomAnalyseMessage;
import com.bitvalue.sdk.collab.helper.CustomHealthPlanMessage;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;

import java.util.ArrayList;

public class CustomHealthPlanMessageController {

    private static final String TAG = CustomHealthPlanMessageController.class.getSimpleName();

    public static void onDraw(ICustomMessageViewGroup parent, final CustomHealthPlanMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        final View view = LayoutInflater.from(TUIKitImpl.getAppContext()).inflate(R.layout.layout_health_plan_message, null, false);
        parent.addMessageContentView(view);

        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_sex = view.findViewById(R.id.tv_sex);
        TextView tv_age = view.findViewById(R.id.tv_age);
        TextView tv_type = view.findViewById(R.id.tv_type);
        TextView tv_analyse = view.findViewById(R.id.tv_analyse);
        TextView tv_plan = view.findViewById(R.id.tv_plan);
        final String text = TUIKitImpl.getAppContext().getString(R.string.no_support_msg);
        if (tv_name == null || tv_name == null) {
            return;
        }
        if (data == null) {
            tv_name.setText(text);
        } else {
            tv_name.setText(data.userName);
        }
        tv_sex.setText(data.userSex);
        tv_age.setText(data.userAge);
        tv_type.setText("就诊类别：" + data.jzlb);
        tv_analyse.setText("初步诊断：" + data.cbzd);
        tv_plan.setText("健康管理：" + data.jkgl);


        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//TODO
//                AppApplication appApplication = (AppApplication) view.getContext();
//                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
////                msgData.userIds = new ArrayList<>();
////                msgData.userIds.add(mChatInfo.getId());//不是必填参数
//                msgData.id = data.pl + "";//这里id设置为 planId
//                appApplication.getHomeActivity().switchSecondFragment(com.bitvalue.healthmanage.Constants.FRAGMENT_HEALTH_PLAN_DETAIL, msgData);
            }
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
