package com.bitvalue.health.model.homemodel;

import android.util.Log;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.AppUpdateContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 05/25
 */
public class AppUpdateModel extends BaseModel implements AppUpdateContract.Model {


    @Override
    public void getAppDownUrl(String id, Callback callback) {
        if (!EmptyUtil.isEmpty(id)) {
            mApi.getAppDownUrl(id).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    Log.e(TAG, "getAppDownUrl: " + result.toString());
                    if (result.getCode() == 0) {
                        callback.onSuccess(result.getMessage(), 1000);
                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }
            }, error -> {
                Log.e(TAG, "getAppDownUrl error:" + error.getMessage());
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }
}
