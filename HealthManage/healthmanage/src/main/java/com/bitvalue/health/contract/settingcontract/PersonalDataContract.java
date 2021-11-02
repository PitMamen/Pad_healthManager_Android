package com.bitvalue.health.contract.settingcontract;

import com.bitvalue.health.api.requestbean.PersonalDataBean;
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
        void logoutAcountFail();
    }


    public interface PersonalDataModel extends IModel {
        void getPersonalData(Callback callback);
        void logout(Callback callback);
    }


    public interface PersonalDataPersenter {
        void getPersonalData();
        void logoutAcount();
    }
}
