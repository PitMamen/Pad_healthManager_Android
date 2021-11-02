package com.bitvalue.health.model.homemodel;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.imcontract.TencentIMContract;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;

/**
 * @author created by bitvalue
 * @data : 11:50
 */
public class TencentIMModel extends BaseModel implements TencentIMContract.TUIKitModel {
    @Override
    public void IMLogin(String userid, String usersig, Callback callback) {
        TUIKit.login(userid, usersig, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                callback.IMloginSuccess(data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                callback.IMloginFail(module, errCode, errMsg);
            }
        });
    }
}
