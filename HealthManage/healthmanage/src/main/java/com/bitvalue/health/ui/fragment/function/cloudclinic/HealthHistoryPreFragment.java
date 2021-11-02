package com.bitvalue.health.ui.fragment.function.cloudclinic;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bitvalue.health.api.requestbean.GetHistoryApi;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.cloudcliniccontract.CaseHistoryPreviewContract;
import com.bitvalue.health.presenter.cloudclinicpersenter.CaseHistoryPreviewPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.fragment.function.chat.ChatFragment;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/***
 * 病历预览界面
 */
public class HealthHistoryPreFragment extends BaseFragment<CaseHistoryPreviewPersenter> implements CaseHistoryPreviewContract.CaseHistoryFreViewView {

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
    private GetHistoryApi mSaveCaseApi;


    @Override
    public void initView(View RootView) {
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
    public void initData() {
        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
        tv_type.setText(loginBean.getUser().user.departmentName);
        tv_doc_name_detail.setText(loginBean.getUser().user.userName);

        if (null != newMsgData && newMsgData.saveCaseApi == null) {
            mSaveCaseApi = new GetHistoryApi();
            mSaveCaseApi.id = newMsgData.id;
            mSaveCaseApi.appointmentId = newMsgData.appointmentId;
               mPresenter.qryUserMedicalCase(mSaveCaseApi);
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

    @Override
    protected CaseHistoryPreviewPersenter createPresenter() {
        return new CaseHistoryPreviewPersenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_health_history_preview;
    }


    @Override
    public void qryCaseMedicalSuccess(ArrayList<SaveCaseApi> listCaseBean) {
        getActivity().runOnUiThread(() -> {
            SaveCaseApi saveCaseApi = listCaseBean.get(0);
            if (null != saveCaseApi.updateTime){
                tv_time.setText(TimeUtils.getTime(Long.parseLong(saveCaseApi.updateTime), TimeUtils.YY_MM_DD_FORMAT_3));
            }
            tv_main_detail.setText(saveCaseApi.chiefComplaint);
            tv_history_detail.setText(saveCaseApi.presentIllness);
            tv_history_before_detail.setText(saveCaseApi.pastIllness);
            tv_body_detail.setText(saveCaseApi.generalInspection);
            tv_result_detail.setText(saveCaseApi.diagnosis);
            tv_conclusion_detail.setText(saveCaseApi.suggestion);
        });


    }

    @Override
    public void qryCaseMedicalFail(String Failmessage) {
        Log.e(TAG, "qryCaseMedicalFail: "+Failmessage );
    }
}
