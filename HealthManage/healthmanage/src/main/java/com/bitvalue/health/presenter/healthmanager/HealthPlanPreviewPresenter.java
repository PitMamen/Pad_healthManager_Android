package com.bitvalue.health.presenter.healthmanager;

import android.util.Log;

import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.PlanTaskDetail;
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


    public void queryTaskDetail(int planID, int taskId,String userId) {
        mModel.queryTaskDetail( planID,  taskId, userId, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().queryHealthPlanContentSuccess((PlanTaskDetail) o);
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


    public void queryhealtPlan(String planID) {
        if (mModel==null){
            Log.e("TAG","mModel==null");
        }
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
