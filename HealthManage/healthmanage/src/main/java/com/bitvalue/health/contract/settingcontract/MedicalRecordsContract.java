package com.bitvalue.health.contract.settingcontract;

import com.bitvalue.health.api.responsebean.PatientResultBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data :
 */
public interface MedicalRecordsContract {
    interface Model extends IModel {

        void qryMyMedicalRecords(String type, String keyWord, Callback callback);


    }

    interface View extends IView {

        void qryMyMedicalRecordsSuccess(ArrayList<PatientResultBean> beanArrayList);

        void qryMyMedicalRecordsFail(String failMessage);

    }

    interface Presenter {

        void qryMyMedicalRecords(String type, String keyWord);

    }
}
