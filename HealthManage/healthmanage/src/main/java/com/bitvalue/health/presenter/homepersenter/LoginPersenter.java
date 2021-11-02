package com.bitvalue.health.presenter.homepersenter;

import android.util.Log;

import com.bitvalue.health.api.requestbean.LoginReqBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.LoginContract;
import com.bitvalue.health.model.homemodel.LoginModel;

public class LoginPersenter extends BasePresenter<LoginContract.loginView, LoginContract.loginModel> implements LoginContract.loginPersenter {
    @Override
    protected LoginContract.loginModel createModule() {
        return new LoginModel();
    }

    @Override
    public void login(LoginReqBean loginReqBean) {

        mModel.login(loginReqBean, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach())
                    getView().loginSuccess(o);

            }

            @Override
            public void onFailedLog(String str, int what) {
                Log.e(TAG, "onFailedLog: "+str );
                getView().loginFail(str);
            }
        });

    }
}
