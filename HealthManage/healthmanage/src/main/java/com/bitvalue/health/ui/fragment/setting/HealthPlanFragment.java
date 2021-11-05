package com.bitvalue.health.ui.fragment.setting;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.eventbusbean.RefreshPlansObj;
import com.bitvalue.health.api.responsebean.PlanListBean;
import com.bitvalue.health.base.BaseAdapter;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.settingcontract.HealthPlanContract;
import com.bitvalue.health.presenter.settingpresenter.HealthPlanPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.HealthPlanAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.hjq.toast.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 11/03
 */
public class HealthPlanFragment extends BaseFragment<HealthPlanPresenter> implements HealthPlanContract.View {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.list_my_plans)
    WrapRecyclerView list_my_plans;

    @BindView(R.id.rl_status_refresh)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.layout_new_plan)
    RelativeLayout relativeLayout;

    private HealthPlanAdapter mAdapter;
    private HomeActivity homeActivity;
    private ArrayList<PlanListBean> planListBeans = new ArrayList<>();


    @Override
    protected HealthPlanPresenter createPresenter() {
        return new HealthPlanPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_health_plan;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        EventBus.getDefault().register(this);
        tv_title.setText("套餐配置");
        mPresenter.getHealthPlanTempalte();  //获取我的套餐计划
        initList();
    }


    private void initList() {
        mRefreshLayout.setEnableLoadMore(false);//SmartRefreshLayout不自动加载
        mRefreshLayout.setEnableRefresh(false);

        mAdapter = new HealthPlanAdapter(homeActivity);
        mAdapter.setOnItemClickListener((recyclerView, itemView, position) -> {
            homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_MODIFY, planListBeans.get(position));
        });
        list_my_plans.setAdapter(mAdapter);

    }

    @Override
    public void getMyHealthPlanTempalteSuccess(ArrayList<PlanListBean> planListBeanlist) {
        planListBeans = planListBeanlist;
        getActivity().runOnUiThread(() -> mAdapter.setData(planListBeans));

    }

    @Override
    public void getMyHealthPlanTempalteFail(String messageFail) {
        getActivity().runOnUiThread(() -> ToastUtils.show(messageFail));

    }


    @OnClick({R.id.layout_new_plan, R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_new_plan:

                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_NEW, "");

//                NewHealthPlanFragment fragment = new NewHealthPlanFragment();
//                Bundle bundle = new Bundle();
////                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
//                fragment.setArguments(bundle);
//                forward(homeActivity,fragment, true);
                break;

            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }


    /**
     * 处理订阅消息 科普文章
     *
     * @param refreshPlansObj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshPlansObj refreshPlansObj) {
        mPresenter.getHealthPlanTempalte();
    }
}
