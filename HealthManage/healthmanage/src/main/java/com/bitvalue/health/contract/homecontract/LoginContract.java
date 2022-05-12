package com.bitvalue.health.contract.homecontract;

import com.bitvalue.health.api.requestbean.LoginReqBean;
import com.bitvalue.health.api.responsebean.CodeValueResopnse;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.callback.Callback;

import java.util.List;

public class LoginContract {


    public interface loginModel extends IModel {
        void login(LoginReqBean loginReqBean, Callback callback);

        void qryCodeValue(String codeGroup, Callback callback);
    }


    public interface loginView extends IView {
        void loginSuccess(Object object);

        void loginFail(String failMessage);

        void qryCodeValueSuccess(List<CodeValueResopnse> listdata);

        void qryCodeValueFail(String failmessage);
    }


    public interface loginPersenter {
        void login(LoginReqBean loginReqBean);

        void qryCodeValue(String codeGroup);
    }


}
