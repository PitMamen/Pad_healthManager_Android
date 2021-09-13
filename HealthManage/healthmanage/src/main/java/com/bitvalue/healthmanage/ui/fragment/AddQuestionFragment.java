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

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.GetQuestionApi;
import com.bitvalue.healthmanage.http.response.QuestionResultBean;
import com.bitvalue.healthmanage.http.response.msg.AddQuestionObject;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.QuestionAdapter;
import com.bitvalue.healthmanage.widget.layout.WrapRecyclerView;
import com.bitvalue.healthmanage.widget.tasks.bean.GetMissionObj;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.bitvalue.healthmanage.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class AddQuestionFragment extends AppFragment {

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.tv_question)
    TextView tv_question;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private HomeActivity homeActivity;
    private SmartRefreshLayout mRefreshLayout;
    private QuestionAdapter mAdapter;
    private WrapRecyclerView list_normal;
    private List<QuestionResultBean.ListDTO> questionBeans = new ArrayList<>();
    private GetQuestionApi getVideosApi;
    private int total;
    private AddQuestionObject addVideoObject;
    private GetMissionObj getMissionObj;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_question;
    }

    @Override
    protected void initView() {
        getMissionObj = (GetMissionObj) getArguments().getSerializable(Constants.GET_MISSION_OBJ);

        tv_title.setText("问卷选择");
        list_normal = (WrapRecyclerView) findViewById(R.id.list_daily);
        homeActivity = (HomeActivity) getActivity();

//        addVideoObject = (AddQuestionObject) getArguments().getSerializable(Constants.ADD_VIDEO_DATA);

        initList();
        initSearchButton();
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
                    getVideosApi.keyWord = et_search.getText().toString();
                    getVideosApi.start = 0;
                    mAdapter.clearData();
                    tv_question.setText("搜索问卷");
                    getQuestions();
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
                    getVideosApi.keyWord = "";
                    getVideosApi.start = 0;
                    tv_question.setText("常用问卷");
                    getQuestions();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void initList() {
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.rl_status_refresh);

        mAdapter = new QuestionAdapter(getAttachActivity());
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                QuestionResultBean.ListDTO listDTO = questionBeans.get(position);
//                listDTO.questionFor = addVideoObject.questionFor;
                if (null != getMissionObj) {
                    listDTO.TaskNo = getMissionObj.getTaskNo();
                    listDTO.MissionNo = getMissionObj.getMissionNo();
                }
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
                getQuestions();

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
                getQuestions();
//                postDelayed(() -> {
//                    mAdapter.clearData();
//                    mAdapter.setData(analogData());
//                    mRefreshLayout.finishRefresh();
//                }, 1000);
            }
        });

//        mAdapter.setData(analogData());//TODO 获取数据


    }

    private void getQuestions() {//    /health/doctor/getUserRemind
        EasyHttp.get(this).api(getVideosApi).request(new HttpCallback<HttpData<QuestionResultBean>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<QuestionResultBean> result) {
                super.onSucceed(result);
                //增加判空
                if (result == null || result.getData() == null) {
                    return;
                }
                if (result.getCode() == 0) {
                    if (getVideosApi.start == 1) {//下拉刷新,以及第一次加载
                        questionBeans = result.getData().list;
                        total = result.getData().total;
                        mRefreshLayout.finishRefresh();
                    } else {//加载更多
                        questionBeans.addAll(result.getData().list);
                        mRefreshLayout.finishLoadMore();
                        mAdapter.setLastPage(mAdapter.getItemCount() >= total);
                        mRefreshLayout.setNoMoreData(mAdapter.isLastPage());
                    }
                    mAdapter.setData(questionBeans);

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
        getVideosApi = new GetQuestionApi();
        getVideosApi.pageSize = 10;
        getVideosApi.start = 1;
        getQuestions();
    }
}
