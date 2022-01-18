package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.LISTBEAN;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PlanListBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.ViewCallback;
import com.bitvalue.health.model.planmodel.PlanListApi;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.PlansAdapter;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.health.util.customview.MPopupWindow;
import com.bitvalue.health.util.customview.TypeGravity;
import com.bitvalue.healthmanage.R;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author created by bitvalue
 * @data : 01/14
 */
public class FollowUpPlanFragment extends BaseFragment implements OnHttpListener {
    @BindView(R.id.rl_back)
    RelativeLayout back;
    @BindView(R.id.tv_addplan)
    TextView tv_addplan;

    @BindView(R.id.tv_sortby_time)
    TextView tv_sortby_time;


    @BindView(R.id.list_plans)
    RecyclerView list_plans;

    private ArrayList<PlanListBean> planListBeans = new ArrayList<>();
    private PlansAdapter plansAdapter;
    private MPopupWindow mPopupWindow;
    private boolean isSortTime = true;//初始值按时间排序
    private HomeActivity homeActivity;
    private List<NewLeaveBean.RowsDTO> sourcePatientList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_followup_plan_layout;
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        list_plans.setLayoutManager(new LinearLayoutManager(homeActivity));
//        list_plans.addItemDecoration(MUtils.spaceDivider(DensityUtil.dip2px(homeActivity, this.getResources().getDimension(R.dimen.qb_px_4)), false));
        plansAdapter = new PlansAdapter(R.layout.item_plan_list, planListBeans);
        Bundle bundle = getArguments();
        sourcePatientList  = (List<NewLeaveBean.RowsDTO>) bundle.getSerializable(LISTBEAN);
        plansAdapter.setOnItemClickListener((adapter, view, position) -> {
//            mealOrderPackege(Integer.valueOf(planListBeans.get(position).templateId), userid);
//            //TODO 如果是出院患者界面跳详过来的 点击当前套餐请求接口，实现套餐绑定,如果是首页或者其他界面跳转过来的 点击当前套餐则进入详情
//            if (!EmptyUtil.isEmpty(userid)) {
//                // TODO: 2022/1/5 请求购买套餐接口
//                showDialog();
//                mealOrderPackege(Integer.valueOf(planListBeans.get(position).templateId), userid);
//            } else {
//                Intent intent = new Intent(FollowUpPlanActivity.this, EditCreatePlanActivity.class);
//                intent.putExtra(Constants.PLAN_LIST_BEAN, planListBeans.get(position).templateId);
//                startActivity(intent);
//            }
        });
        list_plans.setAdapter(plansAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        getMyPlans();
    }

    /**
     * 获取套餐列表
     */
    private void getMyPlans() {
        EasyHttp.post(this).api(new PlanListApi()).request(new HttpCallback<ApiResult<ArrayList<PlanListBean>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<ArrayList<PlanListBean>> result) {
                super.onSucceed(result);
                if (!EmptyUtil.isEmpty(result)) {
                    planListBeans = result.getData();
                    plansAdapter.setNewData(planListBeans);
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }


//    /***
//     * 分配计划
//     * @param packegeID 套餐ID    pakecgeName 套餐名称   UserID 患者ID
//     */
//    private void mealOrderPackege(int packegeID, String UserID) {
//        MealCreateOrderApi orderApi = new MealCreateOrderApi();
//        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, Application.instance());
//        if (loginBean == null) {
//            return;
//        }
//        int docId = loginBean.getAccount().user.userId;
//        orderApi.patientId = UserID;
//        orderApi.doctorId = String.valueOf(docId);
//        orderApi.templateId = String.valueOf(packegeID);
//        orderApi.beginTime = TimeUtils.getCurrenTimeYMDHMS() + " 05:00:00";
//        EasyHttp.post(this).api(orderApi).request(new HttpCallback<ApiResult>(this) {
//            @Override
//            public void onSucceed(ApiResult result) {
//                super.onSucceed(result);
//                hideDialog();
//                if (result.getCode() == 0) {
//                    ToastUtils.show("分配随访计划成功!");
//                    finish();
//                } else {
//                    ToastUtils.show(result.getMessage());
//                }
//            }
//
//            @Override
//            public void onFail(Exception e) {
//                super.onFail(e);
//                hideDialog();
//                ToastUtils.show("分配计划失败");
//            }
//        });
//
//    }


    @OnClick({R.id.rl_back, R.id.tv_addplan, R.id.tv_sortby_time})
    public void OnClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.rl_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0)
                    homeActivity.getSupportFragmentManager().popBackStack();
                break;

//                新增随访
            case R.id.tv_addplan:
//                startActivity(new Intent(this, EditCreatePlanActivity.class));
                break;

//                按创建时间排序
            case R.id.tv_sortby_time:
                showPopChoose(R.layout.pop_sort);
                break;

        }


    }

    private void showPopChoose(int layoutId) {
        mPopupWindow = MPopupWindow.create(homeActivity)
                .setLayoutId(layoutId)
//                .setBackgroundDrawable(new ColorDrawable(Color.GREEN))
                .setAnimationStyle(R.style.AnimDown)
//                .setOutsideTouchable(false)
                .setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (mPopupWindow != null) {
                        }
                    }
                })
                .setViewCallBack(viewCallback)
                .setTarget(tv_sortby_time)
                .setGravity(TypeGravity.BOTTOM_CENTER)
                .build();
        mPopupWindow.show();
    }

    /**
     * 弹窗点击排序功能
     */
    private ViewCallback viewCallback = new ViewCallback() {
        @Override
        public void onInitView(View view, int mLayoutId) {
            switch (mLayoutId) {
                case R.layout.pop_sort:
                    TextView tv_sort_time = view.findViewById(R.id.tv_sort_time);
                    TextView tv_sort_num = view.findViewById(R.id.tv_sort_num);
                    tv_sort_num.setTextColor(isSortTime ? homeActivity.getColor(R.color.white) : homeActivity.getColor(R.color.main_blue));
                    tv_sort_time.setTextColor(isSortTime ? homeActivity.getColor(R.color.main_blue) : homeActivity.getColor(R.color.white));
                    tv_sort_time.setOnClickListener(v -> {
                        isSortTime = true;
                        getMyPlans();
                        tv_sort_num.setTextColor(homeActivity.getColor(R.color.white));
                        tv_sort_time.setTextColor(homeActivity.getColor(R.color.main_blue));
                        tv_sortby_time.setText("按创建时间排序");
                        mPopupWindow.dismiss();
                    });
                    tv_sort_num.setOnClickListener(v -> {
                        isSortTime = false;
                        Collections.sort(planListBeans);
                        plansAdapter.setNewData(planListBeans);
                        tv_sort_num.setTextColor(homeActivity.getColor(R.color.main_blue));
                        tv_sort_time.setTextColor(homeActivity.getColor(R.color.white));
                        tv_sortby_time.setText("按使用人数排序");
                        mPopupWindow.dismiss();
                    });
                    break;
            }
        }
    };


    @Override
    public void onSucceed(Object result) {

    }

    @Override
    public void onFail(Exception e) {

    }


}
