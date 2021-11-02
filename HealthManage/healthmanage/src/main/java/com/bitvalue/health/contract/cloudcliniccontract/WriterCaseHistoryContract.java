package com.bitvalue.health.contract.cloudcliniccontract;

import com.bitvalue.health.api.requestbean.GetHistoryApi;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.callback.Callback;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data : 10/29
 * 书写病历Contract
 */
public class WriterCaseHistoryContract {




    public interface  WriterCaseHistoryView extends IView{
        void qryCaseMedicalSuccess(ArrayList<SaveCaseApi> listCaseBean);
        void qryCaseMedicalFail(String Failmessage);

        void  commitCaseHistorySuccess(SaveCaseApi listCaseBean);
        void  commitCaseHistoryFail(String FailMessage);

    }



    public interface WriterCaseHistoryModel extends IModel {
        void qryUserMedicalCase(GetHistoryApi qryMedicalCase, Callback callback);

        void commitCaseHistory(SaveCaseApi caseHistorybean, Callback callback);
    }


    public interface WriterCaseHistoryPersenter {


        void qryUserMedicalCase(GetHistoryApi qryMedicalCase);

        void commitCaseHistory(SaveCaseApi caseHistorybean);

    }
}
