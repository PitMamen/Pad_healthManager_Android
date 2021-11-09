package com.bitvalue.health.presenter.chatpersenter;

import android.util.Log;

import com.bitvalue.health.api.requestbean.ReportStatusBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.chattract.Chatcontract;
import com.bitvalue.health.model.chatmodel.ChatModel;

/**
 * 聊天界面 Persenter
 * @author created by bitvalue
 * @data : 10/29
 */
public class ChatPersenter extends BasePresenter<Chatcontract.ChatIvew, Chatcontract.ChatModel> implements Chatcontract.ChatPersenter {
    @Override
    protected Chatcontract.ChatModel createModule() {
        return new ChatModel();
    }

    @Override
    public void updateAttendanceStatus(ReportStatusBean bean) {
        mModel.updateAttendanceStaus(bean, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                Log.e(TAG, "2222222222222: " );
                if (isViewAttach()) {
                    getView().updateAttendanceStausSuccess();
                }
            }


            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().updateFail(str);
                }


            }
        });
    }
}
