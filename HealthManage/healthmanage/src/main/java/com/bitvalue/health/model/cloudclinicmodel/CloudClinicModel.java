package com.bitvalue.health.model.cloudclinicmodel;

import android.util.Log;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.cloudcliniccontract.CloudClinicContract;
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
 * @data : 10/28
 */
public class CloudClinicModel extends BaseModel implements CloudClinicContract.CloudClinicModel {
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
    public void qryMedicalPatients(String attendanceStatus, Callback callback) {

        mApi.getMyMedicalPatients(attendanceStatus).subscribeOn(Schedulers.io()).subscribe(result->{
            if (!EmptyUtil.isEmpty(result)){
               if (result.getCode()==0){
                       callback.onSuccess(result.getData(),1000);
               }else {
                   callback.onFailedLog(result.getMessage(),1001);
               }
            }else {
                Log.e(TAG, "qryMedicalPatients = null " );
            }
        },error->{
            Log.e(TAG, "qryMedicalPatients: "+error.getMessage() );
            callback.onError(error.getMessage(),1001);
        });
    }
}
