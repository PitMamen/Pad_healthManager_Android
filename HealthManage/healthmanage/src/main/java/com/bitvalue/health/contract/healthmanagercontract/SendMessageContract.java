package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.requestbean.SendUserRemind;
import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.PlanTaskDetail;
import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.List;

/**
 * @author created by bitvalue
 * @data :
 */
public interface SendMessageContract {
    interface View extends IView {
        void sendSuccess();

    }

    interface Model extends IModel {

        void sendMessage(SaveAnalyseApi body, Callback callback);

    }



}
