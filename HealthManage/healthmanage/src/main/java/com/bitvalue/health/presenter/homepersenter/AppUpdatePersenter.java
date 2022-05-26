package com.bitvalue.health.presenter.homepersenter;

import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.AppUpdateContract;
import com.bitvalue.health.model.homemodel.AppUpdateModel;

/**
 * @author created by bitvalue
 * @data : 05/25
 */
public class AppUpdatePersenter extends BasePresenter<AppUpdateContract.View, AppUpdateContract.Model> implements AppUpdateContract.Presenter {
    @Override
    protected AppUpdateContract.Model createModule() {
        return new AppUpdateModel();
    }

    @Override
    public void getAppDownUrl(String id) {
        if (mModel != null) {
            mModel.getAppDownUrl(id, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().getAppDownUrlSuccess((String) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().getAppDownUrlFaile(str);
                    }
                }
            });
        }
    }
}
