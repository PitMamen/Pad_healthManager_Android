package com.bitvalue.health.model.settingmodel;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.settingcontract.MedicalRecordsContract;
import com.bitvalue.health.util.EmptyUtil;

import java.util.EnumMap;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data :
 */
public class MedicalRecordsModel extends BaseModel implements MedicalRecordsContract.Model {
    @Override
    public void qryMyMedicalRecords(String type, String keyWord, Callback callback) {
        mApi.qryMyMedicalRecords(type, keyWord).subscribeOn(Schedulers.io()).subscribe(arrayListApiResult -> {
            if (!EmptyUtil.isEmpty(arrayListApiResult)) {
                if (arrayListApiResult.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(arrayListApiResult.getData())) {
                        callback.onSuccess(arrayListApiResult.getData(), 1000);
                    }
                } else {
                    callback.onFailedLog(arrayListApiResult.getMessage(), 1001);
                }
            }
        }, error -> {
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }
}
