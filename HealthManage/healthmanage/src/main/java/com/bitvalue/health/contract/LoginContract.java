package com.bitvalue.health.contract;

import com.bitvalue.health.api.requestbean.LoginReqBean;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.callback.Callback;

public class LoginContract {


    public interface loginModel extends IModel {
        void login(LoginReqBean loginReqBean, Callback callback);
    }


    public interface loginView extends IView {
        void loginSuccess(Object object);

        void loginFail(String failMessage);
    }


    public interface loginPersenter {
       void  login(LoginReqBean loginReqBean);
    }


}
