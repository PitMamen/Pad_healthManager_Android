package com.bitvalue.health.contract.cloudcliniccontract;

import com.bitvalue.health.api.requestbean.GetHistoryApi;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.callback.Callback;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data :10/29
 */
public class CaseHistoryPreviewContract {

    public interface  CaseHistoryFreViewView extends IView {
        void qryCaseMedicalSuccess(ArrayList<SaveCaseApi> listCaseBean);
        void qryCaseMedicalFail(String Failmessage);


    }



    public interface CaseHistoryFreViewModel extends IModel {
        void qryUserMedicalCase(GetHistoryApi qryMedicalCase, Callback callback);

    }


    public interface CaseHistoryFreViewPersenter {


        void qryUserMedicalCase(GetHistoryApi qryMedicalCase);


    }
}
