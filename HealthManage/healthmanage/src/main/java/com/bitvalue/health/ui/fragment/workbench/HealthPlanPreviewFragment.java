package com.bitvalue.health.ui.fragment.workbench;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.PlanTaskDetail;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanPreviewContract;
import com.bitvalue.health.presenter.healthmanager.HealthPlanPreviewPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.HealthPlanDetailAdapter;
import com.bitvalue.health.ui.adapter.HealthPlanPreviewListAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 健康计划  按时间的任务列表  (弃用)
 */
@Deprecated
public class HealthPlanPreviewFragment extends BaseFragment<HealthPlanPreviewPresenter> implements HealthPlanPreviewContract.View {

    @BindView(R.id.tv_title)
    TextView tv_title;
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

    @BindView(R.id.list_health_plan)
    RecyclerView planRecyclerView;




    private HealthPlanPreviewListAdapter mAdapter;
    private String planId;


    /**
     * 控件初始化
     * @param rootView
     */
    @Override
    public void initView(View rootView) {

//        planDetailResultFor = (PlanDetailResult) getArguments().getSerializable(Constants.PLAN_PREVIEW);
//        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);
        if (getArguments()==null){
            return;
        }
        planId = getArguments().getString(Constants.PLAN_ID);
        NewLeaveBean.RowsDTO userInfo= (NewLeaveBean.RowsDTO) getArguments().getSerializable(Constants.USERINFO);



        planRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HealthPlanPreviewListAdapter();

        planRecyclerView.setAdapter(mAdapter);

        refreshData(planId,userInfo);

    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void initData() {


    }

    public void refreshData(String planId, NewLeaveBean.RowsDTO userInfo) {



        if (userInfo!=null){
            String curen = TimeUtils.getCurrenTime();
            int finatime = Integer.valueOf(curen) - Integer.valueOf((userInfo.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
            img_head.setImageDrawable(userInfo.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
            tv_name.setText(userInfo.getUserName());
            tv_sex.setText(userInfo.getSex());
            tv_age.setText(finatime+"岁");
            tv_phone.setText(userInfo.getInfoDetail().getSjhm());
        }
        mPresenter.queryhealtPlan(planId);
    }



    @Override
    protected HealthPlanPreviewPresenter createPresenter() {
        return new HealthPlanPreviewPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_health_plan_preview;
    }


    @Override
    public void queryhealtPlanSuccess(List<HealthPlanTaskListBean> taskListBeanList) {
        tv_title.setText(taskListBeanList.get(0).getPlan_name());
        mAdapter.setNewData(taskListBeanList);
    }

    @Override
    public void queryHealthPlanContentSuccess(PlanTaskDetail taskPlanDetailBean) {

    }

    @Override
    public void queryHealthFail(String failMessage) {

    }

}
