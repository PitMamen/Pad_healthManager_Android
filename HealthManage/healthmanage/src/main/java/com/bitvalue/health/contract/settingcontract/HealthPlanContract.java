package com.bitvalue.health.contract.settingcontract;

import com.bitvalue.health.api.responsebean.PlanListBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data :
 */
public interface HealthPlanContract {
    interface View extends IView {
        void getMyHealthPlanTempalteSuccess(ArrayList<PlanListBean> planListBeans);

        void getMyHealthPlanTempalteFail(String messageFail);
    }


    interface Model extends IModel {
        void getHealthPlanTempalte(Callback callback);

    }


    interface Presenter {

        void getHealthPlanTempalte();

    }
}
