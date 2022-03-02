package com.bitvalue.health.model.healthmanagermodel;

import android.util.Log;

import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.InterestsUseApplyByDocContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 02/22
 */
public class InterestsUseApplyByDocModel extends BaseModel implements InterestsUseApplyByDocContract.Model {
    @Override
    public void saveRightsUseRecord(SaveRightsUseBean bean, Callback callback) {
        if (!EmptyUtil.isEmpty(bean)) {
            mApi.saveRightsRecord(bean).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        Log.e(TAG, "saveRightsUseRecord: "+result.getData() );
                        SaveRightsUseBean saveRightsUseBean = result.getData();
                        if (!EmptyUtil.isEmpty(saveRightsUseBean)) {
                            callback.onSuccess(saveRightsUseBean, 1000);
                        } else {
                            callback.onSuccess("null", 1000);
                        }
                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }
            }, error -> {
                Log.e(TAG, "结束问诊出错: "+error.getMessage() );
                callback.onFailedLog("操作失败", 1001);
            });
        }
    }
}
