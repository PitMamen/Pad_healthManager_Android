package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.GetPlanDetailApi;
import com.bitvalue.healthmanage.http.request.SearchArticleApi;
import com.bitvalue.healthmanage.http.response.PlanBean;
import com.bitvalue.healthmanage.http.response.PlanDetailResult;
import com.bitvalue.healthmanage.http.response.SearchArticleResult;
import com.bitvalue.healthmanage.ui.adapter.HealthPlanAdapter;
import com.bitvalue.healthmanage.ui.adapter.HealthPlanDetailAdapter;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class HealthPlanDetailFragment extends AppFragment {

    @BindView(R.id.tv_plan_name)
    TextView tv_plan_name;

    @BindView(R.id.tv_join_time)
    TextView tv_join_time;

//    private SmartRefreshLayout mRefreshLayout;
    private HealthPlanDetailAdapter mAdapter;
    private WrapRecyclerView list_health_plan;
    private ArrayList<String> mIds;
    private String planId;
    private PlanDetailResult planDetailResult;
    private List<PlanDetailResult.UserPlanDetailsDTO> userPlanDetails;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_health_plan_detail;
    }

    @Override
    protected void initView() {
        mIds = getArguments().getStringArrayList(Constants.MSG_IDS);
        planId = getArguments().getString(Constants.PLAN_ID);
        list_health_plan = (WrapRecyclerView) findViewById(R.id.list_health_plan);
        initList();
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

            }
        });
        list_health_plan.setAdapter(mAdapter);

//        TextView headerView = list_my_plans.addHeaderView(R.layout.picker_item);
//        headerView.setText("我是头部");
//        headerView.setOnClickListener(v -> toast("点击了头部"));
//
//        TextView footerView = list_my_plans.addFooterView(R.layout.picker_item);
//        footerView.setText("我是尾部");
//        footerView.setOnClickListener(v -> toast("点击了尾部"));

//        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
////                postDelayed(() -> {
////                    mAdapter.addData(analogData());
////                    mRefreshLayout.finishLoadMore();
////
////                    mAdapter.setLastPage(mAdapter.getItemCount() >= 100);
////                    mRefreshLayout.setNoMoreData(mAdapter.isLastPage());
////                }, 1000);
//            }
//
//            @Override
//            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
////                postDelayed(() -> {
////                    mAdapter.clearData();
////                    mAdapter.setData(analogData());
////                    mRefreshLayout.finishRefresh();
////                }, 1000);
//            }
//        });

        mAdapter.setData(userPlanDetails);//TODO 获取数据

        getPlanData();
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
