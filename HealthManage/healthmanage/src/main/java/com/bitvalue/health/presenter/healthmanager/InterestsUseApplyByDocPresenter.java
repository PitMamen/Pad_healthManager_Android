package com.bitvalue.health.presenter.healthmanager;

import com.bitvalue.health.api.requestbean.CallRequest;
import com.bitvalue.health.api.requestbean.QuickReplyRequest;
import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.api.requestbean.SummaryBean;
import com.bitvalue.health.api.requestbean.UpdateRightsRequestTimeRequestBean;
import com.bitvalue.health.api.responsebean.CallResultBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.QueryRightsRecordBean;
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
    public void sendsummary_result(SummaryBean request) {
        if (mModel != null) {
            mModel.sendsummary_result(request, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().sendsummary_resultSuucess((boolean) o);
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
                        getView().getSummaryListSuucess((List<SummaryBean>) o);
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
                        getView().callSuccess((boolean) o);
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


    @Override
    public void queryRightsRecord(int pageNo, int pageSize, int rightsId, String userId, String id) {
        if (mModel != null) {
            mModel.queryRightsRecord(pageNo, pageSize, rightsId, userId, id, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().queryRightsRecordSuccess((QueryRightsRecordBean) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().queryRightsRecordFail(str);
                    }

                }
            });
        }


    }

    @Override
    public void updateRightsRequestTime(UpdateRightsRequestTimeRequestBean requestTimeRequestBean) {
        if (mModel != null) {
            mModel.updateRightsRequestTime(requestTimeRequestBean, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().updateRightsRequestTimeSuccess((Boolean) o);
                    }
                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().updateRightsRequestTimeFail(str);
                    }
                }
            });
        }
    }

    @Override
    public void qryRightsUserLog(String tradedId, String userId, String dealType) {
        if (mModel != null) {
            mModel.qryRightsUserLog(tradedId, userId, dealType, new CallBackAdapter() {
                @Override
                public void onSuccess(Object o, int what) {
                    super.onSuccess(o, what);
                    if (isViewAttach()) {
                        getView().qryRightsUserLogSuccess((List<DataReViewRecordResponse>) o);
                    }

                }

                @Override
                public void onFailedLog(String str, int what) {
                    super.onFailedLog(str, what);
                    if (isViewAttach()) {
                        getView().qryRightsUserLogFail(str);
                    }

                }
            });
        }
    }


}
