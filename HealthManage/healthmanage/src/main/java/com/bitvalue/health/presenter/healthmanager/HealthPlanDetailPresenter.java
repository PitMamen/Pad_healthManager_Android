package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanDetailContract;
import com.bitvalue.health.model.healthmanagermodel.HealthPlanDetailModel;

/**
 * @author created by bitvalue
 * @data :
 */
public class HealthPlanDetailPresenter extends BasePresenter<HealthPlanDetailContract.View, HealthPlanDetailContract.Model> implements HealthPlanDetailContract.Presenter {

    @Override
    public void queryHealthPlanContent(String contentId, String planType, String userid) {
        mModel.queryHealthPlanContent(contentId, planType, userid, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().queryHealthPlanContentSuccess((TaskPlanDetailBean) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().queryHealthFail(str);
                }
            }
        });
    }

    @Override
    public void queryhealtPlan(String planID) {
        mModel.queryhealtPlan(planID, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().queryhealtPlanSuccess((PlanDetailResult) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().queryHealthFail(str);
                }


            }
        });

    }

    @Override
    protected HealthPlanDetailContract.Model createModule() {
        return new HealthPlanDetailModel();
    }
}
