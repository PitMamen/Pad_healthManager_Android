package com.bitvalue.health.util.chatUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.health.Application;
import com.bitvalue.health.util.Constants;
import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;

public class CustomHealthDataMessageController {

    private static final String TAG = CustomHealthDataMessageController.class.getSimpleName();

    public static void onDraw(ICustomMessageViewGroup parent, final CustomHealthDataMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        final View view = LayoutInflater.from(Application.instance()).inflate(R.layout.layout_health_data_message, null, false);
        parent.addMessageContentView(view);

        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView tv_name = view.findViewById(R.id.tv_name);

        TextView tv_type = view.findViewById(R.id.tv_type);
        TextView tv_analyse = view.findViewById(R.id.tv_analyse);
        TextView tv_plan = view.findViewById(R.id.tv_plan);
        TextView tv_plan_end = view.findViewById(R.id.tv_plan_end);
        final String text = TUIKitImpl.getAppContext().getString(R.string.no_support_msg);
        if (tv_name == null || tv_name == null) {
            return;
        }
        if (data == null) {
            tv_name.setText(text);
        } else {
            tv_name.setText("患者资料上传");
        }
        tv_type.setText("就诊医院：" + data.jzyy);
        tv_analyse.setText("就诊时间：" + data.jzsj);
        tv_plan.setText("就诊类型：" + data.jzlx);
        tv_plan_end.setText("初步诊断：" + data.cbzd);


        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick-------------" );
                Application appApplication = (Application) view.getContext();
                appApplication.getHomeActivity().switchSecondFragment(Constants.FRAGMENT_USER_DATA, data);
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
