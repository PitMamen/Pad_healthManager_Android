package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.MoreDataDetailContract;
import com.bitvalue.health.model.planmodel.MoreDataDetailModel;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/11
 */
public class MoreDataDetailPresenter extends BasePresenter<MoreDataDetailContract.View,MoreDataDetailContract.Model> implements MoreDataDetailContract.Presenter {
    @Override
    protected MoreDataDetailContract.Model createModule() {
        return new MoreDataDetailModel();
    }

    @Override
    public void qryUserLocalVisit(UserLocalVisitBean visitBean) {
            mModel.qryUserLocalVisit(visitBean,new CallBackAdapter(){
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()){
                        getView().qryUserVisitSuccess((List<TaskDetailBean>) o);
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
