package com.bitvalue.health.model.docfriendmodel;

import android.util.Log;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.doctorfriendscontract.NeedDealWithContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;

/**
 * 医生好友Model
 *
 * @author created by bitvalue
 * @data : 10/28
 */
public class NeedDealWithModel extends BaseModel implements NeedDealWithContract.NeedDealWithModel {


    @Override
    public void getMyTaskDetail(int execFlag, int taskType, String docUserId, Callback callback) {
        if (!EmptyUtil.isEmpty(docUserId)) {
            mApi.getUserTask(execFlag, taskType, docUserId).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        if (!EmptyUtil.isEmpty(result.getData())) {
                            callback.onSuccess(result.getData(), 1000);
                        } else {
                            callback.onSuccess(null, 1001);
                        }

                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }else {
                    callback.onSuccess(null, 1001);
                }
            }, error -> {
                Log.e(TAG, "getMyTaskDetail: "+error.getMessage() );
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }

    @Override
    public void getMyAlreadyDealTaskDetail(int execFlag, int taskType, String docUserId,Callback callback) {
        if (!EmptyUtil.isEmpty(docUserId)) {
            mApi.getUserTask(execFlag, taskType, docUserId).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        if (!EmptyUtil.isEmpty(result.getData())) {
                            callback.onSuccess(result.getData(), 1000);
                        } else {
                            callback.onSuccess(null, 1001);
                        }

                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }else {
                    callback.onSuccess(null, 1001);
                }
            }, error -> {
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }
}
