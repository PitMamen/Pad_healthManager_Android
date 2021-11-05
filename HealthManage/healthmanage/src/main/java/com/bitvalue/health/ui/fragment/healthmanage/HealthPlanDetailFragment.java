package com.bitvalue.health.ui.fragment.healthmanage;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.base.BaseAdapter;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanDetailContract;
import com.bitvalue.health.presenter.healthmanager.HealthPlanDetailPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.HealthPlanDetailAdapter;
import com.bitvalue.health.ui.fragment.chat.ChatFragment;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 11/01
 * <p>
 * 健康计划详情界面
 */
public class HealthPlanDetailFragment extends BaseFragment<HealthPlanDetailPresenter> implements HealthPlanDetailContract.View {
    @BindView(R.id.tv_plan_name)
    TextView tv_plan_name;

    @BindView(R.id.tv_join_time)
    TextView tv_join_time;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.list_health_plan)
    WrapRecyclerView list_health_plan;


    private HealthPlanDetailAdapter mAdapter;

    private ArrayList<String> mIds = new ArrayList<>();
    private String planId;
    private PlanDetailResult planDetailResultbean;
    private List<PlanDetailResult.UserPlanDetailsDTO> userPlanDetails;
    private HomeActivity homeActivity;
    private PlanDetailResult.UserPlanDetailsDTO userPlanDetailsDTO;


    @Override
    protected HealthPlanDetailPresenter createPresenter() {
        return new HealthPlanDetailPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_health_plan_detail;
    }


    /**
     * 初始化adapter 及 接收上个界面传过来的数据
     * @param rootView
     */
    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        tv_title.setText(getString(R.string.health_management_plan));
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);
        planId = getArguments().getString(Constants.PLAN_ID);
        mPresenter.queryhealtPlan(planId);//健康计划详情查看（任务列表查看）
        mAdapter = new HealthPlanDetailAdapter(homeActivity);
        //item 点击事件
        mAdapter.setOnItemClickListener((recyclerView, itemView, position) -> {
            userPlanDetailsDTO = userPlanDetails.get(position);
            //planType   Quest   Remind  Knowledge   DrugGuide
            //健康计划类型【Evaluate：健康体检及评估 Quest：随访跟踪 DrugGuide：用药指导 Knowledge：健康科普 Remind：健康提醒 OutsideInformation：院外资料】
            mPresenter.queryHealthPlanContent(String.valueOf(userPlanDetailsDTO.contentId), userPlanDetailsDTO.planType,mIds.get(0));  //查看任务详情
        });
        list_health_plan.setAdapter(mAdapter);
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
        homeActivity = (HomeActivity) getActivity();
    }


    /***
     * 获取健康计划成功回调
     * @param planDetailResult
     */
    @Override
    public void queryhealtPlanSuccess(PlanDetailResult planDetailResult) {
        getActivity().runOnUiThread(() -> {
            planDetailResultbean = planDetailResult;
            userPlanDetails = planDetailResultbean.userPlanDetails;

            tv_plan_name.setText(planDetailResultbean.planName);
            tv_join_time.setText("加入时间:" + planDetailResultbean.startDate);

            mAdapter.setData(userPlanDetails);

            //不能刷新
            mAdapter.setLastPage(true);
        });

    }


    /***
     * 查看任务详情  成功回调
     * @param taskPlanDetailBean
     */
    @Override
    public void queryHealthPlanContentSuccess(TaskPlanDetailBean taskPlanDetailBean) {
        //增加判空
        if (taskPlanDetailBean == null){
            return;
        }
        getActivity().runOnUiThread(() -> {
            switch (userPlanDetailsDTO.planType) {
                case "Quest":
                    QuestionResultBean.ListDTO listDTO = new QuestionResultBean.ListDTO();
                    if (userPlanDetailsDTO.execFlag == 1) {
                        listDTO.questUrl = taskPlanDetailBean.questUrl + "?userId=" + mIds.get(0);
                    } else {
                        listDTO.questUrl = taskPlanDetailBean.questUrl;
                    }
                    Application.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_QUESTION_DETAIL, listDTO);
                    break;
                case "Remind":
                    homeActivity.switchSecondFragment(Constants.FRAGMENT_PLAN_MSG, taskPlanDetailBean);
                    break;
                case "Evaluate":
                    ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                    if (null == taskPlanDetailBean) {
                        return;
                    }
                    msgData.id = taskPlanDetailBean.id + "";
                    homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_ANALYSE_DISPLAY, msgData);
                    break;
                case "Knowledge":
                    ArticleBean articleBean = new ArticleBean();
                    articleBean.content = taskPlanDetailBean.knowContent;
                    articleBean.title = taskPlanDetailBean.articleTitle;
                    Application.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, articleBean);
                    break;
                case "DrugGuide":
                    ToastUtil.toastShortMessage("用药提醒详情二期内容开发中");
                    break;
                case "OutsideInformation":
                    CustomHealthDataMessage customHealthDataMessage = new CustomHealthDataMessage();
                    customHealthDataMessage.contentId = taskPlanDetailBean.contentId;
                    customHealthDataMessage.userId = taskPlanDetailBean.userId;
                    homeActivity.switchSecondFragment(Constants.FRAGMENT_USER_DATA, customHealthDataMessage);
                    break;
            }
        });



    }

    /***
     * 获取健康计划失败回调
     * @param
     */
    @Override
    public void queryHealthFail(String failMessage) {
       getActivity().runOnUiThread(() -> ToastUtil.toastShortMessage(failMessage));
    }
}
