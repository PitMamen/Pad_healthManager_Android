package com.bitvalue.healthmanage.ui.fragment.function.healthmanage;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.ui.adapter.VideoAdapter;
import com.bitvalue.healthmanage.base.AppFragment;
import com.bitvalue.healthmanage.base.BaseAdapter;
import com.bitvalue.healthmanage.http.bean.AddVideoObject;
import com.bitvalue.healthmanage.http.bean.VideoResultBean;
import com.bitvalue.healthmanage.http.model.ApiResult;
import com.bitvalue.healthmanage.http.api.GetVideosApi;
import com.bitvalue.healthmanage.ui.activity.main.HomeActivity;
import com.bitvalue.healthmanage.widget.layout.WrapRecyclerView;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/***
 * 健康消息--添加视频
 */
public class AddVideoFragment extends AppFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.list_daily)
    WrapRecyclerView list_normal;

    private HomeActivity homeActivity;
    private SmartRefreshLayout mRefreshLayout;
    private VideoAdapter mAdapter;
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
        tv_title.setText(getString(R.string.video_selection));
        homeActivity = (HomeActivity) getActivity();

        addVideoObject = (AddVideoObject) getArguments().getSerializable(Constants.ADD_VIDEO_DATA);

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

//    根据关键字搜索视频
    private void getVideos() {//    /health/doctor/getUserRemind
        EasyHttp.get(this).api(getVideosApi).request(new HttpCallback<ApiResult<VideoResultBean>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<VideoResultBean> result) {
                super.onSucceed(result);
                //TODO 展示获取到的信息
                //增加判空
                if (result == null || result.getData() ==null){
                    return;
                }
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

    @Override
    protected void initData() {
        //初始化
        getVideosApi = new GetVideosApi();
        getVideosApi.pageSize = 10;
        getVideosApi.start = 1;
        getVideos();
    }
}
