package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.HealthAnalseContract;
import com.bitvalue.health.model.healthmanager.HealthAnalseModel;

/**
 * @author created by bitvalue
 * @data :
 */
public class HealthAnalsePresenter extends BasePresenter<HealthAnalseContract.View, HealthAnalseContract.Model> implements HealthAnalseContract.Presenter {
    @Override
    protected HealthAnalseContract.Model createModule() {
        return new HealthAnalseModel();
    }

    @Override
    public void commitAnalse(SaveAnalyseApi commitBody) {
        mModel.commitAnalse(commitBody, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().commitSuccess((SaveAnalyseApi) o);
                }
            }


            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().commitFail(str);
                }
            }
        });
    }
}
