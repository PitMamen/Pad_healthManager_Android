package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.SaveAnalyseApi;
import com.bitvalue.healthmanage.http.request.SaveTotalMsgApi;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.util.TimeUtils;
import com.bitvalue.sdk.collab.helper.CustomAnalyseMessage;
import com.bitvalue.sdk.collab.helper.CustomHealthMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class HealthAnalyseFragment extends AppFragment {
    @BindView(R.id.et_text_analyse)
    EditText et_text_analyse;

    @BindView(R.id.tv_send_analyse)
    TextView tv_send_analyse;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private ArrayList<String> mIds;
    private HomeActivity homeActivity;
    private String planId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_analyse_health;
    }

    @Override
    protected void initView() {
        tv_title.setText("健康评估");
        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    protected void initData() {
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);//要发送消息到患者的userId
        planId = getArguments().getString(Constants.PLAN_ID);
    }

    private void commitTotalMsg() {
        SaveAnalyseApi saveAnalyseApi = new SaveAnalyseApi();
        saveAnalyseApi.evalContent = et_text_analyse.getText().toString();
        saveAnalyseApi.evalTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.YY_MM_DD_FORMAT_4);
        saveAnalyseApi.userId = mIds.get(0);
        saveAnalyseApi.planId = planId;
        EasyHttp.post(this).api(saveAnalyseApi).request(new HttpCallback<HttpData<SaveAnalyseApi>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<SaveAnalyseApi> result) {
                super.onSucceed(result);
                //增加判空
                if (result == null || result.getData() ==null){
                    return;
                }
                if (result.getCode() == 0) {
                    ToastUtil.toastShortMessage("发送成功");
                    CustomAnalyseMessage message = new CustomAnalyseMessage();
                    message.title = "健康评估";
                    message.msgDetailId = result.getData().id + "";
                    message.userId = mIds;
                    message.content = result.getData().evalContent;
//                    message.content = saveTotalMsgApi.remindContent;
                    //这个属性区分消息类型 HelloChatController中onDraw方法去绘制布局
                    message.setType("CustomAnalyseMessage");
                    message.setDescription("健康评估消息");
                    EventBus.getDefault().post(message);
                    homeActivity.getSupportFragmentManager().popBackStack();
                } else {
                    ToastUtil.toastShortMessage(result.getMessage());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    @OnClick({R.id.tv_send_analyse, R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_analyse:
                if (et_text_analyse.getText().toString().isEmpty()) {
                    ToastUtil.toastShortMessage("请输入意见内容");
                    return;
                }
                commitTotalMsg();
                break;
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }
}
