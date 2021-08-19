package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.GetCustomMsgApi;
import com.bitvalue.healthmanage.http.request.GetVideosApi;
import com.bitvalue.healthmanage.http.request.SaveTotalMsgApi;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.PaperBean;
import com.bitvalue.healthmanage.http.response.VideoResultBean;
import com.bitvalue.healthmanage.http.response.msg.AddVideoObject;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.PaperAdapter;
import com.bitvalue.healthmanage.ui.adapter.VideoAdapter;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.layout.WrapRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class AddVideoFragment extends AppFragment {
    private HomeActivity homeActivity;
    private SmartRefreshLayout mRefreshLayout;
    private VideoAdapter mAdapter;
    private WrapRecyclerView list_normal;
    private List<VideoResultBean.ListDTO> videoBeans = new ArrayList<>();
    private GetVideosApi getVideosApi;
    private int total;
    private AddVideoObject addVideoObject;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_video;
    }

    @Override
    protected void initView() {
        list_normal = (WrapRecyclerView) findViewById(R.id.list_daily);
        homeActivity = (HomeActivity) getActivity();

        addVideoObject = (AddVideoObject) getArguments().getSerializable(Constants.ADD_VIDEO_DATA);

        initList();
    }

    private void initList() {
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.rl_status_refresh);

        mAdapter = new VideoAdapter(getAttachActivity());
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                VideoResultBean.ListDTO listDTO = videoBeans.get(position);
                listDTO.videoFor = addVideoObject.videoFor;
                EventBus.getDefault().post(listDTO);
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
                getVideosApi.start = mAdapter.getCount() + 1;
                getVideos();

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
                getVideosApi.start = 1;
                mAdapter.clearData();
                getVideos();
//                postDelayed(() -> {
//                    mAdapter.clearData();
//                    mAdapter.setData(analogData());
//                    mRefreshLayout.finishRefresh();
//                }, 1000);
            }
        });

//        mAdapter.setData(analogData());//TODO 获取数据


    }

    private void getVideos() {//    /health/doctor/getUserRemind
        EasyHttp.get(this).api(getVideosApi).request(new HttpCallback<HttpData<VideoResultBean>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<VideoResultBean> result) {
                super.onSucceed(result);
                //TODO 展示获取到的信息
                if (result.getCode() == 0) {
                    if (getVideosApi.start == 1) {//下拉刷新,以及第一次加载
                        videoBeans = result.getData().list;
                        total = result.getData().total;
                        mRefreshLayout.finishRefresh();
                    } else {//加载更多
                        videoBeans.addAll(result.getData().list);
                        mRefreshLayout.finishLoadMore();
                        mAdapter.setLastPage(mAdapter.getItemCount() >= total);
                        mRefreshLayout.setNoMoreData(mAdapter.isLastPage());
                    }
                    mAdapter.setData(videoBeans);

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

//    /**
//     * 模拟数据
//     */
//    private List<ArticleBean> analogData() {
//        List<ArticleBean> data = new ArrayList<>();
//        for (int i = mAdapter.getItemCount(); i < mAdapter.getItemCount() + 10; i++) {
//            ArticleBean planBean;
//            if (i % 3 == 0) {
//                planBean = new ArticleBean("我是第" + i + "条目", 1);
//            } else {
//                planBean = new ArticleBean("我是第" + i + "条目", 2);
//            }
//            data.add(planBean);
//        }
//        return data;
//    }

    @Override
    protected void initData() {
        //初始化
        getVideosApi = new GetVideosApi();
        getVideosApi.pageSize = 10;
        getVideosApi.start = 1;
        getVideos();
    }
}
