package com.bitvalue.health.contract.homecontract;

import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.callback.Callback;

/**
 * @author created by bitvalue
 * @data : 13:52
 */
public class HomeContract {


    public interface TUIKitView extends IView {
        void LoginSuccess(Object o);

        void LoginFail(String module, final int code, final String desc);
    }


    public interface TUIKitModel extends IModel {
        void IMLogin(String userid, String usersig, Callback callback);
    }

    public interface TUIKitPersenter {
        void IMLogin(String userid, String usersig);
    }





}
