package com.bitvalue.health.util.chatUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.health.Application;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.helper.CustomHealthPlanMessage;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;

public class CustomHealthPlanMessageController {

    private static final String TAG = CustomHealthPlanMessageController.class.getSimpleName();
    private static HomeActivity homeActivity;

    public static void onDraw(ICustomMessageViewGroup parent, final CustomHealthPlanMessage data, final int position, final MessageLayout.OnItemLongClickListener onItemLongClickListener, final MessageInfo info) {

        // 把自定义消息view添加到TUIKit内部的父容器里
        final View view = LayoutInflater.from(Application.instance()).inflate(R.layout.layout_health_plan_message, null, false);
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

        homeActivity = ((Application) view.getContext()).getHomeActivity();
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getDetail(data);
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

    /***
     * 这里健康计划请求方式 不再使用该方式
     * @param data
     */
//    private static void getDetail(CustomHealthPlanMessage data) {
//        GetPlanIdApi getPlanIdApi = new GetPlanIdApi();
//        getPlanIdApi.goodsId = data.goodsId;
//        getPlanIdApi.userId = data.userId;
//        EasyHttp.get(homeActivity).api(getPlanIdApi).request(new HttpCallback<ApiResult<String>>(homeActivity) {
//            @Override
//            public void onStart(Call call) {
//                super.onStart(call);
//            }
//
//            @Override
//            public void onSucceed(ApiResult<String> result) {
//                super.onSucceed(result);
//                if (result.getCode() == 0) {
//                    String planId = result.getData();
//                    ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
//                    msgData.userIds = new ArrayList<>();
//                    msgData.userIds.add(data.userId);
//                    msgData.id = planId + "";//这里id设置为 planId
//                    homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN_DETAIL, msgData);
//                } else {
//                    ToastUtil.toastShortMessage(result.getMessage());
//                }
//            }
//
//            @Override
//            public void onFail(Exception e) {
//                super.onFail(e);
//            }
//        });
//    }
}
