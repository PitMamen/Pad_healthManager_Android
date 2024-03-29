package com.bitvalue.health.ui.fragment.healthmanage;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.SearchArticleResult;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.healthmanagercontract.AddArticleContract;
import com.bitvalue.health.presenter.healthmanager.AddArticlePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.ArticleAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.chatUtil.CustomCaseHistoryMessage;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/****
 * 添加文章界面
 */
public class AddArticleFragment extends BaseFragment<AddArticlePresenter> implements AddArticleContract.View {
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

    @BindView(R.id.layout_back)
    LinearLayout back;

    private HomeActivity homeActivity;


    private ArticleAdapter mDailyAdapter;
    private ArticleAdapter mSearchAdapter;

    private List<ArticleBean> dailyArticles = new ArrayList<>();
    private List<ArticleBean> searchArticles = new ArrayList<>();
    private static final int pageSize = 10;
    private int startPage = 1;
    private int currentPage = 0;

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
        tv_title.setText(getString(R.string.article_select));
        back.setVisibility(View.VISIBLE);
        initSearchButton();
        initDailyList();
        initSearchList();
    }

    //初始化搜索控件 并 设置监听
    private void initSearchButton() {
        et_search.setOnEditorActionListener((v, actionId, event) -> {
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


    //初始化 文章 及 加载更多控件
    private void initDailyList() {
        mDailyAdapter = new ArticleAdapter(getActivity());
        mDailyAdapter.setOnItemClickListener((recyclerView, itemView, position) -> {
            if (dailyArticles.size() == 0) {
                return;
            }
            sendArticle(dailyArticles.get(position));
            homeActivity.getSupportFragmentManager().popBackStack();
        });
        list_daily.setAdapter(mDailyAdapter);

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                Log.e(TAG, "11111111111111111 ");
                // TODO: 2021/12/8 加载下一页
                Log.e(TAG, "onLoadMore111: " + startPage + " currentPage: " + currentPage);
                if (currentPage == startPage) {
                    startPage = 1;
                    Log.e(TAG, "无更多数据");
                    mRefreshLayout.finishLoadMore();
                    mRefreshLayout.finishRefresh();
                    return;
                }
                startPage++;
                getDailyArticles();
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
            }

            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                Log.e(TAG, "onLoadMore222: " + startPage);
                if (startPage > 1) {
                    startPage--;
                } else {
                    mRefreshLayout.finishRefresh();
                    return;
                }

                getDailyArticles();
                mRefreshLayout.finishRefresh();
            }
        });

        getDailyArticles();
    }


    //初始化搜索控件
    private void initSearchList() {

        mSearchAdapter = new ArticleAdapter(getActivity());
        mSearchAdapter.setOnItemClickListener((recyclerView, itemView, position) -> {
            if (searchArticles.size() == 0) {
                return;
            }
            sendArticle(searchArticles.get(position));
            homeActivity.getSupportFragmentManager().popBackStack();
        });
        list_search.setAdapter(mSearchAdapter);


        layout_search_result.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                Log.e(TAG, "11111111111111111 ");
            }

            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                Log.e(TAG, "onRefresh---------");
                refreshLayout.finishRefresh();
            }
        });

    }

    private void sendArticle(ArticleBean articleBean) {
        CustomCaseHistoryMessage message = new CustomCaseHistoryMessage();
        message.title = "文章内容";
        message.content = articleBean.title;
        message.id = articleBean.articleId;
        message.url = articleBean.previewUrl;
        message.setType("CustomArticleMessage");
        message.setDescription("文章");
        EventBus.getDefault().post(message);
    }

    //接口请求 加载文章列表
    private void getDailyArticles() {
        showLoading();
        mPresenter.getUsefulArticle(pageSize,startPage,Constants.DEPT_CODE);
    }


    //根据关键字 查询相关文章(线上获取)
    private void getSearchArticles() {
        String title = et_search.getText().toString();
        if (title.isEmpty()) {
            ToastUtil.toastShortMessage("请输入关键字以查询");
            return;
        }
        mPresenter.qryArticleByTitle(10, 1, title);
    }


    @Override
    protected AddArticlePresenter createPresenter() {
        return new AddArticlePresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_add_article;
    }

    @Override
    public void getArticleSuccess(List<ArticleBean> articleBeanArrayList) {
        getActivity().runOnUiThread(() -> {
            hideDialog();
            if (startPage > 1 && articleBeanArrayList.size() == 0) {
                return;
            }
//            if (dailyArticles.size() == articleBeanArrayList.size()) {
//                ToastUtil.toastShortMessage("无更多数据");
//            }
            dailyArticles.clear();
            dailyArticles = articleBeanArrayList;
            mDailyAdapter.setData(dailyArticles);
            layout_daily.setVisibility(null == dailyArticles || dailyArticles.size() == 0 ? View.GONE : View.VISIBLE);

        });

    }

    @Override
    public void qryArticleByTitleSuccess(SearchArticleResult searchArticleResult) {
        getActivity().runOnUiThread(() -> {
            searchArticles = searchArticleResult.list;
            if (null == searchArticles || searchArticles.size() == 0) {
                ToastUtil.toastShortMessage("未查询到结果");
                layout_daily.setVisibility(View.VISIBLE);
                layout_search_result.setVisibility(View.GONE);
            } else {
                mSearchAdapter.setData(searchArticles);
                layout_daily.setVisibility(View.GONE);
                layout_search_result.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void getArticleFaile(String messageFail) {
        getActivity().runOnUiThread(() -> {
            hideDialog();
            ToastUtil.toastShortMessage("获取文章列表失败!");
        });
    }
}
