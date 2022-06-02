package com.bitvalue.health.contract.homecontract;

import com.bitvalue.health.api.responsebean.CheckNewVersionBean;
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


        void checkNewAppVersionSuccess(CheckNewVersionBean newVersionBean);

        void checkNewAppVersionFail(String failMessage);

    }


    public interface TUIKitModel extends IModel {
        void IMLogin(String userid, String usersig, Callback callback);

        void checkNewAppVersion(Callback callback);
    }

    public interface TUIKitPersenter {
        void IMLogin(String userid, String usersig);

        void checkNewAppVersion();
    }





}
