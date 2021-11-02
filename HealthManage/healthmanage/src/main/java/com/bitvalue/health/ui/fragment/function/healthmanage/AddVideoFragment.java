package com.bitvalue.health.ui.fragment.function.healthmanage;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.requestbean.GetVideosApi;
import com.bitvalue.health.api.responsebean.VideoResultBean;
import com.bitvalue.health.api.responsebean.message.AddVideoObject;
import com.bitvalue.health.base.BaseAdapter;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.VideoAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
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

public class AddVideoFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rl_status_refresh)
    SmartRefreshLayout mRefreshLayout;
    private HomeActivity homeActivity;
    private VideoAdapter mAdapter;
    private WrapRecyclerView list_normal;
    private List<VideoResultBean.ListDTO> videoBeans = new ArrayList<>();
    private GetVideosApi getVideosApi;
    private int total;
    private AddVideoObject addVideoObject;


    @Override
    public void initView(View rootView) {
        tv_title.setText("视频选择");
        list_normal = rootView.findViewById(R.id.list_daily);
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
        mAdapter = new VideoAdapter(getActivity());
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

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                getVideosApi.start = mAdapter.getCount() + 1;
                getVideos();
            }

            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                getVideosApi.start = 1;
                mAdapter.clearData();
                getVideos();
            }
        });
    }

    private void getVideos() {
        EasyHttp.get(this).api(getVideosApi).request(new HttpCallback<ApiResult<VideoResultBean>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<VideoResultBean> result) {
                super.onSucceed(result);
                Log.e(TAG, "add  video onSucceed: "+result.toString() );
                //增加判空
                if (result == null || result.getData() == null) {
                    return;
                }
                if (result.getData().list.size()==0){
                    ToastUtil.toastShortMessage("未获取到视频");
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
    public void initData() {
        //初始化
        getVideosApi = new GetVideosApi();
        getVideosApi.pageSize = 10;
        getVideosApi.start = 1;
        getVideos();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_add_video;
    }
}
