package com.bitvalue.healthmanage.helper;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.GetStatusApi;
import com.bitvalue.healthmanage.http.request.ReportStatusApi;
import com.bitvalue.healthmanage.http.response.LoginBean;
import com.bitvalue.healthmanage.http.response.VideoClientsResultBean;
import com.bitvalue.healthmanage.http.response.VideoPatientStatusBean;
import com.bitvalue.healthmanage.util.SharedPreManager;
import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.helper.CustomVideoCallMessage;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.tencent.trtc.videocall.VideoConsultActivity;

import java.util.ArrayList;

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
        EasyHttp.get(AppApplication.instance().getHomeActivity()).api(getStatusApi).request(new HttpCallback<HttpData<VideoPatientStatusBean>>(AppApplication.instance().getHomeActivity()) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<VideoPatientStatusBean> result) {
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
