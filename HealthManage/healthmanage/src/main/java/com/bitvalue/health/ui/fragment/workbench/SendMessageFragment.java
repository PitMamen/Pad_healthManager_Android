package com.bitvalue.health.ui.fragment.workbench;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.SendUserRemind;
import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PlanTaskDetail;
import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanPreviewContract;
import com.bitvalue.health.contract.healthmanagercontract.SendMessageContract;
import com.bitvalue.health.presenter.healthmanager.HealthPlanPreviewPresenter;
import com.bitvalue.health.presenter.healthmanager.SendMessagePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.HealthPlanPreviewListAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.TypeConstants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 发送提醒 评估
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
    @BindView(R.id.tv_time)
    TextView tv_gotoDetail;
    @BindView(R.id.tv_text_analyse)
    TextView tv_text_analyse;
    @BindView(R.id.layout_back)
    LinearLayout back;


    private HomeActivity homeActivity;






    private NewLeaveBean.RowsDTO userInfo;


    /**
     * 控件初始化
     * @param rootView
     */
    @Override
    public void initView(View rootView) {
        back.setOnClickListener(v -> {
            if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0)
                homeActivity.getSupportFragmentManager().popBackStack();
        });
        if (getArguments()==null){
            return;
        }

        userInfo= (NewLeaveBean.RowsDTO) getArguments().getSerializable(Constants.USERINFO);

        if ( userInfo==null)
            return;
        if (TypeConstants.Evaluate.equals(userInfo.getSendPlanType())){
            tv_title.setText("健康评估");
            tv_text_analyse.setText("请输入健康评估的内容：");
            tv_send.setText("发送评估");
        }else {
            tv_title.setText("发送提醒");
        }



        if (userInfo!=null){
            String curen = TimeUtils.getCurrenTime();
            int finatime = Integer.valueOf(curen) - Integer.valueOf((userInfo.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
            img_head.setImageDrawable(userInfo.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
            tv_name.setText(userInfo.getUserName());
            tv_sex.setText(userInfo.getSex());
            tv_age.setText(finatime+"岁");
            tv_phone.setText(userInfo.getInfoDetail().getSjhm());
            tv_gotoDetail.setOnClickListener(v -> {
                homeActivity.switchSecondFragment(Constants.FRAGMENT_DETAIL, userInfo);
            });
        }


        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_text_tx.getText().toString().trim();
                if (TextUtils.isEmpty(content)){
                    ToastUtil.toastShortMessage("请输入内容");
                    return;
                }
                if (TypeConstants.Evaluate.equals(userInfo.getSendPlanType())){
                    sendUserEevaluate(content);
                }else if(TypeConstants.Remind.equals(userInfo.getSendPlanType())){
                    sendUserRemind(content);
                }


            }

        });
    }

    //健康评估
    private void sendUserEevaluate(String content) {
        SaveAnalyseApi saveAnalyseApi = new SaveAnalyseApi();
        saveAnalyseApi.evalContent = content;
        saveAnalyseApi.evalTime = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.YY_MM_DD_FORMAT_4);
        saveAnalyseApi.userId = userInfo.getUserId();
        saveAnalyseApi.planId = userInfo.planId;
        saveAnalyseApi.describe=content;
        saveAnalyseApi.taskId=userInfo.getTaskId();
        saveAnalyseApi.type=1;

        mPresenter.sendUserEevaluate(saveAnalyseApi);
    }

    //健康提醒
    private void sendUserRemind(String content) {
        SendUserRemind remindApi=new SendUserRemind();
        remindApi.setPlanId(userInfo.getPlanId());
        remindApi.setTaskId(userInfo.getTaskId());
        remindApi.setRemindContent(content);
        remindApi.setDescribe(content);
        remindApi.setUserId(userInfo.getUserId());
        remindApi.setType(1);
        mPresenter.sendUserRemind(remindApi);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
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
        String title="";
        if (TypeConstants.Evaluate.equals(userInfo.getSendPlanType())){
            title="发送健康评估";
        }else {
            title="发送健康提醒";
        }
        DataUtil.showNormalDialog(homeActivity, title, "发送成功！", "确定", "", new DataUtil.OnNormalDialogClicker() {
            @Override
            public void onPositive() {
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0)
                    homeActivity.getSupportFragmentManager().popBackStack();

            }

            @Override
            public void onNegative() {
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0)
                    homeActivity.getSupportFragmentManager().popBackStack();
            }
        });

    }


}
