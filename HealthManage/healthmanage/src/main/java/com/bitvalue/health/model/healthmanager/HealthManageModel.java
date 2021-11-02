package com.bitvalue.health.model.healthmanager;

import android.util.Log;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.HealthManageContract;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IMEventListener;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data :
 */
public class HealthManageModel extends BaseModel implements HealthManageContract.Model {
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

    @Override
    public void getMyPatients(Callback callback) {
        mApi.getMyPatients().subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        callback.onSuccess(result.getData(), 1000);
                    }
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            }else {
                Log.e(TAG, "qryPatients = null " );
            }
        }, error -> {
            callback.onFailedLog(error.getMessage(), 1001);
        });


    }
}
