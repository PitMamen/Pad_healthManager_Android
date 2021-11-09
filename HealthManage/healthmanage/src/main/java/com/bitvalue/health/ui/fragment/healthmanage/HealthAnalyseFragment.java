package com.bitvalue.health.ui.fragment.healthmanage;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.HealthAnalseContract;
import com.bitvalue.health.presenter.healthmanager.HealthAnalsePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.helper.CustomAnalyseMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/***
 * 健康评估界面
 */
public class HealthAnalyseFragment extends BaseFragment<HealthAnalsePresenter> implements HealthAnalseContract.View {
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
    public void initView(View rootview) {
        tv_title.setText(getString(R.string.health_assessment));

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initData() {
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);//要发送消息到患者的userId
        planId = getArguments().getString(Constants.PLAN_ID);
    }

    @Override
    protected HealthAnalsePresenter createPresenter() {
        return new HealthAnalsePresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_analyse_health;
    }

    private void commitTotalMsg() {
        SaveAnalyseApi saveAnalyseApi = new SaveAnalyseApi();
        saveAnalyseApi.evalContent = et_text_analyse.getText().toString();
        saveAnalyseApi.evalTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.YY_MM_DD_FORMAT_4);
        saveAnalyseApi.userId = mIds.get(0);
        saveAnalyseApi.planId = planId;
        mPresenter.commitAnalse(saveAnalyseApi);
    }

    @OnClick({R.id.tv_send_analyse, R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_analyse:
                if (et_text_analyse.getText().toString().isEmpty()) {
                    ToastUtil.toastShortMessage("请输入意见内容");
                    return;
                }
                commitTotalMsg();  //提交评估
                break;
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }

    //提交健康评估成功回调
    @Override
    public void commitSuccess(SaveAnalyseApi resultBean) {
        getActivity().runOnUiThread(() -> {
            ToastUtil.toastShortMessage("发送成功");
            CustomAnalyseMessage message = new CustomAnalyseMessage();
            message.title = "健康评估";
            message.msgDetailId = resultBean.id + "";
            message.userId = mIds;
            message.content = resultBean.evalContent;
            message.contentId = resultBean.contentId;
//                    message.content = saveTotalMsgApi.remindContent;
            //这个属性区分消息类型 HelloChatController中onDraw方法去绘制布局
            message.setType("CustomAnalyseMessage");
            message.setDescription("健康评估消息");
            EventBus.getDefault().post(message);
            homeActivity.getSupportFragmentManager().popBackStack();
        });

    }

    @Override
    public void commitFail(String faiMessage) {
        getActivity().runOnUiThread(() -> ToastUtil.toastShortMessage(faiMessage));
    }
}
