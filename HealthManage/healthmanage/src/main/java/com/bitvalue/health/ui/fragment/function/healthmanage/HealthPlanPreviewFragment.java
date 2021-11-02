package com.bitvalue.health.ui.fragment.function.healthmanage;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.base.BaseAdapter;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.HealthPlanDetailAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class HealthPlanPreviewFragment extends BaseFragment {

    @BindView(R.id.tv_plan_name)
    TextView tv_plan_name;

    @BindView(R.id.tv_join_time)
    TextView tv_join_time;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.list_health_plan)
    WrapRecyclerView list_health_plan;

    //    private SmartRefreshLayout mRefreshLayout;
    private HealthPlanDetailAdapter mAdapter;
    private ArrayList<String> mIds;
    private String planId;
    private PlanDetailResult planDetailResult;
    private List<PlanDetailResult.UserPlanDetailsDTO> userPlanDetails;
    private HomeActivity homeActivity;
    private PlanDetailResult planDetailResultFor;


    @Override
    public void initView(View rootView) {
        tv_title.setText("健康管理计划");

        planDetailResultFor = (PlanDetailResult) getArguments().getSerializable(Constants.PLAN_PREVIEW);
        homeActivity = (HomeActivity) getActivity();
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);
        planId = getArguments().getString(Constants.PLAN_ID);
        initList();
    }

    @OnClick({R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
        }
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
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_health_plan_detail;
    }

    private void initList() {
        mAdapter = new HealthPlanDetailAdapter(getActivity());
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                PlanDetailResult.UserPlanDetailsDTO userPlanDetailsDTO = planDetailResultFor.userPlanDetails.get(position);
                //planType   Quest   Remind  Knowledge   DrugGuide
                //健康计划类型【Evaluate：健康体检及评估 Quest：随访跟踪 DrugGuide：用药指导 Knowledge：健康科普 Remind：健康提醒 OutsideInformation：院外资料】
//                getQuestDetail(userPlanDetailsDTO);

                switch (userPlanDetailsDTO.planType) {
                    case "Quest":
                        QuestionResultBean.ListDTO listDTO = new QuestionResultBean.ListDTO();
                        listDTO.questUrl = userPlanDetailsDTO.questUrl;
                        homeActivity.switchSecondFragment(Constants.FRAGMENT_QUESTION_DETAIL, listDTO);
                        break;
                    case "Remind":
//                        et_text_msg.setText(taskPlanDetailBean.remindContent);
                        TaskPlanDetailBean taskPlanDetailBean = new TaskPlanDetailBean();
                        taskPlanDetailBean.remindContent = userPlanDetailsDTO.remindContent;
                        taskPlanDetailBean.voiceList = userPlanDetailsDTO.voiceList;
//                        processAudios(taskPlanDetailBean.voiceList);
                        homeActivity.switchSecondFragment(Constants.FRAGMENT_PLAN_MSG, taskPlanDetailBean);
                        break;
//                    case "Evaluate":
//                        ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
//                        if (null == result.getData()) {
//                            return;
//                        }
//                        msgData.id = result.getData().id + "";
//                        homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_ANALYSE_DISPLAY, msgData);
//                        break;
                    case "Knowledge":
                        ArticleBean articleBean = new ArticleBean();
                        articleBean.content = userPlanDetailsDTO.content;
                        articleBean.title = userPlanDetailsDTO.title;
                        homeActivity.switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, articleBean);
                        break;
                }
            }
        });
        list_health_plan.setAdapter(mAdapter);

        mAdapter.setData(userPlanDetails);

        if (null != planDetailResultFor && null != planDetailResultFor.userPlanDetails) {
            tv_plan_name.setText(planDetailResultFor.planName);
            tv_join_time.setText("加入时间:" + planDetailResultFor.startDate);

            //不能刷新
            mAdapter.setLastPage(true);
            mAdapter.setData(planDetailResultFor.userPlanDetails);
        }

    }



}
