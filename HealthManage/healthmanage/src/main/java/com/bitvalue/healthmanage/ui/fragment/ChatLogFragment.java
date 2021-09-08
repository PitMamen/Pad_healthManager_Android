package com.bitvalue.healthmanage.ui.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.ArticleAdapter;
import com.bitvalue.healthmanage.ui.adapter.SearchPatientAdapter;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.base.BaseAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatLogFragment extends AppFragment {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_all)
    TextView tv_all;

    @BindView(R.id.line_all)
    View line_all;

    @BindView(R.id.tv_video)
    TextView tv_video;

    @BindView(R.id.line_video)
    View line_video;

    @BindView(R.id.tv_health)
    TextView tv_health;

    @BindView(R.id.line_health)
    View line_health;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.list_patients)
    RecyclerView list_patients;

    private HomeActivity homeActivity;
    private int tabPosition;
    private SearchPatientAdapter mSearchPatientAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_log;
    }

    @Override
    protected void initView() {
        tv_title.setText("我的看诊记录");
        homeActivity = (HomeActivity) getActivity();
        initSearchButton();
        initSearchList();
        onSelectTab();
    }

    @Override
    protected void initData() {

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
//                    getSearchArticles();
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
//                    layout_daily.setVisibility(View.VISIBLE);
//                    layout_search_result.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void initSearchList() {
        mSearchPatientAdapter = new SearchPatientAdapter(getAttachActivity());
        mSearchPatientAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
            }
        });
        list_patients.setAdapter(mSearchPatientAdapter);

//        TextView headerView = list_my_plans.addHeaderView(R.layout.picker_item);
//        headerView.setText("我是头部");
//        headerView.setOnClickListener(v -> toast("点击了头部"));
//
//        TextView footerView = list_my_plans.addFooterView(R.layout.picker_item);
//        footerView.setText("我是尾部");
//        footerView.setOnClickListener(v -> toast("点击了尾部"));

//        layout_search_result.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
//                postDelayed(() -> {
//                    mSearchAdapter.addData(analogData());//TODO
//                    layout_search_result.finishLoadMore();
//
//                    mSearchAdapter.setLastPage(mSearchAdapter.getItemCount() >= 11);
//                    layout_search_result.setNoMoreData(mSearchAdapter.isLastPage());
//                }, 1000);
//            }
//
//            @Override
//            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
//                postDelayed(() -> {
//                    mSearchAdapter.clearData();
//                    mSearchAdapter.setData(analogData());//TODO
//                    layout_search_result.finishRefresh();
//                }, 1000);
//            }
//        });

    }

    @OnClick({R.id.layout_back, R.id.layout_all, R.id.layout_video, R.id.tv_health})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
            case R.id.layout_all:
                tabPosition = 0;
                onSelectTab();
                break;
            case R.id.layout_video:
                tabPosition = 1;
                onSelectTab();
                break;
            case R.id.tv_health:
                tabPosition = 2;
                onSelectTab();
                break;
        }
    }

    private void onSelectTab() {
        resetAllTab();
        checkTab();
    }

    private void resetAllTab() {
        tv_all.setTextColor(homeActivity.getResources().getColor(R.color.text_black));
        line_all.setBackgroundResource(0);

        tv_video.setTextColor(homeActivity.getResources().getColor(R.color.text_black));
        line_video.setBackgroundResource(0);

        tv_health.setTextColor(homeActivity.getResources().getColor(R.color.text_black));
        line_health.setBackgroundResource(0);
    }

    private void checkTab() {
        switch (tabPosition) {
            case 0:
                tv_all.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                line_all.setBackgroundColor(homeActivity.getResources().getColor(R.color.main_blue));
                break;
            case 1:
                tv_video.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                line_video.setBackgroundColor(homeActivity.getResources().getColor(R.color.main_blue));
                break;
            case 2:
                tv_health.setTextColor(homeActivity.getResources().getColor(R.color.main_blue));
                line_health.setBackgroundColor(homeActivity.getResources().getColor(R.color.main_blue));
                break;
        }

        //TODO 获取数据
    }
}
