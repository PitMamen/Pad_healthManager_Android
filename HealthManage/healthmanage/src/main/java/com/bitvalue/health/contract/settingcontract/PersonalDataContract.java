package com.bitvalue.health.contract.settingcontract;

import com.bitvalue.health.api.requestbean.PersonalDataBean;
import com.bitvalue.health.api.requestbean.ResetPasswordRequestBean;
import com.bitvalue.health.base.model.IModel;
import com.bitvalue.health.base.view.IView;
import com.bitvalue.health.callback.Callback;

/**
 * @author created by bitvalue
 * @data : 10/29
 */
public class PersonalDataContract {


    public interface PersonalDataView extends IView {
        void getPersonalDocDataSuccess(PersonalDataBean docDataBean);
        void getPersonalDocDataFail(String messagefail);

        void logoutAcountSuccess();
        void logoutAcountFail(String failMessage);

        void resetPasswordSuccess(String successMessage);
        void resetPasswordFail(String failMessage);

    }


    public interface PersonalDataModel extends IModel {
        void getPersonalData(Callback callback);
        void logout(Callback callback);

        void resetPassword(ResetPasswordRequestBean requestBean,Callback callback);
    }


    public interface PersonalDataPersenter {
        void getPersonalData();
        void logoutAcount();
        void resetPassword(ResetPasswordRequestBean requestBean);
    }
}
