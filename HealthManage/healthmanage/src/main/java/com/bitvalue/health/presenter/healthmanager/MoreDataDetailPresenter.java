package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
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
public class MoreDataDetailPresenter extends BasePresenter<MoreDataDetailContract.View, MoreDataDetailContract.Model> implements MoreDataDetailContract.Presenter {
    @Override
    protected MoreDataDetailContract.Model createModule() {
        return new MoreDataDetailModel();
    }

    @Override
    public void qryUserLocalVisit(UserLocalVisitBean visitBean) {
        if (mModel != null) {
            mModel.qryUserLocalVisit(visitBean, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().qryUserVisitSuccess((List<TaskDetailBean>) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);

                    if (isViewAttach()) {
                        getView().qryUserVisitFail(str);
                    }
                }
            });
        }

    }

    @Override
    public void getDataReviewRecord(String tradedID, String userID) {
        if (mModel != null) {
            mModel.getDataReviewRecord(tradedID, userID, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().getDataReviewRecordSuccess((List<DataReViewRecordResponse>) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().getDataReviewRecordFail(str);
                    }
                }
            });
        }
    }

    @Override
    public void saveDataReviewRecord(DataReViewRecordResponse request) {
        if (mModel != null) {
            mModel.saveDataReviewRecord(request, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().saveDataReviewRecordSuucess((DataReViewRecordResponse) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().saveDataReviewRecordFail(str);
                    }
                }
            });
        }
    }
}
