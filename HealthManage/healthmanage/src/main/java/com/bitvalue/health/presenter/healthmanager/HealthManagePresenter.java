package com.bitvalue.health.presenter.healthmanager;

import android.util.Log;

import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.HealthManageContract;
import com.bitvalue.health.model.healthmanager.HealthManageModel;
import com.bitvalue.health.util.EmptyUtil;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data :
 */
public class HealthManagePresenter extends BasePresenter<HealthManageContract.View, HealthManageContract.Model> implements HealthManageContract.Presenter {
    @Override
    protected HealthManageContract.Model createModule() {
        return new HealthManageModel();
    }

    @Override
    public void getConversationList(long nextSeq, int count) {
        mModel.getConversationList(nextSeq, count, new CallBackAdapter() {
            @Override
            public void loginV2TIMSuccess(V2TIMConversationResult v2TIMConversationResult) {
                super.loginV2TIMSuccess(v2TIMConversationResult);
                Log.e(TAG, "loginV2TIMSuccess:CloudClinicPersenter");
                if (isViewAttach() && null != v2TIMConversationResult)
                    getView().getConversationSuccess(v2TIMConversationResult);
            }


            @Override
            public void loginV2TIMFail(int code, String desc) {
                super.loginV2TIMFail(code, desc);
                if (isViewAttach())
                    getView().getConversationfail(code, desc);
            }
        });
    }

    @Override
    public void listennerIMNewMessage() {
        Log.e(TAG, "listennerIMNewMessage: " + mModel);
        mModel.listenerIMNewMessage(new CallBackAdapter() {
            @Override
            public void IMgetNewMessage(V2TIMMessage v2TIMMessage) {
                super.IMgetNewMessage(v2TIMMessage);
                if (isViewAttach())
                    if (!EmptyUtil.isEmpty(v2TIMMessage)) {
                        getView().onNewMessage(v2TIMMessage);
                    }
            }
        });
    }

    @Override
    public void getMyPatients() {
        mModel.getMyPatients(new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().getPatientSuccess((ArrayList<ClientsResultBean>) o);
                }
            }


            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().getPatientFail(str);
                }
            }
        });

    }
}
