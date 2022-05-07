package com.bitvalue.health.util.chatUtil;

import static com.bitvalue.health.util.Constants.FRAGEMNT_PHONE_CONSULTATION;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.eventbusbean.NotiflyUIObj;
import com.bitvalue.health.api.responsebean.MessageInfoData;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;

public class CustomHealthPlanMessageController {


    public static void onDraw(ICustomMessageViewGroup parent, final CustomHealthPlanMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        final View view = LayoutInflater.from(Application.instance()).inflate(R.layout.layout_health_plan_message, null, false);
        parent.addMessageContentView(view);

        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView content1 = view.findViewById(R.id.tv_content);   //时间
        TextView content2 = view.findViewById(R.id.tv_content_two);   //时间
        TextView content3 = view.findViewById(R.id.tv_content_three);   //时间
        TextView tv_title = view.findViewById(R.id.tv_title);
        final String text = TUIKitImpl.getAppContext().getString(R.string.no_support_msg);
        if (data == null || EmptyUtil.isEmpty(data.time)) {
            return;
        }
        tv_title.setText(data == null ? text : data.description);
        String dataString = data.time;

        String[] secind = dataString.split(",");
        if (secind.length >= 1) {
            content1.setText("时间一:" + secind[0]);
            if (secind.length >= 2) {
                content2.setVisibility(View.VISIBLE);
                content2.setText("时间二:" + secind[1]);
                if (secind.length >= 3) {
                    content3.setVisibility(View.VISIBLE);
                    content3.setText("时间三:" + secind[2]);
                }
            }
        }
        if (!info.isSelf()&&data.description.equals("确认时间")){
            EventBus.getDefault().post(new NotiflyUIObj(data.tradeId));
        }



        view.setClickable(true);
        view.setOnClickListener(v -> {
            if (!info.isSelf()) {
                if (!data.description.equals("确认时间")) {
                    MessageInfoData messageInfoData = new MessageInfoData();
                    messageInfoData.time = secind[0];
                    messageInfoData.tradeId = data.tradeId;
                    Application.instance().getHomeActivity().switchSecondFragment(FRAGEMNT_PHONE_CONSULTATION, messageInfoData);
                } else {
                    ToastUtils.show("已确认过时间,无需再次选择!");
                }

            } else {
                Log.e("TAG", "非登录用户!!!");
            }


        });
    }
}
