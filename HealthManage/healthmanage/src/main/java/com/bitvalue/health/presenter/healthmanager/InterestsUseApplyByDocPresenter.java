package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.InterestsUseApplyByDocContract;
import com.bitvalue.health.model.healthmanagermodel.InterestsUseApplyByDocModel;

/**
 * @author created by bitvalue
 * @data : 02/22
 */
public class InterestsUseApplyByDocPresenter extends BasePresenter<InterestsUseApplyByDocContract.View,InterestsUseApplyByDocContract.Model> implements InterestsUseApplyByDocContract.Presenter {
    @Override
    protected InterestsUseApplyByDocContract.Model createModule() {
        return new InterestsUseApplyByDocModel();
    }

    @Override
    public void saveRightsUseRecord(SaveRightsUseBean bean) {
        mModel.saveRightsUseRecord(bean,new CallBackAdapter(){
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()){
                    getView().saveRightsUseRecordSuccess(bean);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);
                if (isViewAttach()){
                    getView().saveRightsUseRecordFail(str);
                }
            }
        });



    }
}
