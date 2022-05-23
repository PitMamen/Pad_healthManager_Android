package com.bitvalue.health.model.healthmanagermodel;

import android.util.Log;

import com.bitvalue.health.api.requestbean.CallRequest;
import com.bitvalue.health.api.requestbean.QuickReplyRequest;
import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.api.requestbean.SummaryBean;
import com.bitvalue.health.api.requestbean.UpdateRightsRequestTimeRequestBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.InterestsUseApplyByDocContract;
import com.bitvalue.health.util.EmptyUtil;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 02/22
 */
public class InterestsUseApplyByDocModel extends BaseModel implements InterestsUseApplyByDocContract.Model {
    @Override
    public void saveRightsUseRecord(SaveRightsUseBean bean, Callback callback) {
        if (!EmptyUtil.isEmpty(bean)) {
            mApi.saveRightsRecord(bean).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        if (!EmptyUtil.isEmpty(result.getData())) {
                            callback.onSuccess(result.getData(), 1000);
                        }
                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }
            }, error -> {
                Log.e(TAG, "结束问诊出错: " + error.getMessage());
                callback.onFailedLog("操作失败", 1001);
            });
        }
    }

    @Override
    public void sendsummary_result(SummaryBean request, Callback callback) {
        mApi.saveSummary(request).subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                Log.e(TAG, "sendsummary_result: " + result.toString());
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        callback.onSuccess(result.getData(), 1000);
                    }
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }

            }
        }, error -> {
            Log.e(TAG, "sendsummary_result111: " + error.getMessage());
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }

    @Override
    public void getsummary_resultList(String userId, Callback callback) {
        mApi.qryRightsUserSummary(userId).subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    callback.onSuccess(result.getData(), 1000);
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            }
        }, error -> {
            Log.e(TAG, "请求获取问诊小结出错: " + error.getMessage());
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }

    @Override
    public void saveCaseCommonWords(QuickReplyRequest request, Callback callback) {
        mApi.modify_createCommonWords(request).subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
//                    QuickReplyRequest resultBean = result.getData();
                    callback.onSuccess(result.getMessage(), 1000);
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            }
        }, error -> {
            Log.e(TAG, "saveCaseCommonWords error: " + error.getMessage());
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }

    @Override
    public void callPhone(CallRequest callRequest, Callback callback) {
        mApi.callPhone(callRequest).subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                Log.e(TAG, "callPhone: " + result.toString());
                if (result.getCode() == 0 && result.getSuccess()) {
                    callback.onSuccess(result.getSuccess(), 1000);
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            }
        }, error -> {
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }


    @Override
    public void queryRightsRecord(int pageNo, int pageSize, int rightsId, String userId, String id, Callback callback) {
        if (!EmptyUtil.isEmpty(userId)) {
            mApi.queryRightsUserRecord(pageNo, pageSize, userId, rightsId, id).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        if (!EmptyUtil.isEmpty(result.getData())) {
                            callback.onSuccess(result.getData(), 1000);
                        } else {
                            callback.onSuccess(null, 1000);
                        }

                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }
            }, error -> {
                Log.e(TAG, "queryRightsRecord: " + error.getMessage());
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }

    @Override
    public void updateRightsRequestTime(UpdateRightsRequestTimeRequestBean requestTimeRequestBean, Callback callback) {
        if (!EmptyUtil.isEmpty(requestTimeRequestBean)) {
            mApi.updateRightsRequestTime(requestTimeRequestBean).subscribeOn(Schedulers.io()).subscribe(reuslt -> {
                if (!EmptyUtil.isEmpty(reuslt)) {
                    if (reuslt.getCode() == 0) {
                        callback.onSuccess(reuslt.getData(), 1000);
                    } else {
                        callback.onFailedLog(reuslt.getMessage(), 1001);
                    }
                }
            }, error -> {
                Log.e(TAG, "updateRightsRequestTime: " + error.getMessage());
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }

    @Override
    public void qryRightsUserLog(String tradedId, String userId, String dealType, Callback callback) {
        if (!EmptyUtil.isEmpty(tradedId) && !EmptyUtil.isEmpty(userId)) {
            mApi.qryRightsUserLog(tradedId, userId, dealType).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    Log.e(TAG, "qryRightsUserLog: " + result.toString());
                    if (result.getCode() == 0) {
                        callback.onSuccess(result.getData(), 1000);
                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }
            }, error -> {
                Log.e(TAG, "qryRightsUserLogerror: " + error.getMessage());
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }
}
