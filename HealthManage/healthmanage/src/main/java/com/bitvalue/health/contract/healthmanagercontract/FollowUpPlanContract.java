package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.requestbean.AllocatedPatientRequest;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PlanListBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 01/26
 */
public interface FollowUpPlanContract {
    interface View extends IView {

        void qryMyPlanSuccess(List<PlanListBean> planListBeans);
        void qryMyplanFail(String failMessage);


    }


    interface Model extends IModel {
        void qryMyPlan(AllocatedPatientRequest allocatedPatientRequest, Callback callback);
    }


    interface Presenter {

        void qryMyPlan(AllocatedPatientRequest allocatedPatientRequest);

    }





}
