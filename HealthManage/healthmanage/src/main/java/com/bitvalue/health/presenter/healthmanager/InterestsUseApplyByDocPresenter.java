package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.requestbean.CallRequest;
import com.bitvalue.health.api.requestbean.QuickReplyRequest;
import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.api.responsebean.CallResultBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.CallBackAdapter;
import com.bitvalue.health.contract.healthmanagercontract.InterestsUseApplyByDocContract;
import com.bitvalue.health.model.healthmanagermodel.InterestsUseApplyByDocModel;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/22
 */
public class InterestsUseApplyByDocPresenter extends BasePresenter<InterestsUseApplyByDocContract.View, InterestsUseApplyByDocContract.Model> implements InterestsUseApplyByDocContract.Presenter {
    @Override
    protected InterestsUseApplyByDocContract.Model createModule() {
        return new InterestsUseApplyByDocModel();
    }

    @Override
    public void saveRightsUseRecord(SaveRightsUseBean bean) {
        if (mModel != null) {
            mModel.saveRightsUseRecord(bean, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().saveRightsUseRecordSuccess(bean);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().saveRightsUseRecordFail(str);
                    }
                }
            });
        }


    }

    @Override
    public void sendsummary_result(DataReViewRecordResponse request) {
        if (mModel != null) {
            mModel.sendsummary_result(request, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().sendsummary_resultSuucess((DataReViewRecordResponse) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().sendsummary_resultFail(str);
                    }
                }
            });
        }
    }

    @Override
    public void getsummary_resultList(String userId) {
        if (mModel != null) {
            mModel.getsummary_resultList(userId, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().getSummaryListSuucess((List<DataReViewRecordResponse>) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().getSummaryListFail(str);
                    }
                }
            });
        }
    }

    @Override
    public void saveCaseCommonWords(QuickReplyRequest request) {
        if (mModel != null) {
            mModel.saveCaseCommonWords(request, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().saveCaseCommonWordsSuccess((String) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().saveCaseCommonWordsFail(str);
                    }
                }
            });
        }
    }

    @Override
    public void callPhone(CallRequest callRequest) {
        if (mModel != null) {
            mModel.callPhone(callRequest, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().callSuccess((CallResultBean) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().callFail(str);
                    }


                }
            });
        }
    }
}
