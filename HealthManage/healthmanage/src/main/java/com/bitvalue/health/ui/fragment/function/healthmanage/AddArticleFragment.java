package com.bitvalue.health.ui.fragment.function.healthmanage;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.requestbean.GetArticleApi;
import com.bitvalue.health.api.requestbean.SearchArticleApi;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.SearchArticleResult;
import com.bitvalue.health.api.responsebean.message.GetMissionObj;
import com.bitvalue.health.base.BaseAdapter;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.ArticleAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
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


/****
 * 添加文章界面
 */
public class AddArticleFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.layout_daily)
    LinearLayout layout_daily;

    @BindView(R.id.layout_search_result)
    SmartRefreshLayout layout_search_result;

    @BindView(R.id.list_search)
    RecyclerView list_search;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.list_daily)
    WrapRecyclerView list_daily;

    @BindView(R.id.rl_status_refresh)
    SmartRefreshLayout mRefreshLayout;

    private HomeActivity homeActivity;


    private ArticleAdapter mDailyAdapter;
    private ArticleAdapter mSearchAdapter;

    private List<ArticleBean> dailyArticles = new ArrayList<>();
    private List<ArticleBean> searchArticles = new ArrayList<>();
    private GetMissionObj getMissionObj;


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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    public void initView(View rootView) {
        tv_title.setText("文章选择");
        getMissionObj = (GetMissionObj) getArguments().getSerializable(Constants.GET_MISSION_OBJ);

        initSearchButton();
        initDailyList();
        initSearchList();
    }

    private void initSearchButton() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (et_search.getText().toString().isEmpty()) {

                        ToastUtil.toastShortMessage("请输入搜索内容");
                        return true;
                    }

                    //关闭软键盘
                    hideKeyboard(et_search);
                    getSearchArticles();
                    return true;
                }
                return false;
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    layout_daily.setVisibility(View.VISIBLE);
                    layout_search_result.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void initDailyList() {
        mDailyAdapter = new ArticleAdapter(getActivity());
        mDailyAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                if (dailyArticles.size() == 0) {
                    return;
                }
                ArticleBean articleBean = dailyArticles.get(position);
                if (null != getMissionObj) {
                    articleBean.TaskNo = getMissionObj.getTaskNo();
                    articleBean.MissionNo = getMissionObj.getMissionNo();
                }
                EventBus.getDefault().post(articleBean);
                homeActivity.getSupportFragmentManager().popBackStack();
            }
        });
        list_daily.setAdapter(mDailyAdapter);

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {

//                postDelayed(() -> {
//                    mDailyAdapter.addData(analogData());
//                    mRefreshLayout.finishLoadMore();
//
//                    mDailyAdapter.setLastPage(mDailyAdapter.getItemCount() >= 5);
//                    mRefreshLayout.setNoMoreData(mDailyAdapter.isLastPage());
//                }, 1000);
            }

            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
//                postDelayed(() -> {
//                    mDailyAdapter.clearData();
//                    mDailyAdapter.setData(analogData());
//                    mRefreshLayout.finishRefresh();
//                }, 1000);
            }
        });

        getDailyArticles();
    }

    private void initSearchList() {

        mSearchAdapter = new ArticleAdapter(getActivity());
        mSearchAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                if (searchArticles.size() == 0) {
                    return;
                }
                EventBus.getDefault().post(searchArticles.get(position));
                homeActivity.getSupportFragmentManager().popBackStack();
            }
        });
        list_search.setAdapter(mSearchAdapter);

//        TextView headerView = list_my_plans.addHeaderView(R.layout.picker_item);
//        headerView.setText("我是头部");
//        headerView.setOnClickListener(v -> toast("点击了头部"));
//
//        TextView footerView = list_my_plans.addFooterView(R.layout.picker_item);
//        footerView.setText("我是尾部");
//        footerView.setOnClickListener(v -> toast("点击了尾部"));

        layout_search_result.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
//                postDelayed(() -> {
//                    mSearchAdapter.addData(analogData());//TODO
//                    layout_search_result.finishLoadMore();
//
//                    mSearchAdapter.setLastPage(mSearchAdapter.getItemCount() >= 11);
//                    layout_search_result.setNoMoreData(mSearchAdapter.isLastPage());
//                }, 1000);
            }

            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
//                postDelayed(() -> {
//                    mSearchAdapter.clearData();
//                    mSearchAdapter.setData(analogData());//TODO
//                    layout_search_result.finishRefresh();
//                }, 1000);
            }
        });

    }

    private void getDailyArticles() {
        GetArticleApi getArticleApi = new GetArticleApi();
        getArticleApi.articleNum = 5;
        EasyHttp.get(this).api(getArticleApi).request(new HttpCallback<ApiResult<ArrayList<ArticleBean>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<ArrayList<ArticleBean>> result) {
                super.onSucceed(result);
                dailyArticles = result.getData();
                mDailyAdapter.setData(dailyArticles);
                if (null == dailyArticles || dailyArticles.size() == 0) {
                    layout_daily.setVisibility(View.GONE);
                } else {
                    layout_daily.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });


    }

    private void getSearchArticles() {
        String title = et_search.getText().toString();
        if (title.isEmpty()) {
            return;
        }
        SearchArticleApi searchArticleApi = new SearchArticleApi();
        searchArticleApi.title = title;
        EasyHttp.get(this).api(searchArticleApi).request(new HttpCallback<ApiResult<SearchArticleResult>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<SearchArticleResult> result) {
                super.onSucceed(result);
                //添加判空
                if (result != null &&  result.getData() !=null){
                    searchArticles = result.getData().list;
                    if (null == searchArticles || searchArticles.size() == 0) {
                        ToastUtil.toastShortMessage("未查询到结果");
                        layout_daily.setVisibility(View.VISIBLE);
                        layout_search_result.setVisibility(View.GONE);
                    } else {
                        mSearchAdapter.setData(searchArticles);
                        layout_daily.setVisibility(View.GONE);
                        layout_search_result.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_add_article;
    }
}
