package com.bitvalue.health.model.settingmodel;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.settingcontract.HealthPlanContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data :
 */
public class HealthPlanModel extends BaseModel implements HealthPlanContract.Model {

    @Override
    public void getHealthPlanTempalte(Callback callback) {
        mApi.getMyPlanTemplate().subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        callback.onSuccess(result.getData(), 1000);
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
