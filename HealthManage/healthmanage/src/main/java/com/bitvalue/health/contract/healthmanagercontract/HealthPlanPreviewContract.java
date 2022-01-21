package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.PlanTaskDetail;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.List;

/**
 * @author created by bitvalue
 * @data :
 */
public interface HealthPlanPreviewContract {
    interface View extends IView {
        void queryhealtPlanSuccess(List<HealthPlanTaskListBean> taskListBeanList);

        void queryHealthPlanContentSuccess(PlanTaskDetail taskPlanDetailBean);

        void queryHealthFail(String failMessage);

    }

    interface Model extends IModel {

        void queryTaskDetail(int planID, int taskId,String userId, Callback callback);

        void queryhealtPlan(int planID, Callback callback);

    }



}
