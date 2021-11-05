package com.bitvalue.health.presenter.settingpresenter;

import com.bitvalue.health.api.responsebean.PlanListBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.settingcontract.HealthPlanContract;
import com.bitvalue.health.model.settingmodel.HealthPlanModel;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data :
 */
public class HealthPlanPresenter extends BasePresenter<HealthPlanContract.View, HealthPlanContract.Model> implements HealthPlanContract.Presenter {

    @Override
    protected HealthPlanContract.Model createModule() {
        return new HealthPlanModel();
    }

    @Override
    public void getHealthPlanTempalte() {
        mModel.getHealthPlanTempalte(new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().getMyHealthPlanTempalteSuccess((ArrayList<PlanListBean>) o);
                }
            }


            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().getMyHealthPlanTempalteFail(str);
                }
            }
        });
    }


}
