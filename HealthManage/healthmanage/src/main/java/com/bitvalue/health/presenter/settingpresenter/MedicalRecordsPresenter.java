package com.bitvalue.health.presenter.settingpresenter;

import com.bitvalue.health.api.responsebean.PatientResultBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.settingcontract.MedicalRecordsContract;
import com.bitvalue.health.model.settingmodel.MedicalRecordsModel;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data :
 */
public class MedicalRecordsPresenter extends BasePresenter<MedicalRecordsContract.View, MedicalRecordsContract.Model> implements MedicalRecordsContract.Presenter {
    @Override
    protected MedicalRecordsContract.Model createModule() {
        return new MedicalRecordsModel();
    }

    @Override
    public void qryMyMedicalRecords(String type, String keyWord) {
        mModel.qryMyMedicalRecords(type, keyWord, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().qryMyMedicalRecordsSuccess((ArrayList<PatientResultBean>) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().qryMyMedicalRecordsFail(str);
                }
            }
        });
    }
}
