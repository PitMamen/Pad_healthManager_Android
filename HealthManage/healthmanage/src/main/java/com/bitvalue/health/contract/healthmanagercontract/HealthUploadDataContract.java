package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

/**
 * @author created by bitvalue
 * @data :
 */
public interface HealthUploadDataContract {
    interface Model extends IModel {
        void queryHealthPlanContent(String contentId, String planType, String userId, Callback callback);
    }

    interface View  extends IView {
        void querySuccess(TaskDetailBean taskDetailBean);
        void queryFail(String failMessage);
    }

    interface Presenter {
        void queryHealthPlanContent(String contentId,String planType,String userId);

    }
}
