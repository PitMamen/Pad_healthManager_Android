package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.responsebean.PatientBaseInfoBean;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 01/07
 */
public interface VisitPlanDetailContract {

    interface Model extends IModel {

        void qryUserLocalVisit(UserLocalVisitBean visitBean,Callback callback);
        void getPatientBaseInfo(int userId,Callback callback);
    }

    interface View extends IView {

        void qryUserVisitSuccess(List<String> iamgeList);
        void qryUserVisitFail(String failMessage);
        void getPatientBaseInfoSuccess(PatientBaseInfoBean patientBaseInfoBean);
        void getPatientBaseInfoFail(String messageFail);
    }


    interface Presenter{

        void qryUserLocalVisit(UserLocalVisitBean visitBean);

        void getPatientBaseInfo(int userId);

    }

}
