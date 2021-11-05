package com.bitvalue.health.ui.fragment.setting;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bitvalue.health.api.responsebean.PatientResultBean;
import com.bitvalue.health.base.BaseAdapter;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.settingcontract.MedicalRecordsContract;
import com.bitvalue.health.presenter.settingpresenter.MedicalRecordsPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.SearchPatientAdapter;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author created by bitvalue
 * @data : 11/03
 * <p>
 * 个人设置我的看诊记录界面
 */
public class MedicalRecordsFragment extends BaseFragment<MedicalRecordsPresenter> implements MedicalRecordsContract.View {
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

    private String type = "";
    private String keyWord = "";
    private int tabPosition = 0;
    private SearchPatientAdapter mSearchPatientAdapter;
    private List<PatientResultBean> patientResultBeans = new ArrayList<>();


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        tv_title.setText("我的看诊记录");
        initSearchButton();
        initListView();
        onSelectTab();
    }

    private void initListView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity);
        mSearchPatientAdapter = new SearchPatientAdapter(homeActivity);
        list_patients.setLayoutManager(linearLayoutManager);
        list_patients.setAdapter(mSearchPatientAdapter);
    }


    private void getLogs() {
        showLoading();
        mPresenter.qryMyMedicalRecords(type, keyWord);
    }

    private void initSearchButton() {
        et_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (et_search.getText().toString().isEmpty()) {
                    keyWord = "";
                    ToastUtil.toastShortMessage("请输入搜索内容");
                    return true;
                }

                //关闭软键盘
                hideKeyboard(et_search);
                keyWord = et_search.getText().toString();
                getLogs();
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
                    getLogs();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
                type = "";
                onSelectTab();
                break;
            case R.id.layout_video:
                tabPosition = 1;
                type = "2";
                onSelectTab();
                break;
            case R.id.tv_health:
                tabPosition = 2;
                type = "1";
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


    @Override
    protected MedicalRecordsPresenter createPresenter() {
        return new MedicalRecordsPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_chat_log;
    }

    @Override
    public void qryMyMedicalRecordsSuccess(ArrayList<PatientResultBean> beanArrayList) {

        getActivity().runOnUiThread(() -> {
            hideDialog();
            if (null == beanArrayList && beanArrayList.size() == 0) {
                return;
            }
            patientResultBeans.clear();
            patientResultBeans = beanArrayList;
            list_patients.setVisibility(patientResultBeans.size() > 0 ? View.VISIBLE : View.GONE);
            tv_no_data.setVisibility(patientResultBeans.size() > 0 ? View.GONE : View.VISIBLE);
            mSearchPatientAdapter.setData(patientResultBeans);
            mSearchPatientAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void qryMyMedicalRecordsFail(String failMessage) {
        getActivity().runOnUiThread(() -> {
            hideDialog();
            ToastUtil.toastShortMessage(failMessage);
        });

    }
}
