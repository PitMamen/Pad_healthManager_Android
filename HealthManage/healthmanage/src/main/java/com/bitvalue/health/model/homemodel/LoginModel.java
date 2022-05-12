package com.bitvalue.health.model.homemodel;

import android.util.Log;

import com.bitvalue.health.api.requestbean.LoginReqBean;
import com.bitvalue.health.api.responsebean.LoginResBean;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.homecontract.LoginContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;


/***
 * 登录Model
 */
public class LoginModel extends BaseModel implements LoginContract.loginModel {
    @Override
    public void login(LoginReqBean loginReqBean, Callback callback) {
        mApi.login(loginReqBean).subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    LoginResBean resBean = result.getData();
                    if (!EmptyUtil.isEmpty(resBean))
                        callback.onSuccess(resBean, 1000);
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            }

        }, error -> {
            callback.onFailedLog("登录失败!" + error.getMessage(), 1001);
        });
    }

    @Override
    public void qryCodeValue(String codeGroup, Callback callback) {
        if (!EmptyUtil.isEmpty(codeGroup)) {
            mApi.qryCodeValue(codeGroup).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    Log.e(TAG, "qryCodeValue: " + result.toString());
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
}
