package com.bitvalue.health.model;

import android.util.Log;

import com.bitvalue.health.api.requestbean.PersonalDataBean;
import com.bitvalue.health.api.requestbean.ResetPasswordRequestBean;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.settingcontract.PersonalDataContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 10/29
 */
public class DocPersonalModel extends BaseModel implements PersonalDataContract.PersonalDataModel {
    @Override
    public void getPersonalData(Callback callback) {
        mApi.getDocPersonalDetail().subscribeOn(Schedulers.io()).subscribe(r -> {
            if (!EmptyUtil.isEmpty(r)) {
                if (r.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(r.getData())) {
                        PersonalDataBean docPersonal = r.getData();
                        callback.onSuccess(docPersonal, 1000);
                    }
                } else {
                    callback.onFailedLog(r.getMessage(), 1001);
                }
            }
        }, error -> {
            Log.e(TAG, "getPersonalData: " + error.getMessage());
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }

    @Override
    public void logout(Callback callback) {
        mApi.logout().subscribeOn(Schedulers.io()).subscribe(r -> {
            if (!EmptyUtil.isEmpty(r)) {
                Log.e(TAG, "logout: " + r.toString());
                if (r.getCode() == 0) {
                    callback.onSuccess("logout success", 1000);
                } else {
                    callback.onFailedLog("退出失败,请重试", 1001);
                }
            }
        }, error -> {
            callback.onFailedLog(error.getMessage(), 1001);
        });

    }

    @Override
    public void resetPassword(ResetPasswordRequestBean requestBean, Callback callback) {
        if (!EmptyUtil.isEmpty(requestBean)) {
            mApi.resetPassWord(requestBean).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        Log.e(TAG, "resetPassword: " + result.toString());
                        callback.onSuccess("success", 1000);
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
