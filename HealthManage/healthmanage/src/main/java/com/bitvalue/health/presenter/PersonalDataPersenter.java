package com.bitvalue.health.presenter;

import com.bitvalue.health.api.requestbean.PersonalDataBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.settingcontract.PersonalDataContract;
import com.bitvalue.health.model.DocPersonalModel;

/**
 * @author created by bitvalue
 * @data : 10/29
 */
public class PersonalDataPersenter extends BasePresenter<PersonalDataContract.PersonalDataView, PersonalDataContract.PersonalDataModel> implements PersonalDataContract.PersonalDataPersenter {
    @Override
    protected PersonalDataContract.PersonalDataModel createModule() {
        return new DocPersonalModel();
    }

    @Override
    public void getPersonalData() {
        mModel.getPersonalData(new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().getPersonalDocDataSuccess((PersonalDataBean) o);
                }

            }


            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().getPersonalDocDataFail(str);
                }
            }
        });


    }

    @Override
    public void logoutAcount() {
        mModel.logout(new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().logoutAcountSuccess();
                }
            }


            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().logoutAcountFail();
                }


            }
        });
    }


}
