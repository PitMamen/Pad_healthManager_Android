package com.bitvalue.health.model.docfriendmodel;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.doctorfriendscontract.DoctorFriendsContract;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IMEventListener;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;

/**
 * 医生好友Model
 * @author created by bitvalue
 * @data : 10/28
 */
public class DoctorFriendsModel extends BaseModel implements DoctorFriendsContract.DoctorFriendsModel {


    @Override
    public void getConversationList(long nextSeq, int count, Callback customCallback) {
        V2TIMManager.getConversationManager().getConversationList(nextSeq, count, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                customCallback.loginV2TIMSuccess(v2TIMConversationResult);
            }

            @Override
            public void onError(int code, String desc) {
                customCallback.loginV2TIMFail(code, desc);

            }
        });
    }

    @Override
    public void listenerIMNewMessage(Callback customback) {
        TUIKit.addIMEventListener(new IMEventListener() {
            @Override
            public void onNewMessage(V2TIMMessage v2TIMMessage) {
                super.onNewMessage(v2TIMMessage);
                customback.IMgetNewMessage(v2TIMMessage);
            }
        });
    }
}
