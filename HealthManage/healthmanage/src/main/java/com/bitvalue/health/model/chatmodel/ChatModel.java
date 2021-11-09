package com.bitvalue.health.model.chatmodel;

import android.util.Log;

import com.bitvalue.health.api.requestbean.ReportStatusBean;
import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.chattract.Chatcontract;
import com.bitvalue.health.util.EmptyUtil;

import java.util.ArrayList;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 10/29
 */
public class ChatModel extends BaseModel implements Chatcontract.ChatModel {
    @Override
    public void updateAttendanceStaus(ReportStatusBean bean, Callback callback) {
        if (!EmptyUtil.isEmpty(bean)) {
            mApi.updateStatus(bean).subscribeOn(Schedulers.io()).subscribe(result -> {
                Log.e(TAG, "updateAttendanceStaus1111" );
                if (!EmptyUtil.isEmpty(result)) {
                    Log.e(TAG, "updateAttendanceStaus222" );
                    if (result.getCode() == 0) {
                        Log.e(TAG, "updateAttendanceStaus333" );
                        callback.onSuccess("success", 1000);
                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                    Log.e(TAG, "updateAttendanceSta" );
                }else {
                    Log.e(TAG, "updateAttendanceStaus = null " );
                }
            }, error -> {
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }
}
