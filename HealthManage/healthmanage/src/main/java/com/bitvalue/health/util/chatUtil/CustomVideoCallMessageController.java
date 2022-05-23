package com.bitvalue.health.util.chatUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.VideoPatientStatusBean;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.net.NetEngine;
import com.bitvalue.health.ui.activity.MeetingMainActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.helper.CustomVideoCallMessage;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.hjq.toast.ToastUtils;
import com.tencent.liteav.debug.GenerateTestUserSig;

/***
 * 自定义视频界面
 */
public class CustomVideoCallMessageController {

    private static final String TAG = CustomVideoCallMessageController.class.getSimpleName();
    private static TextView tv_content;
    private static VideoPatientStatusBean videoPatientStatusBean;

    public static void onDraw(ICustomMessageViewGroup parent, final CustomVideoCallMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        final View view = LayoutInflater.from(Application.instance()).inflate(R.layout.layout_health_message, null, false);
        parent.addMessageContentView(view);

        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);
        final String text = TUIKitImpl.getAppContext().getString(R.string.no_support_msg);
        if (tv_content == null || tv_title == null) {
            return;
        }
        tv_title.setText(null == data ? text : data.title);
        tv_content.setText(data.content);
        view.setClickable(true);

        getPatientAppointmentById(data, false);

        //这里是 在聊天对话框中，点击视频信息 进入视频界面的点击事件
        view.setOnClickListener(v -> getPatientAppointmentById(data, true));
        //长按点击事件  发出的消息  可删除、撤回、转发
        view.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onMessageLongClick(v, position, info);
            }
            return false;
        });
    }


    /**
     * 根据预约id获取患者的一行预约信息
     *
     * @param data
     * @param ifClick
     */
    public static void getPatientAppointmentById(CustomVideoCallMessage data, boolean ifClick) {
        try {
            NetEngine.getInstance().getDataDetail(data, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    videoPatientStatusBean = (VideoPatientStatusBean) o;

                    if (ifClick) {
                        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, Application.instance());
                        if (loginBean == null) {
                            Log.e(TAG, "Failed to get login bean.");
                            return;
                        }
                        MeetingMainActivity.enterRoom(Application.instance().getApplicationContext(),
                                data.msgDetailId, // room_id
                                String.valueOf(loginBean.getUser().user.userId),
                                loginBean.getUser().user.userName,
                                loginBean.getUser().user.avatarUrl,
                                GenerateTestUserSig.SDKAPPID,
                                loginBean.getUserSig(),
                                true, // 是否默认打开摄像头
                                true, // 是否默认打开麦克风
                                true); // 是否支持USB摄像头/视频采集盒
//                        }
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                }
            });
        } catch (Throwable throwable) {
            Log.e(TAG, "视频异常: " + throwable.getMessage());
            throwable.printStackTrace();
        }
    }
}
