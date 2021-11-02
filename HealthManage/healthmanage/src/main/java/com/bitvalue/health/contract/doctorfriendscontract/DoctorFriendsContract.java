package com.bitvalue.health.contract.doctorfriendscontract;

import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.callback.Callback;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

/**
 * @author created by bitvalue
 * @data : 10/28
 */
public class DoctorFriendsContract {


    public interface DoctorFriendsView extends IView{
        void getConversationSuccess(V2TIMConversationResult v2TIMConversationResult); //获取对话框成功

        void getConversationfail(int code, String message);   //获取对话框失败

        void onNewMessage(V2TIMMessage v2TIMMessage);   //监听新消息
    }




    public interface DoctorFriendsModel extends IModel {
        void getConversationList(long nextSeq, int count, Callback customCallback);

        void  listenerIMNewMessage(Callback customback);
    }


    public interface DoctorFriendsPersent {
        void getConversationList(long nextSeq, int count);  // 获取对话框

        void listennerIMNewMessage();   //监听聊天新消息
    }
}
