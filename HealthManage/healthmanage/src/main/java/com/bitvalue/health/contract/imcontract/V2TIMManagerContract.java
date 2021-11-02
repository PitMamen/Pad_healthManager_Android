package com.bitvalue.health.contract.imcontract;

import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.callback.Callback;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

/**
 * @author created by bitvalue
 * @data : 20:05
 */
public class V2TIMManagerContract {

    public interface V2TIMManagerView extends IView {
        void loginSuccess(V2TIMConversationResult v2TIMConversationResult); //IM登录成功

        void loginIMfail(int code, String message);   //IM登录失败

        void onNewMessage(V2TIMMessage v2TIMMessage);   //监听新消息
    }


    public interface V2TIMManagerModel extends IModel {
        void getConversationList(long nextSeq, int count, Callback customCallback);

        void  listennerIMNewMessage(Callback customback);
    }


    public interface V2TIMManagerPersenter {
        void getConversationList(long nextSeq, int count);  // 获取对话框

        void listennerIMNewMessage();   //监听聊天新消息
    }

}
