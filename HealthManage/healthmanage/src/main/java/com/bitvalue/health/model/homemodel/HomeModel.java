package com.bitvalue.health.model.homemodel;

import android.text.TextUtils;
import android.util.Log;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.homecontract.HomeContract;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.tencent.imsdk.v2.V2TIMManager;

import io.reactivex.schedulers.Schedulers;

/**
 * 主界面Model
 * @author created by bitvalue
 * @data : 10/27
 */
public class HomeModel extends BaseModel implements HomeContract.TUIKitModel {


    @Override
    public void IMLogin(String userid, String usersig, Callback callback) {
         if (userid!=null&&!TextUtils.isEmpty(userid)&&null!=usersig&&!TextUtils.isEmpty(usersig)){
             TUIKit.login(userid, usersig, new IUIKitCallBack() {
                 @Override
                 public void onSuccess(Object data) {
                     Log.e(TAG, "IMlogin onSuccess-----------------" );
                     callback.IMloginSuccess(data);
                 }

                 @Override
                 public void onError(String module, int errCode, String errMsg) {
                     callback.IMloginFail(module,errCode,errMsg);
                 }
             });
         }
    }


    @Override
    public void checkNewAppVersion(Callback callback) {
        mApi.checkNewAppVersion().subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                Log.e(TAG, "checkNewAppVersion: "+result.toString() );
                if (result.getCode() == 0) {
                    callback.onSuccess(result.getData(), 1000);
                } else {
                    callback.onFailed(result.getMessage(), 1001);
                }
            }
        }, error -> {
            Log.e(TAG, "查询APP新版本接口出错:" + error.getMessage());
            callback.onFailed(error.getMessage(), 1001);
        });
    }

}
