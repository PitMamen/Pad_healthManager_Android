package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.HealthAnalyseDisplayContract;
import com.bitvalue.health.model.healthmanagermodel.HealthAnalyseDisplayModel;

/**
 * @author created by bitvalue
 * @data :
 */
public class HealthAnalyseDisplayPresenter extends BasePresenter<HealthAnalyseDisplayContract.View, HealthAnalyseDisplayContract.Model> implements HealthAnalyseDisplayContract.Presenter {
    @Override
    protected HealthAnalyseDisplayContract.Model createModule() {
        return new HealthAnalyseDisplayModel();
    }

    @Override
    public void getUserEevaluate(int id) {
        mModel.getUserEevaluate(id, new CallBackAdapter() {
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()) {
                    getView().getUserEevaluateSuccess((SaveAnalyseApi) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()) {
                    getView().getUserEevaluateFail(str);
                }
            }
        });

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
