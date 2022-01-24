package com.bitvalue.health.ui.fragment.workbench;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PlanTaskDetail;
import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanPreviewContract;
import com.bitvalue.health.contract.healthmanagercontract.SendMessageContract;
import com.bitvalue.health.presenter.healthmanager.HealthPlanPreviewPresenter;
import com.bitvalue.health.presenter.healthmanager.SendMessagePresenter;
import com.bitvalue.health.ui.adapter.HealthPlanPreviewListAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 发送提醒
 */
public class SendMessageFragment extends BaseFragment<SendMessagePresenter> implements SendMessageContract.View {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_text_tx)
    EditText et_text_tx;
    @BindView(R.id.tv_send)
    TextView tv_send;






    private HealthPlanPreviewListAdapter mAdapter;
    private String planId;
    private String userId;


    /**
     * 控件初始化
     * @param rootView
     */
    @Override
    public void initView(View rootView) {

        if (getArguments()==null){
            return;
        }
        planId = getArguments().getString(Constants.PLAN_ID);
        userId = getArguments().getString(Constants.USER_ID);

        tv_title.setText("发送提醒");

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_text_tx.getText().toString().trim();
                if (TextUtils.isEmpty(msg)){
                    ToastUtil.toastShortMessage("请输入提醒内容");
                    return;
                }

                SaveAnalyseApi saveAnalyseApi = new SaveAnalyseApi();
                saveAnalyseApi.evalContent = msg;
                saveAnalyseApi.evalTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.YY_MM_DD_FORMAT_4);
                saveAnalyseApi.userId = userId;
                saveAnalyseApi.planId = planId;
                saveAnalyseApi.type=2;

                mPresenter.sendMessage(saveAnalyseApi);
            }
        });
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void initData() {


    }


    public void refreshData(String planId, String userId) {
        et_text_tx.setText("");
        this.planId=planId;
        this.userId=userId;
    }

    @Override
    protected SendMessagePresenter createPresenter() {
        return new SendMessagePresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_send_message;
    }



    @Override
    public void sendSuccess() {
        ToastUtil.toastShortMessage("发送成功！");
    }


}
