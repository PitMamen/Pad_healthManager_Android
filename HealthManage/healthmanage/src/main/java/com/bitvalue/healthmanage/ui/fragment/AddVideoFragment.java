package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.PaperBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.PaperAdapter;
import com.hjq.base.BaseAdapter;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AddVideoFragment extends AppFragment {
    private HomeActivity homeActivity;
    private SmartRefreshLayout mRefreshLayout;
    private PaperAdapter mAdapter;
    private WrapRecyclerView list_normal;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_paper;
    }

    @Override
    protected void initView() {
        list_normal = (WrapRecyclerView) findViewById(R.id.list_daily);
        homeActivity = (HomeActivity) getActivity();

        initList();
    }

    private void initList() {
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.rl_status_refresh);

        mAdapter = new PaperAdapter(getAttachActivity());
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                toast(mAdapter.getItem(position).title);
//                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
//                    homeActivity.getSupportFragmentManager().popBackStack();
//                }
                //TODO 传数据
                homeActivity.getSupportFragmentManager().popBackStack();
            }
        });
        list_normal.setAdapter(mAdapter);

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
//                    mAdapter.setLastPage(mAdapter.getItemCount() >= 11);
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

        mAdapter.setData(analogData());//TODO 获取数据
    }

    /**
     * 模拟数据
     */
    private List<ArticleBean> analogData() {
        List<ArticleBean> data = new ArrayList<>();
        for (int i = mAdapter.getItemCount(); i < mAdapter.getItemCount() + 10; i++) {
            ArticleBean planBean;
            if (i % 3 == 0) {
                planBean = new ArticleBean("我是第" + i + "条目", 1);
            } else {
                planBean = new ArticleBean("我是第" + i + "条目", 2);
            }
            data.add(planBean);
        }
        return data;
    }

    @Override
    protected void initData() {

    }
}
