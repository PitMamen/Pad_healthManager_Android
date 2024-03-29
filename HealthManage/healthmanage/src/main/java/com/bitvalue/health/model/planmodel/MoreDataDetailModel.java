package com.bitvalue.health.model.planmodel;

import android.util.Log;

import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.MoreDataDetailContract;
import com.bitvalue.health.util.EmptyUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 02/11
 */
public class MoreDataDetailModel extends BaseModel implements MoreDataDetailContract.Model {

    /**
     * 获取患者基本信息
     * @param userId
     * @param callback
     */
    @Override
    public void getPatientBaseInfo(int userId, Callback callback) {
        mApi.getPatientBaseInfo(userId).subscribeOn(Schedulers.io()).subscribe(result -> {
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
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }

    @Override
    public void qryUserLocalVisit(UserLocalVisitBean visitBean, Callback callback) {
        mApi.qryUserLocalVisit(visitBean).subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        List<TaskDetailBean> listBean = result.getData();
//                        List<String> imagesDTOList = new ArrayList<>();
//                        for (int i = 0; i < listBean.size(); i++) {
//                            for (int j = 0; j <listBean.get(i).getHealthImages().size() ; j++) {
//                                imagesDTOList.add(listBean.get(i).getHealthImages().get(j).getFileUrl());
//                            }
//                        }
                        callback.onSuccess(listBean, 1000);
                    } else {
                        callback.onSuccess(null, 1000);
                    }
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            } else {
                Log.e(TAG, "null=========");
            }
        }, error -> {
            Log.e(TAG, "请求失败: " + error.getMessage());
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


    //保存审核记录
    @Override
    public void saveDataReviewRecord(DataReViewRecordResponse request, Callback callback) {
        mApi.saveDataReviewRecord(request).subscribeOn(Schedulers.io()).subscribe(result -> {
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
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }
}
