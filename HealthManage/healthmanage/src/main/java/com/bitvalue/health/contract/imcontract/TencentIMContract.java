package com.bitvalue.health.contract.imcontract;

import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.callback.Callback;

/**
 * @author created by bitvalue
 * @data : 11:42
 */
public class TencentIMContract {


    public interface TUIKitView extends IView {
        void LoginSuccess(Object o);

        void LoginFail(String module, final int code, final String desc);
    }


    public interface TUIKitModel extends IModel {
        void IMLogin(String userid, String usersig, Callback callback);
    }

    public interface TUIKitPersenter {
        void IMLogin(String userid, String usersig);   //1·IM 登录   （2·监听新消息  3· 获取对话框）
    }
}
