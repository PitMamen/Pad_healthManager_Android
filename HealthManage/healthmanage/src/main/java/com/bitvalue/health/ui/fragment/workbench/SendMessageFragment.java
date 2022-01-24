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
    @BindView(R.id.img_head)
    ImageView img_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_age)
    TextView tv_age;
    @BindView(R.id.tv_phone)
    TextView tv_phone;





    private HealthPlanPreviewListAdapter mAdapter;
    private String planId;
    private NewLeaveBean.RowsDTO userInfo;


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
        userInfo= (NewLeaveBean.RowsDTO) getArguments().getSerializable(Constants.USERINFO);

        if (planId==null || userInfo==null)
            return;

        tv_title.setText("发送提醒");


        if (userInfo!=null){
            String curen = TimeUtils.getCurrenTime();
            int finatime = Integer.valueOf(curen) - Integer.valueOf((userInfo.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
            img_head.setImageDrawable(userInfo.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
            tv_name.setText(userInfo.getUserName());
            tv_sex.setText(userInfo.getSex());
            tv_age.setText(finatime+"岁");
            tv_phone.setText(userInfo.getInfoDetail().getSjhm());
        }


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
                saveAnalyseApi.userId = userInfo.getUserId();
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
