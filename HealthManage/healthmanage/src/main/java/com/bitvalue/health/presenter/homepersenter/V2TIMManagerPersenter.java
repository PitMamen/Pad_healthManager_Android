package com.bitvalue.health.presenter.homepersenter;

import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.imcontract.V2TIMManagerContract;
import com.bitvalue.health.model.homemodel.V2TIMManagerModel;
import com.bitvalue.health.util.EmptyUtil;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

/**
 * @author created by bitvalue
 * @data : 10/27 09:37
 */
public class V2TIMManagerPersenter extends BasePresenter<V2TIMManagerContract.V2TIMManagerView, V2TIMManagerContract.V2TIMManagerModel> implements V2TIMManagerContract.V2TIMManagerPersenter {
    @Override
    protected V2TIMManagerContract.V2TIMManagerModel createModule() {
        return new V2TIMManagerModel();
    }

    @Override
    public void getConversationList(long nextSeq, int count) {
        mModel.getConversationList(nextSeq, count, new CallBackAdapter() {
            @Override
            public void loginV2TIMSuccess(V2TIMConversationResult v2TIMConversationResult) {
                super.loginV2TIMSuccess(v2TIMConversationResult);
                if (isViewAttach() && null != v2TIMConversationResult)
                    getView().loginSuccess(v2TIMConversationResult);
            }


            @Override
            public void loginV2TIMFail(int code, String desc) {
                super.loginV2TIMFail(code, desc);
                if (isViewAttach())
                    getView().loginIMfail(code, desc);
            }
        });

    }

    @Override
    public void listennerIMNewMessage() {
        mModel.listennerIMNewMessage(new CallBackAdapter() {
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
