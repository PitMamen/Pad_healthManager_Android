package com.bitvalue.health.model.chatmodel;

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
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        ArrayList<VideoClientsResultBean> resultData = result.getData();
                        if (!EmptyUtil.isEmpty(resultData)) {
                            callback.onSuccess(resultData, 1000);
                        }
                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }

                }
            }, error -> {
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }
}
