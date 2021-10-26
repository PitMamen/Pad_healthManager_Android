package com.bitvalue.healthmanage.ui.fragment.function.setting;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.base.AppFragment;
import com.bitvalue.healthmanage.http.model.ApiResult;
import com.bitvalue.healthmanage.http.api.ChatLogApi;
import com.bitvalue.healthmanage.http.bean.PatientResultBean;
import com.bitvalue.healthmanage.ui.activity.main.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.SearchPatientAdapter;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.bitvalue.healthmanage.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


/***
 * 个人设置 我的看诊记录界面
 */
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

    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    private HomeActivity homeActivity;
    private int tabPosition;
    private SearchPatientAdapter mSearchPatientAdapter;
    private ChatLogApi chatLogApi;
    private List<PatientResultBean> patientResultBeans = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_log;
    }

    @Override
    protected void initView() {
        tv_title.setText(getString(R.string.my_visit_record));
        homeActivity = (HomeActivity) getActivity();
        initSearchButton();
        initSearchList();
        chatLogApi = new ChatLogApi();
        onSelectTab();
    }

    @Override
    protected void initData() {
    }

    private void getLogs() {
        EasyHttp.get(this).api(chatLogApi).request(new HttpCallback<ApiResult<List<PatientResultBean>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<List<PatientResultBean>> result) {
                super.onSucceed(result);
//                //增加判空
                if (result == null || null == result.getData()) {
                    return;
                }
                if (result.getCode() == 0) {
                    if (result.getData().size() > 0) {
                        list_patients.setVisibility(View.VISIBLE);
                        tv_no_data.setVisibility(View.GONE);
                        patientResultBeans.clear();
                        patientResultBeans = result.getData();
                        mSearchPatientAdapter.setData(patientResultBeans);
                        mSearchPatientAdapter.notifyDataSetChanged();
                    } else {
                        list_patients.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.VISIBLE);
                    }
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

    private void initSearchButton() {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (et_search.getText().toString().isEmpty()) {
                        chatLogApi.keyWord = "";
                        ToastUtil.toastShortMessage("请输入搜索内容");
                        return true;
                    }

                    //关闭软键盘
                    hideKeyboard(et_search);
                    chatLogApi.keyWord = et_search.getText().toString();
                    getLogs();
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
                    chatLogApi.keyWord = "";
                    getLogs();
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

    }

    @OnClick({R.id.layout_back, R.id.layout_all, R.id.layout_video, R.id.tv_health, R.id.tv_no_data})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
            case R.id.layout_all:
                tabPosition = 0;
                chatLogApi.type = "";
                onSelectTab();
                break;
            case R.id.layout_video:
                tabPosition = 1;
                chatLogApi.type = "2";
                onSelectTab();
                break;
            case R.id.tv_health:
                tabPosition = 2;
                chatLogApi.type = "1";
                onSelectTab();

            case R.id.tv_no_data:
                getLogs();
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

        getLogs();
    }
}
