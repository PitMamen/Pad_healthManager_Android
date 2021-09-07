package com.bitvalue.healthmanage.helper;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.response.LoginBean;
import com.bitvalue.healthmanage.ui.fragment.ChatFragment;
import com.bitvalue.healthmanage.util.SharedPreManager;
import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.helper.CustomHealthMessage;
import com.bitvalue.sdk.collab.helper.CustomVideoCallMessage;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.tencent.trtc.videocall.VideoCallingActivity;
import com.tencent.trtc.videocall.VideoCallingEnterActivity;

public class CustomVideoCallMessageController {

    private static final String TAG = CustomVideoCallMessageController.class.getSimpleName();

    public static void onDraw(ICustomMessageViewGroup parent, final CustomVideoCallMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        final View view = LayoutInflater.from(TUIKitImpl.getAppContext()).inflate(R.layout.layout_health_message, null, false);
        parent.addMessageContentView(view);

        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_content = view.findViewById(R.id.tv_content);
        final String text = TUIKitImpl.getAppContext().getString(R.string.no_support_msg);
        if (tv_content == null || tv_title == null) {
            return;
        }
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
                AppApplication appApplication = (AppApplication) view.getContext();
                Intent intent = new Intent(appApplication, VideoCallingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Constants.ROOM_ID, data.msgDetailId);
                int userId = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, appApplication).getUser().user.userId;
                intent.putExtra(Constants.USER_ID, userId + "");
                appApplication.startActivity(intent);

//                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
//                msgData.msgType = Constants.MSG_SINGLE;
//                msgData.userIds = data.userId;
//                msgData.id = data.msgDetailId;
//                appApplication.getHomeActivity().switchSecondFragment(Constants.FRAGMENT_SEND_MSG_DISPLAY,msgData);
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