package com.bitvalue.health.model.healthmanagermodel;

import android.util.Log;

import com.bitvalue.health.api.requestbean.FinshMidRequestBean;
import com.bitvalue.health.api.responsebean.MyRightBean;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.RightApplyUseContract;
import com.bitvalue.health.util.EmptyUtil;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 02/21
 */
public class RightApplyUseModel extends BaseModel implements RightApplyUseContract.Model {
    @Override
    public void getMyRight(int goodsId, String id, String userId, Callback callback) {
        if (!EmptyUtil.isEmpty(userId)) {
            mApi.queryMyRights(userId,id).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    Log.e(TAG, "getMyRight: "+result.toString() );
                    if (result.getCode() == 0) {
                        List<MyRightBean> myRightBeanList = result.getData();
                        if (!EmptyUtil.isEmpty(myRightBeanList)) {
                            callback.onSuccess(myRightBeanList, 1000);
                        } else {
                            callback.onSuccess(null, 1000);
                        }
                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }
            }, error -> {
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }

    @Override
    public void queryRightsRecord(int pageNo, int pageSize, int rightsId, String userId, Callback callback) {
        if (!EmptyUtil.isEmpty(userId)) {
            mApi.queryRightsUserRecord(pageNo, pageSize, userId,rightsId,"").subscribeOn(Schedulers.io()).subscribe(result -> {
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
                Log.e(TAG, "queryRightsRecord: "+error.getMessage() );
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }

    @Override
    public void finishMidRequest(FinshMidRequestBean finshMidRequestBean, Callback callback) {
        if (!EmptyUtil.isEmpty(finshMidRequestBean)) {
            mApi.finishMidRequest(finshMidRequestBean).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        FinshMidRequestBean finshMidResponse = result.getData();
                        if (!EmptyUtil.isEmpty(finshMidResponse)) {
                            callback.onSuccess(finshMidResponse, 1000);
                        }
                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }
            }, error -> {
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }

    @Override
    public void getDocList(String departId, Callback callback) {
        mApi.getDoctorList(departId).subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        Log.e(TAG, "getDocList: "+result.getData().size() );
                        callback.onSuccess(result.getData(), 1000);
                    }
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            }
        }, error -> {
            Log.e(TAG, "getDocList: " + error.getMessage());
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }


    @Override
    public void getDataReviewRecord(String tradedID, String userID, Callback callback) {
        mApi.getDataReviewRecord(tradedID, userID,"资料审核").subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                Log.e(TAG, "getDataReviewRecord: " + result.toString());
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        callback.onSuccess(result.getData(), 1000);
                    }
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            }
        }, error -> {
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }


}
