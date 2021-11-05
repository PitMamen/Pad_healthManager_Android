package com.bitvalue.health.ui.fragment.healthmanage;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.healthmanagercontract.HealthAnalyseDisplayContract;
import com.bitvalue.health.presenter.healthmanager.HealthAnalyseDisplayPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.helper.CustomAnalyseMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 11/02
 * 健康评估详情列表
 */
public class HealthAnalyseDisplayFragment extends BaseFragment<HealthAnalyseDisplayPresenter> implements HealthAnalyseDisplayContract.View {
    @BindView(R.id.et_text_analyse)
    EditText et_text_analyse;

    @BindView(R.id.tv_send_analyse)
    TextView tv_send_analyse;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private ArrayList<String> mIds;
    private HomeActivity homeActivity;
    private String detailId;
    private String planId;


    @Override
    protected HealthAnalyseDisplayPresenter createPresenter() {
        return new HealthAnalyseDisplayPresenter();
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        tv_title.setText("健康评估");
    }


    /**
     * 接收从上个界面传过来的数据
     */
    @Override
    public void initData() {
        super.initData();
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);//要发送消息到患者的userId
        //显示详情的时候才要
        detailId = getArguments().getString(Constants.MSG_CUSTOM_ID);
        planId = getArguments().getString(Constants.PLAN_ID);
        mPresenter.getUserEevaluate(Integer.valueOf(detailId));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_analyse_health_dis;
    }


    //发送健康评估按钮的点击事件和 返回上一个界面的点击事件
    @OnClick({R.id.tv_send_analyse, R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_analyse:
                if (et_text_analyse.getText().toString().isEmpty()) {
                    ToastUtil.toastShortMessage("请输入意见内容");
                    return;
                }
                SaveAnalyseApi saveAnalyseApi = new SaveAnalyseApi();
                saveAnalyseApi.evalContent = et_text_analyse.getText().toString();
                saveAnalyseApi.evalTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.YY_MM_DD_FORMAT_4);
                saveAnalyseApi.userId = mIds.get(0);
                saveAnalyseApi.planId = planId;
                mPresenter.commitAnalse(saveAnalyseApi);
                break;
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }

    //请求线上接口 成功获取数据的回调
    @Override
    public void getUserEevaluateSuccess(SaveAnalyseApi EevaluateBean) {
         getActivity().runOnUiThread(() -> {
             et_text_analyse.setFocusable(false);
             et_text_analyse.setText(EevaluateBean.evalContent);
         });
    }

    //请求线上接口 获取数据失败的回调
    @Override
    public void getUserEevaluateFail(String messageFail) {
        getActivity().runOnUiThread(() -> ToastUtil.toastShortMessage(messageFail));
    }

    //提交健康评估成功的回调 (接口请求)
    @Override
    public void commitSuccess(SaveAnalyseApi resultBean) {
        getActivity().runOnUiThread(() -> {
            ToastUtil.toastShortMessage("发送成功");
            CustomAnalyseMessage message = new CustomAnalyseMessage();
            message.title = "健康评估";
            message.msgDetailId = resultBean.id + "";
            message.userId = mIds;
//                    message.content = saveTotalMsgApi.remindContent;
            //这个属性区分消息类型 HelloChatController中onDraw方法去绘制布局
            message.setType("CustomAnalyseMessage");
            message.setDescription("健康评估消息");
            EventBus.getDefault().post(message);
            homeActivity.getSupportFragmentManager().popBackStack();
        });
    }

    //提交健康评估失败的回调 (接口请求)
    @Override
    public void commitFail(String failmessage) {
        getActivity().runOnUiThread(() -> ToastUtil.toastShortMessage(failmessage));
    }
}
