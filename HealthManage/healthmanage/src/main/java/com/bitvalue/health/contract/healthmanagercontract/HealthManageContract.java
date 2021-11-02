package com.bitvalue.health.contract.healthmanagercontract;

import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data :
 */
public interface HealthManageContract {


    interface View extends IView {
        void getConversationSuccess(V2TIMConversationResult v2TIMConversationResult); //获取对话框成功

        void getConversationfail(int code, String message);   //获取对话框失败

        void onNewMessage(V2TIMMessage v2TIMMessage);   //监听新消息

        void getPatientSuccess(ArrayList<ClientsResultBean> beanArrayList);

        void getPatientFail(String failmessage);

    }


    interface Model extends IModel {
        void getConversationList(long nextSeq, int count, Callback customCallback);

        void listenerIMNewMessage(Callback customback);

        void getMyPatients(Callback callback);
    }


    interface Presenter {
        void getConversationList(long nextSeq, int count);  // 获取对话框

        void listennerIMNewMessage();   //监听聊天新消息

        void getMyPatients();  //获取健康管理用户

    }
}
