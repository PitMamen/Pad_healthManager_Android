package com.bitvalue.health.presenter.cloudclinicpersenter;

import com.bitvalue.health.api.requestbean.GetHistoryApi;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.cloudcliniccontract.CaseHistoryPreviewContract;
import com.bitvalue.health.model.cloudclinicmodel.CaseHistoryPreviewModel;

import java.util.ArrayList;

/**
 * 病历预览Persenter
 * @author created by bitvalue
 * @data : 10/29
 */
public class CaseHistoryPreviewPersenter extends BasePresenter<CaseHistoryPreviewContract.CaseHistoryFreViewView,CaseHistoryPreviewContract.CaseHistoryFreViewModel> implements CaseHistoryPreviewContract.CaseHistoryFreViewPersenter {
    @Override
    protected CaseHistoryPreviewContract.CaseHistoryFreViewModel createModule() {
        return new CaseHistoryPreviewModel();
    }

    @Override
    public void qryUserMedicalCase(GetHistoryApi qryMedicalCase) {
        mModel.qryUserMedicalCase(qryMedicalCase, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().qryCaseMedicalSuccess((ArrayList<SaveCaseApi>) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().qryCaseMedicalFail(str);
                }
            }
        });
    }
}
