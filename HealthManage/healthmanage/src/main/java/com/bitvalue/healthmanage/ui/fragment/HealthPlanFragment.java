package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.PlanListApi;
import com.bitvalue.healthmanage.http.response.PlanListBean;
import com.bitvalue.healthmanage.http.response.RefreshPlansObj;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.HealthPlanAdapter;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.OnClick;
import okhttp3.Call;

public class HealthPlanFragment extends AppFragment {

    private WrapRecyclerView list_my_plans;
    private SmartRefreshLayout mRefreshLayout;
    private HealthPlanAdapter mAdapter;
    private HomeActivity homeActivity;
    private ArrayList<PlanListBean> planListBeans = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_health_plan;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
//        setOnClickListener(R.id.layout_new_plan);
        list_my_plans = (WrapRecyclerView) findViewById(R.id.list_my_plans);
        homeActivity = (HomeActivity) getActivity();
        initList();
        getMyPlans();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initList() {
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.rl_status_refresh);

        mAdapter = new HealthPlanAdapter(getAttachActivity());
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                //TODO 修改计划
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_MODIFY,planListBeans.get(position));
                turnData(position);
            }
        });
        list_my_plans.setAdapter(mAdapter);

//        TextView headerView = list_my_plans.addHeaderView(R.layout.picker_item);
//        headerView.setText("我是头部");
//        headerView.setOnClickListener(v -> toast("点击了头部"));
//
//        TextView footerView = list_my_plans.addFooterView(R.layout.picker_item);
//        footerView.setText("我是尾部");
//        footerView.setOnClickListener(v -> toast("点击了尾部"));

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
//                postDelayed(() -> {
//                    mAdapter.addData(analogData());
//                    mRefreshLayout.finishLoadMore();
//
//                    mAdapter.setLastPage(mAdapter.getItemCount() >= 100);
//                    mRefreshLayout.setNoMoreData(mAdapter.isLastPage());
//                }, 1000);
            }

            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
//                postDelayed(() -> {
//                    mAdapter.clearData();
//                    mAdapter.setData(analogData());
//                    mRefreshLayout.finishRefresh();
//                }, 1000);
            }
        });

        mAdapter.setData(planListBeans);
    }

    private void turnData(int position) {
        if (planListBeans.get(position).status.equals("1")){
            planListBeans.get(position).status = "0";
        }else {
            planListBeans.get(position).status = "1";
        }

        mAdapter.setData(planListBeans);
    }

    private void getMyPlans() {
        EasyHttp.post(this).api(new PlanListApi()).request(new HttpCallback<HttpData<ArrayList<PlanListBean>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<ArrayList<PlanListBean>> result) {
                super.onSucceed(result);
                planListBeans = result.getData();
                mAdapter.setData(planListBeans);
//                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    /**
     * 处理订阅消息 科普文章
     *
     * @param refreshPlansObj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshPlansObj refreshPlansObj) {
        getMyPlans();
    }

    /**
     * 模拟数据
     */
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
    @Override
    protected void initData() {

    }

    @OnClick({R.id.layout_new_plan})
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
        }
    }
}
