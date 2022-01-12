package com.bitvalue.health.presenter.healthmanager;

import android.util.Log;

import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.PatientReportContract;
import com.bitvalue.health.model.healthmanagermodel.PatientReportModel;
import com.bitvalue.health.util.EmptyUtil;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data :
 */
public class PatientReportPresenter extends BasePresenter<PatientReportContract.View, PatientReportContract.Model> implements PatientReportContract.Presenter {
    @Override
    protected PatientReportContract.Model createModule() {
        return new PatientReportModel();
    }


}
