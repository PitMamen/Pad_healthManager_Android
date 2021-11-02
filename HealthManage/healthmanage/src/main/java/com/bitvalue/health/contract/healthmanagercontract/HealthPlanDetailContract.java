package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

/**
 * @author created by bitvalue
 * @data :
 */
public interface HealthPlanDetailContract {
    interface View extends IView {
        void queryhealtPlanSuccess(PlanDetailResult planDetailResult);

        void queryHealthPlanContentSuccess(TaskPlanDetailBean taskPlanDetailBean);

        void queryHealthFail(String failMessage);

    }

    interface Model extends IModel {

        void queryHealthPlanContent(String contentId, String planType, String userid, Callback callback);

        void queryhealtPlan(String planID, Callback callback);

    }


    interface Presenter {


        void queryHealthPlanContent(String contentId, String planType, String userid);

        void queryhealtPlan(String planID);

    }
}
