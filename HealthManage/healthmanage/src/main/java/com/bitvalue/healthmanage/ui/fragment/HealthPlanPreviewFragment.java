package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.GetPlanDetailApi;
import com.bitvalue.healthmanage.http.request.TaskDetailApi;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.PlanDetailResult;
import com.bitvalue.healthmanage.http.response.QuestionResultBean;
import com.bitvalue.healthmanage.http.response.TaskPlanDetailBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.HealthPlanDetailAdapter;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.bitvalue.healthmanage.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.bitvalue.healthmanage.widget.layout.WrapRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class HealthPlanPreviewFragment extends AppFragment {

    @BindView(R.id.tv_plan_name)
    TextView tv_plan_name;

    @BindView(R.id.tv_join_time)
    TextView tv_join_time;

    @BindView(R.id.tv_title)
    TextView tv_title;

    //    private SmartRefreshLayout mRefreshLayout;
    private HealthPlanDetailAdapter mAdapter;
    private WrapRecyclerView list_health_plan;
    private ArrayList<String> mIds;
    private String planId;
    private PlanDetailResult planDetailResult;
    private List<PlanDetailResult.UserPlanDetailsDTO> userPlanDetails;
    private HomeActivity homeActivity;
    private PlanDetailResult planDetailResultFor;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_health_plan_detail;
    }

    @Override
    protected void initView() {
        tv_title.setText("健康管理计划");

        planDetailResultFor = (PlanDetailResult) getArguments().getSerializable(Constants.PLAN_PREVIEW);
        homeActivity = (HomeActivity) getActivity();
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);
        planId = getArguments().getString(Constants.PLAN_ID);
        list_health_plan = (WrapRecyclerView) findViewById(R.id.list_health_plan);
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
    protected void initData() {

    }

    private void initList() {
//        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.rl_health_plan);

        mAdapter = new HealthPlanDetailAdapter(getAttachActivity());
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
                        AppApplication.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_QUESTION_DETAIL, listDTO);
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
                        AppApplication.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, articleBean);
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

//        getPlanData();
    }

    private void getQuestDetail(PlanDetailResult.UserPlanDetailsDTO userPlanDetailsDTO) {
        TaskDetailApi taskDetailApi = new TaskDetailApi();
        taskDetailApi.contentId = userPlanDetailsDTO.contentId + "";
        taskDetailApi.planType = userPlanDetailsDTO.planType;
        taskDetailApi.userId = mIds.get(0);
        EasyHttp.get(this).api(taskDetailApi).request(new HttpCallback<HttpData<TaskPlanDetailBean>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<TaskPlanDetailBean> result) {
                super.onSucceed(result);
                //增加判空
                if (result == null || result.getData() == null) {
                    return;
                }
                if (result.getCode() == 0) {
                    switch (userPlanDetailsDTO.planType) {
                        case "Quest":
                            QuestionResultBean.ListDTO listDTO = new QuestionResultBean.ListDTO();
                            if (userPlanDetailsDTO.execFlag == 1) {
                                listDTO.questUrl = result.getData().questUrl + "?userId=" + taskDetailApi.userId;
                            } else {
                                listDTO.questUrl = result.getData().questUrl;
                            }
                            AppApplication.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_QUESTION_DETAIL, listDTO);
                            break;
                        case "Remind":
                            homeActivity.switchSecondFragment(Constants.FRAGMENT_PLAN_MSG, result.getData());
                            break;
                        case "Evaluate":
                            ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                            if (null == result.getData()) {
                                return;
                            }
                            msgData.id = result.getData().id + "";
                            homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_ANALYSE_DISPLAY, msgData);
                            break;
                        case "Knowledge":
                            ArticleBean articleBean = new ArticleBean();
                            articleBean.content = result.getData().knowContent;
                            articleBean.title = result.getData().articleTitle;
                            AppApplication.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, articleBean);
                            break;
                        case "DrugGuide":
                            ToastUtil.toastShortMessage("用药提醒详情二期内容开发中");
                            break;
                        case "OutsideInformation":
                            CustomHealthDataMessage customHealthDataMessage = new CustomHealthDataMessage();
                            customHealthDataMessage.contentId = result.getData().contentId;
                            customHealthDataMessage.userId = result.getData().userId;
                            homeActivity.switchSecondFragment(Constants.FRAGMENT_USER_DATA, customHealthDataMessage);
                            break;
                    }
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

    private void getPlanData() {
        GetPlanDetailApi getPlanDetailApi = new GetPlanDetailApi();
        getPlanDetailApi.planId = planId;
        EasyHttp.get(this).api(getPlanDetailApi).request(new HttpCallback<HttpData<PlanDetailResult>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<PlanDetailResult> result) {
                super.onSucceed(result);
                planDetailResult = result.getData();
                userPlanDetails = planDetailResult.userPlanDetails;

                tv_plan_name.setText(planDetailResult.planName);
                tv_join_time.setText("加入时间:" + planDetailResult.startDate);

                mAdapter.setData(userPlanDetails);

                //不能刷新
                mAdapter.setLastPage(true);
//                mRefreshLayout.setNoMoreData(mAdapter.isLastPage());
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }


//    /**
//     * 模拟数据
//     */
//    private List<PlanBean> analogData() {
//        List<PlanBean> data = new ArrayList<>();
//        for (int i = mAdapter.getItemCount(); i < mAdapter.getItemCount() + 20; i++) {
//            PlanBean planBean;
//            if (i % 3 == 0) {
//                planBean = new PlanBean("我是第" + i + "条目", "启用");
//            } else {
//                planBean = new PlanBean("我是第" + i + "条目", "停用");
//            }
//            data.add(planBean);
//        }
//        return data;
//    }
}
