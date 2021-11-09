package com.bitvalue.health.ui.fragment.healthmanage;

import static com.hjq.http.EasyUtils.postDelayed;

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
import com.bitvalue.health.api.responsebean.message.GetMissionObj;
import com.bitvalue.health.base.BaseAdapter;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.healthmanagercontract.AddArticleContract;
import com.bitvalue.health.presenter.healthmanager.AddArticlePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.ArticleAdapter;
import com.bitvalue.health.util.Constants;
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

    private HomeActivity homeActivity;


    private ArticleAdapter mDailyAdapter;
    private ArticleAdapter mSearchAdapter;

    private List<ArticleBean> dailyArticles = new ArrayList<>();
    private List<ArticleBean> searchArticles = new ArrayList<>();
    private GetMissionObj getMissionObj;
    private int UsefulArticleCount = 10;

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
        getMissionObj = (GetMissionObj) getArguments().getSerializable(Constants.GET_MISSION_OBJ);

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
            ArticleBean articleBean = dailyArticles.get(position);
            if (null != getMissionObj) {
                articleBean.TaskNo = getMissionObj.getTaskNo();
                articleBean.MissionNo = getMissionObj.getMissionNo();
            }
            EventBus.getDefault().post(articleBean);
            homeActivity.getSupportFragmentManager().popBackStack();
        });
        list_daily.setAdapter(mDailyAdapter);

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                Log.e(TAG, "11111111111111111 " );
            }

            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                mPresenter.getUsefulArticle(UsefulArticleCount++);
                UsefulArticleCount = 10;
                refreshLayout.finishRefresh(500);
            }
        });

        getDailyArticles();
    }


    //初始化搜索控件
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


        layout_search_result.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                Log.e(TAG, "11111111111111111 " );
            }

            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                Log.e(TAG, "onRefresh---------");
                mPresenter.getUsefulArticle(UsefulArticleCount++);
                UsefulArticleCount = 10;
                refreshLayout.finishRefresh();
            }
        });

    }


    //接口请求 加载文章列表
    private void getDailyArticles() {
        mPresenter.getUsefulArticle(UsefulArticleCount);
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
    public void getArticleSuccess(ArrayList<ArticleBean> articleBeanArrayList) {
        getActivity().runOnUiThread(() -> {
            if (dailyArticles.size()==articleBeanArrayList.size()){
                ToastUtil.toastShortMessage("无更多数据");
            }
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
        getActivity().runOnUiThread(() -> ToastUtil.toastShortMessage(messageFail));
    }
}
