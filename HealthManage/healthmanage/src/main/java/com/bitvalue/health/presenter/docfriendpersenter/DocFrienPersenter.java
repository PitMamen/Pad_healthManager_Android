package com.bitvalue.health.presenter.docfriendpersenter;

import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.doctorfriendscontract.DoctorFriendsContract;
import com.bitvalue.health.model.docfriendmodel.DoctorFriendsModel;
import com.bitvalue.health.util.EmptyUtil;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

/**
 * @author created by bitvalue
 * @data : 10/28
 */
public class DocFrienPersenter extends BasePresenter<DoctorFriendsContract.DoctorFriendsView, DoctorFriendsContract.DoctorFriendsModel> implements DoctorFriendsContract.DoctorFriendsPersent {
    @Override
    protected DoctorFriendsContract.DoctorFriendsModel createModule() {
        return new DoctorFriendsModel();
    }

    @Override
    public void getConversationList(long nextSeq, int count) {
        mModel.getConversationList(nextSeq, count, new CallBackAdapter() {
            @Override
            public void loginV2TIMSuccess(V2TIMConversationResult v2TIMConversationResult) {
                super.loginV2TIMSuccess(v2TIMConversationResult);
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
}
