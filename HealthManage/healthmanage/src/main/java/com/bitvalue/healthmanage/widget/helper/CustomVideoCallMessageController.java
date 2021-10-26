package com.bitvalue.healthmanage.widget.helper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.healthmanage.util.Constants;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.model.ApiResult;
import com.bitvalue.healthmanage.http.api.GetStatusApi;
import com.bitvalue.healthmanage.http.bean.LoginBean;
import com.bitvalue.healthmanage.http.bean.VideoPatientStatusBean;
import com.bitvalue.healthmanage.widget.manager.SharedPreManager;
import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.helper.CustomVideoCallMessage;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.tencent.liteav.meeting.ui.MeetingMainActivity;

import okhttp3.Call;

public class CustomVideoCallMessageController {

    private static final String TAG = CustomVideoCallMessageController.class.getSimpleName();
    private static TextView tv_content;
    private static VideoPatientStatusBean videoPatientStatusBean;

    public static void onDraw(ICustomMessageViewGroup parent, final CustomVideoCallMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        final View view = LayoutInflater.from(AppApplication.instance()).inflate(R.layout.layout_health_message, null, false);
        parent.addMessageContentView(view);

        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);
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

        getDetail(data, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetail(data, true);
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

    private static void getDetail(CustomVideoCallMessage data, boolean ifClick) {
        GetStatusApi getStatusApi = new GetStatusApi();
        getStatusApi.id = data.id;
        EasyHttp.get(AppApplication.instance().getHomeActivity()).api(getStatusApi).request(new HttpCallback<ApiResult<VideoPatientStatusBean>>(AppApplication.instance().getHomeActivity()) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<VideoPatientStatusBean> result) {
                super.onSucceed(result);
                videoPatientStatusBean = result.getData();
                if (videoPatientStatusBean == null) {
                    return;
                }
                if (videoPatientStatusBean.attendanceStatus.equals("4")) {
                    tv_content.setText("视频看诊已结束");
                }

                if (ifClick) {
                    if (videoPatientStatusBean.attendanceStatus.equals("4")) {
                        tv_content.setText("视频看诊已结束");
                        ToastUtils.show("视频看诊已结束");
                    } else {
                        /*
                        Intent intent = new Intent(AppApplication.instance(), VideoConsultActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.ROOM_ID, data.msgDetailId);
                        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, AppApplication.instance());
                        if (null == loginBean) {
                            return;
                        }
                        int userId = loginBean.getUser().user.userId;
                        intent.putExtra(Constants.USER_ID, userId + "");
                        intent.putExtra(Constants.PLAN_ID, data.id);//data.id就是云看诊预约id
                        AppApplication.instance().startActivity(intent);

                         */

                        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, AppApplication.instance());
                        if (loginBean == null) {
                            Log.e(TAG, "Failed to get login bean.");
                            return;
                        }

                        MeetingMainActivity.enterRoom(AppApplication.instance().getApplicationContext(),
                                data.msgDetailId, // room_id
                                String.valueOf(loginBean.getUser().user.userId),
                                loginBean.getUser().user.userName,
                                loginBean.getUser().user.avatarUrl,
                                Constants.IM_APPId,
                                loginBean.getUserSig(),
                                true, // 是否默认打开摄像头
                                true, // 是否默认打开麦克风
                                true); // 是否支持USB摄像头/视频采集盒
                    }
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }
}
