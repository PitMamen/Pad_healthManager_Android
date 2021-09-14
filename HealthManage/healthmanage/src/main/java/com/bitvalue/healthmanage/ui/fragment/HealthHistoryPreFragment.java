package com.bitvalue.healthmanage.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.GetCaseApi;
import com.bitvalue.healthmanage.http.request.GetPlanDetailApi;
import com.bitvalue.healthmanage.http.request.SaveCaseApi;
import com.bitvalue.healthmanage.http.response.LoginBean;
import com.bitvalue.healthmanage.http.response.PlanDetailResult;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.util.SharedPreManager;
import com.bitvalue.healthmanage.util.TimeUtils;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class HealthHistoryPreFragment extends AppFragment {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_type)
    TextView tv_type;

    @BindView(R.id.tv_main_detail)
    TextView tv_main_detail;

    @BindView(R.id.tv_history_detail)
    TextView tv_history_detail;

    @BindView(R.id.tv_history_before_detail)
    TextView tv_history_before_detail;

    @BindView(R.id.tv_body_detail)
    TextView tv_body_detail;

    @BindView(R.id.tv_result_detail)
    TextView tv_result_detail;

    @BindView(R.id.tv_conclusion_detail)
    TextView tv_conclusion_detail;

    @BindView(R.id.tv_doc_name_detail)
    TextView tv_doc_name_detail;

    private HomeActivity homeActivity;
    private ChatFragment.NewMsgData newMsgData;
    private GetCaseApi mSaveCaseApi;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_health_history_preview;
    }

    @Override
    protected void initView() {
        tv_title.setText("就诊病历");

        newMsgData = (ChatFragment.NewMsgData) getArguments().getSerializable(Constants.DATA_MSG);

        homeActivity = (HomeActivity) getActivity();
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

    @Override
    protected void initData() {
        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
        tv_type.setText(loginBean.getUser().user.departmentName);
        tv_doc_name_detail.setText(loginBean.getUser().user.userName);

        if (null != newMsgData && newMsgData.saveCaseApi == null) {
            mSaveCaseApi = new GetCaseApi();
            mSaveCaseApi.id = Integer.parseInt(newMsgData.id);
            mSaveCaseApi.appointmentId = newMsgData.appointmentId;
            getCaseData();
        } else {
            tv_time.setText(TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.YY_MM_DD_FORMAT_3));

            tv_main_detail.setText(newMsgData.saveCaseApi.chiefComplaint);
            tv_history_detail.setText(newMsgData.saveCaseApi.presentIllness);
            tv_history_before_detail.setText(newMsgData.saveCaseApi.pastIllness);
            tv_body_detail.setText(newMsgData.saveCaseApi.generalInspection);
            tv_result_detail.setText(newMsgData.saveCaseApi.diagnosis);
            tv_conclusion_detail.setText(newMsgData.saveCaseApi.suggestion);
        }


    }

    private void getCaseData() {
        EasyHttp.post(this).api(mSaveCaseApi).request(new HttpCallback<HttpData<List<SaveCaseApi>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<List<SaveCaseApi>> result) {
                super.onSucceed(result);
                if (result.getCode() == 0) {
                    if (null == result.getData() || result.getData().size() == 0) {
                        return;
                    }
                    SaveCaseApi saveCaseApi = result.getData().get(0);
                    tv_time.setText(TimeUtils.getTime(Long.parseLong(saveCaseApi.updateTime), TimeUtils.YY_MM_DD_FORMAT_3));

                    tv_main_detail.setText(saveCaseApi.chiefComplaint);
                    tv_history_detail.setText(saveCaseApi.presentIllness);
                    tv_history_before_detail.setText(saveCaseApi.pastIllness);
                    tv_body_detail.setText(saveCaseApi.generalInspection);
                    tv_result_detail.setText(saveCaseApi.diagnosis);
                    tv_conclusion_detail.setText(saveCaseApi.suggestion);
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }
}
