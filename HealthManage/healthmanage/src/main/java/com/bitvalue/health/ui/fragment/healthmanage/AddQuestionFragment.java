package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.USER_ID;

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

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.settingcontract.AddQuestionContract;
import com.bitvalue.health.presenter.settingpresenter.AddQuestionPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.QuestionAdapter;
import com.bitvalue.health.util.chatUtil.CustomWenJuanMessage;
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


/***
 * 添加问卷调查 界面
 */
public class AddQuestionFragment extends BaseFragment<AddQuestionPresenter> implements AddQuestionContract.View {

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.tv_question)
    TextView tv_question;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rl_status_refresh)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.list_daily)
    WrapRecyclerView list_normal;

    @BindView(R.id.layout_back)
    LinearLayout back;

    private HomeActivity homeActivity;
    private QuestionAdapter mAdapter;
    private List<QuestionResultBean.ListDTO> questionBeans = new ArrayList<>();
    private int total;

    private int cureentPage = 0;
    private int pageSize = 10;
    private int start = 1;
    private String keyWord = "";
    private String user_id;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    public void initView(View view) {
        tv_title.setText(getString(R.string.questionnaire_selection));
        back.setVisibility(View.VISIBLE);
        user_id = getArguments().getString(USER_ID, "");
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
        et_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (et_search.getText().toString().isEmpty()) {
                    ToastUtil.toastShortMessage("请输入搜索内容");
                    return true;
                }

                //关闭软键盘
                hideKeyboard(et_search);
                keyWord = et_search.getText().toString();
                start = 0;
                mAdapter.clearData();
                tv_question.setText("搜索问卷");
                getQuestions();
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
                    keyWord = "";
                    start = 0;
                    tv_question.setText("常用问卷");
                    getQuestions();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    private void sendQuestion(QuestionResultBean.ListDTO bean) {
        Log.e(TAG, "发送问卷---------");
        CustomWenJuanMessage questionMessage = new CustomWenJuanMessage();
        questionMessage.name = bean.name;
        questionMessage.url = bean.questUrl;     //这里url后面加 字段 用语隐藏 提交按钮  ?showsubmitbtn=hide
        questionMessage.id = bean.id;
        questionMessage.userId = user_id;
        questionMessage.setType("CustomWenJuanMessage");
        questionMessage.setDescription("问卷");
        EventBus.getDefault().post(questionMessage);
    }

    private void initList() {
        mAdapter = new QuestionAdapter(getActivity());
        mAdapter.setOnItemClickListener((recyclerView, itemView, position) -> {
            sendQuestion(questionBeans.get(position));
            homeActivity.getSupportFragmentManager().popBackStack();
        });
        list_normal.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            //上拉
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {

                // TODO: 2021/12/8 加载下一页
                Log.e(TAG, "上拉 当前页: " + cureentPage + "   开始页: " + start);
                if (cureentPage == start) {
                    start = 1;
                    Log.e(TAG, "无更多数据");
                    mRefreshLayout.finishLoadMore();
                    mRefreshLayout.finishRefresh();
                    return;
                }
                start++;
                getQuestions();
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();

            }

            //下拉
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                Log.e(TAG, "下拉 当前页: " + cureentPage + "   开始页: " + start);
                if (start > 1) {
                    start--;
                } else {
                    mRefreshLayout.finishRefresh();
                    return;
                }
                getQuestions();
                mRefreshLayout.finishRefresh();
            }
        });


    }


    //请求接口获取问卷调查
    private void getQuestions() {
        mPresenter.qryQuestByKeyWord(pageSize, start, keyWord);
    }


    @Override
    public void initData() {
        //初始化
        getQuestions();
    }

    @Override
    protected AddQuestionPresenter createPresenter() {
        return new AddQuestionPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_add_question;
    }

    @Override
    public void qryQuestByKeyWordSuccess(QuestionResultBean questionResultBean) {
        getActivity().runOnUiThread(() -> {
            if (start == 1) {//下拉刷新,以及第一次加载
                questionBeans = questionResultBean.list;
                total = questionResultBean.total;
                mRefreshLayout.finishRefresh();
            } else {//加载更多
                if (null != questionResultBean.list && questionResultBean.list.size() == 0) {
                    cureentPage = start;
                }
                questionBeans.addAll(questionResultBean.list);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.setNoMoreData(mAdapter.isLastPage());
            }
            mAdapter.setData(questionBeans);
        });

    }

    @Override
    public void qryQuestByKeyWordFail(String faileMessage) {
        getActivity().runOnUiThread(() -> ToastUtil.toastShortMessage("获取问卷列表失败!"));
    }
}
