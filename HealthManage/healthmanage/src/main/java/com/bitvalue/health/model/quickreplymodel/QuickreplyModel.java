package com.bitvalue.health.model.quickreplymodel;

import android.util.Log;

import com.bitvalue.health.api.requestbean.QuickReplyRequest;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.quickreply.QuickReplyContract;
import com.bitvalue.health.util.EmptyUtil;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 04/28
 */
public class QuickreplyModel extends BaseModel implements QuickReplyContract.Model {
    @Override
    public void qryCaseCommonWords(QuickReplyRequest request, Callback callback) {
        if (request != null) {
            mApi.qryCaseCommonWords(request).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        List<QuickReplyRequest> list = result.getData();
                        callback.onSuccess(list, 1000);
                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }
            }, error -> {
                Log.e(TAG, "qryCaseCommonWords error: " + error.getMessage());
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }

    @Override
    public void saveCaseCommonWords(QuickReplyRequest request, Callback callback) {
        mApi.modify_createCommonWords(request).subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    QuickReplyRequest resultBean = result.getData();
                    callback.onSuccess(resultBean, 1000);
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            }
        }, error -> {
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }
}
