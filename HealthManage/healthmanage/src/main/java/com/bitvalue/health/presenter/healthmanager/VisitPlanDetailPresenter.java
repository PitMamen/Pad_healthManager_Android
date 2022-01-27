package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.VisitPlanDetailContract;
import com.bitvalue.health.model.planmodel.VisitPlanDetailModel;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 01/07
 */
public class VisitPlanDetailPresenter extends BasePresenter<VisitPlanDetailContract.View,VisitPlanDetailContract.Model> implements VisitPlanDetailContract.Presenter {
    @Override
    protected VisitPlanDetailContract.Model createModule() {
        return new VisitPlanDetailModel();
    }

    @Override
    public void qryUserLocalVisit(UserLocalVisitBean visitBean) {
        mModel.qryUserLocalVisit(visitBean,new CallBackAdapter(){
            @Override
            public void onSuccess(Object o, int what) {
                super.onSuccess(o, what);
                if (isViewAttach()){
                    getView().qryUserVisitSuccess((List<String>) o);
                }
            }

            @Override
            public void onFailedLog(String str, int what) {
                super.onFailedLog(str, what);

                if (isViewAttach()){
                    getView().qryUserVisitFail(str);
                }
            }
        });
    }
}
