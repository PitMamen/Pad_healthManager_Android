package com.bitvalue.health.contract.cloudcliniccontract;

import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.callback.Callback;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

import java.util.ArrayList;

/**
 * @author created by bitvalue
 * @data : 10/28
 */
public class CloudClinicContract {


    public interface CloudClinicView extends IView {
        void getConversationSuccess(V2TIMConversationResult v2TIMConversationResult); //获取对话框成功

        void getConversationfail(int code, String message);   //获取对话框失败

        void onNewMessage(V2TIMMessage v2TIMMessage);   //监听新消息

        void qryMedicalPatientsSuccess(ArrayList<VideoClientsResultBean> result);
        void qryMedicalPatientsNotData();

        void qryMedicalPatientsFail(String failMessage);

    }


    public interface CloudClinicModel extends IModel {
        void getConversationList(long nextSeq, int count, Callback customCallback);

        void listenerIMNewMessage(Callback customback);

        void qryMedicalPatients(String attendanceStatus,Callback callback); //云门诊就诊列表查询
    }


    public interface CloudClinicPersent {
        void getConversationList(long nextSeq, int count);  // 获取对话框

        void listennerIMNewMessage();   //监听聊天新消息

        void qryMedicalPatients(String attendanceStatus); //云门诊就诊列表查询
    }


}
