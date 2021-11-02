package com.bitvalue.health.presenter.cloudclinicpersenter;

import com.bitvalue.health.api.requestbean.GetHistoryApi;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.cloudcliniccontract.WriterCaseHistoryContract;
import com.bitvalue.health.model.cloudclinicmodel.WriterCaseHistoryModel;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data : 10/29
 */
public class WriterCaseHistoryPersenter extends BasePresenter<WriterCaseHistoryContract.WriterCaseHistoryView, WriterCaseHistoryContract.WriterCaseHistoryModel> implements WriterCaseHistoryContract.WriterCaseHistoryPersenter {
    @Override
    protected WriterCaseHistoryContract.WriterCaseHistoryModel createModule() {
        return new WriterCaseHistoryModel();
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

    @Override
    public void commitCaseHistory(SaveCaseApi caseHistorybean) {
        mModel.commitCaseHistory(caseHistorybean, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().commitCaseHistorySuccess((SaveCaseApi) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().commitCaseHistoryFail(str);
                }
            }
        });

    }
}
