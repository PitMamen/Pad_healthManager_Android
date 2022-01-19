package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanDetailContract;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanPreviewContract;
import com.bitvalue.health.model.healthmanagermodel.HealthPlanDetailModel;
import com.bitvalue.health.model.healthmanagermodel.HealthPlanPreviewModel;

import java.util.List;

/**
 * 随访计划 任务时间列表
 * @author created by bitvalue
 * @data :
 */
public class HealthPlanPreviewPresenter extends BasePresenter<HealthPlanPreviewContract.View, HealthPlanPreviewContract.Model> {


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


    public void queryhealtPlan(int planID) {
        mModel.queryhealtPlan(planID, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().queryhealtPlanSuccess((List<HealthPlanTaskListBean>) o);
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
    protected HealthPlanPreviewContract.Model createModule() {
        return new HealthPlanPreviewModel();
    }
}
