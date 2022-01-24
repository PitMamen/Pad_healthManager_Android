package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.PlanTaskDetail;
import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.HealthPlanPreviewContract;
import com.bitvalue.health.contract.healthmanagercontract.SendMessageContract;
import com.bitvalue.health.model.healthmanagermodel.HealthPlanPreviewModel;
import com.bitvalue.health.model.healthmanagermodel.SendMessageModel;
import com.bitvalue.sdk.collab.utils.ToastUtil;

import java.util.List;

/**
 * 发送提醒
 * @author created by bitvalue
 * @data :
 */
public class SendMessagePresenter extends BasePresenter<SendMessageContract.View, SendMessageContract.Model> {


    @Override
    protected SendMessageContract.Model createModule() {
        return new SendMessageModel();
    }


    public void sendMessage(SaveAnalyseApi saveAnalyseApi) {
        mModel.sendMessage(saveAnalyseApi,  new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                getView().sendSuccess();
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    if (str!=null)
                    ToastUtil.toastShortMessage(str);
                }


            }
        });
    }
}
