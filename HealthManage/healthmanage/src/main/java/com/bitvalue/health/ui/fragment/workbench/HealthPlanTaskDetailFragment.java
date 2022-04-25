package com.bitvalue.health.ui.fragment.workbench;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.eventbusbean.VisitPlanRefreshObj;
import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.PlanTaskDetail;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanPreviewContract;
import com.bitvalue.health.model.planmodel.EndUuserPlanApi;
import com.bitvalue.health.presenter.healthmanager.HealthPlanPreviewPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.HealthPlanPreviewListAdapter;
import com.bitvalue.health.ui.adapter.HealthPlanTaskDetailAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.TypeConstants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 健康计划 每次任务详情
 */
public class HealthPlanTaskDetailFragment extends BaseFragment<HealthPlanPreviewPresenter> implements HealthPlanPreviewContract.View {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.layout_back)
    View layout_back;
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

    @BindView(R.id.list_health_plan)
    RecyclerView planRecyclerView;

    @BindView(R.id.ll_task_tetail)
    LinearLayout ll_task_tetail;
    @BindView(R.id.tv_plan_title)
    TextView tv_plan_title;
    @BindView(R.id.tv_plan_time)
    TextView tv_plan_time;
    @BindView(R.id.task_tetail_listview)
    RecyclerView taskRecyclerView;
    @BindView(R.id.rl_default_view)
    RelativeLayout default_view;


    private HealthPlanPreviewListAdapter planAdapter;   //点击随访计划 进入的第一个界面 患者详情下面显示的 list  (第N次随访任务)
    private HealthPlanTaskDetailAdapter taskDetailAdapter;
    private PlanTaskDetail planTaskDetail;
    private NewLeaveBean.RowsDTO userInfo;
    private String planId;
    private HomeActivity homeActivity;
    private View footerview;

    /**
     * 控件初始化
     *
     * @param rootView
     */
    @Override
    public void initView(View rootView) {

        layout_back.setOnClickListener(v -> {
            if (ll_task_tetail.getVisibility() == View.VISIBLE) {
                layout_back.setVisibility(View.GONE);
                ll_task_tetail.setVisibility(View.GONE);
                planRecyclerView.setVisibility(View.VISIBLE);
            } else {
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
            }
        });
        if (getArguments() == null) {
            return;
        }
        planId = getArguments().getString(Constants.PLAN_ID);
        userInfo = (NewLeaveBean.RowsDTO) getArguments().getSerializable(Constants.USERINFO);
        tv_gotoDetail.setOnClickListener(v -> {
            homeActivity.switchSecondFragment(Constants.DATA_REVIEW, userInfo.getUserId());
        });


        //任务列表
        planRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        planAdapter = new HealthPlanPreviewListAdapter();
        planRecyclerView.setAdapter(planAdapter);
        planAdapter.setOnItemClickListener((adapter, view, position) -> showTaskDetailView((HealthPlanTaskListBean) adapter.getItem(position)));

        //任务详情的列表
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskDetailAdapter = new HealthPlanTaskDetailAdapter();

        taskRecyclerView.setAdapter(taskDetailAdapter);


        refreshData(planId, userInfo);

    }

    private void initFooterViewClick(View footerview) {


        //健康评估
        Button tv_send_jkpg = footerview.findViewById(R.id.tv_send_jkpg);
        //健康提醒
        Button tv_send_jktx = footerview.findViewById(R.id.tv_send_jktx);
        //结束计划
        Button tv_send_end = footerview.findViewById(R.id.tv_send_end);
        tv_send_end.setVisibility(userInfo.isShowEndPlanButton ? View.VISIBLE : View.GONE);

        tv_send_jkpg.setOnClickListener(v -> {
            if (planTaskDetail == null || userInfo == null) {
                return;
            }
            userInfo.setSendPlanType(TypeConstants.Evaluate);
            userInfo.setPlanId(planTaskDetail.planId + "");
            userInfo.setTaskId(planTaskDetail.taskId);
            homeActivity.switchSecondFragment(Constants.FRAGMENT_SEND_MESSAGE, userInfo);
        });
        tv_send_jktx.setOnClickListener(v -> {
            if (planTaskDetail == null || userInfo == null) {
                return;
            }
            userInfo.setSendPlanType(TypeConstants.Remind);
            userInfo.setPlanId(planTaskDetail.planId + "");
            userInfo.setTaskId(planTaskDetail.taskId);
            homeActivity.switchSecondFragment(Constants.FRAGMENT_SEND_MESSAGE, userInfo);
        });
        tv_send_end.setOnClickListener(v -> {
            //二期开发功能
            EndUuserPlanApi endUuserPlanApi = new EndUuserPlanApi();
            endUuserPlanApi.id = userInfo.id_plan;  //计划的id
            EasyHttp.get(this).api(endUuserPlanApi).request(new HttpCallback<ApiResult<String>>(this) {
                @Override
                public void onSucceed(ApiResult<String> result) {
                    super.onSucceed(result);
                    ToastUtils.show("操作成功!");
                    if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        homeActivity.getSupportFragmentManager().popBackStack();
                    }
                    EventBus.getDefault().post(new VisitPlanRefreshObj());
                    Log.e(TAG, "onSucceed: " + result.toString());
                }

                @Override
                public void onFail(Exception e) {
                    super.onFail(e);
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            });
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initData() {


    }

    //刷新计划列表
    public void refreshData(String planId, NewLeaveBean.RowsDTO userInfo) {

        ll_task_tetail.setVisibility(View.GONE);
        planRecyclerView.setVisibility(View.VISIBLE);

        if (userInfo != null) {
            String curen = TimeUtils.getCurrenTime();
            if (!EmptyUtil.isEmpty(userInfo.getAge())) {
                int finatime = Integer.valueOf(curen) - Integer.valueOf((userInfo.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
                tv_age.setText(finatime + "岁");
            }
            if (!EmptyUtil.isEmpty(userInfo.getSex())) {
                img_head.setImageDrawable(userInfo.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
            }

            tv_name.setText(userInfo.getUserName());
            tv_sex.setText(userInfo.getSex());
            if (null != userInfo.getInfoDetail()) {
                tv_phone.setText(userInfo.getInfoDetail().getSjhm());
            }
        }
        if (planId == null) {
            ToastUtils.show("套餐计划Id为空!");
            return;
        }
        mPresenter.queryhealtPlan(planId);
    }

    //显示每次任务详情
    private void showTaskDetailView(HealthPlanTaskListBean item) {
        Log.e(TAG, "显示详情----------");
        layout_back.setVisibility(View.VISIBLE);
        ll_task_tetail.setVisibility(View.VISIBLE);
        planRecyclerView.setVisibility(View.GONE);
        tv_plan_title.setText(item.getTask_describe());
        tv_plan_time.setText(item.getExec_time() != null ? TimeUtils.getTime(item.getExec_time()) : "");

        if (!EmptyUtil.isEmpty(item.getPlan_id()) && !EmptyUtil.isEmpty(item.getTask_id()) && !EmptyUtil.isEmpty(item.getUser_id()))
            mPresenter.queryTaskDetail(item.getPlan_id(), item.getTask_id(), item.getUser_id());

    }


    @Override
    protected HealthPlanPreviewPresenter createPresenter() {
        return new HealthPlanPreviewPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_health_plan_task_detail;
    }


    @Override
    public void queryhealtPlanSuccess(List<HealthPlanTaskListBean> taskListBeanList) {
        default_view.setVisibility(taskListBeanList.size() == 0 ? View.VISIBLE : View.GONE);
        if (taskListBeanList.size() > 0) {
            tv_title.setText(taskListBeanList.get(0).getPlan_name());
            planAdapter.setNewData(taskListBeanList);
        }
    }

    @Override
    public void queryHealthPlanContentSuccess(PlanTaskDetail taskPlanDetailBean) {
        this.planTaskDetail = taskPlanDetailBean;
        taskDetailAdapter.setNewData(taskPlanDetailBean.userPlanDetails);
        if (footerview == null) {
            footerview = LayoutInflater.from(getContext()).inflate(R.layout.layout_plan_task_footer, null);
            initFooterViewClick(footerview);
            taskDetailAdapter.addFooterView(footerview);  //底部 健康评估 健康提醒 结束随访 按钮
        }

    }

    @Override
    public void queryHealthFail(String failMessage) {
        Log.e("TAG", failMessage);
        ToastUtil.toastShortMessage(failMessage);
    }

}
