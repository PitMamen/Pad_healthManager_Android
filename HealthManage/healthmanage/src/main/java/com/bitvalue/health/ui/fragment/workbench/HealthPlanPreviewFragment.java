package com.bitvalue.health.ui.fragment.workbench;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanPreviewContract;
import com.bitvalue.health.presenter.healthmanager.HealthPlanPreviewPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.HealthPlanDetailAdapter;
import com.bitvalue.health.ui.adapter.HealthPlanPreviewListAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 健康计划 按时间的任务列表
 */
public class HealthPlanPreviewFragment extends BaseFragment<HealthPlanPreviewPresenter> implements HealthPlanPreviewContract.View {

    @BindView(R.id.tv_plan_name)
    TextView tv_plan_name;

    @BindView(R.id.tv_join_time)
    TextView tv_join_time;

    @BindView(R.id.list_health_plan)
    RecyclerView planRecyclerView;


    private HealthPlanPreviewListAdapter mAdapter;
    private int planId=386;


    /**
     * 控件初始化
     * @param rootView
     */
    @Override
    public void initView(View rootView) {

//        planDetailResultFor = (PlanDetailResult) getArguments().getSerializable(Constants.PLAN_PREVIEW);
//        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);
//        planId = getArguments().getString(Constants.PLAN_ID);

        planRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HealthPlanPreviewListAdapter();

        planRecyclerView.setAdapter(mAdapter);



    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void initData() {
        Log.d(TAG,"initData");
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
        mAdapter.setNewData(taskListBeanList);
    }

    @Override
    public void queryHealthPlanContentSuccess(TaskPlanDetailBean taskPlanDetailBean) {

    }

    @Override
    public void queryHealthFail(String failMessage) {

    }
}
