package com.bitvalue.health.model.healthmanagermodel;

import android.util.Log;

import com.bitvalue.health.api.requestbean.AllocatedPatientRequest;
import com.bitvalue.health.api.responsebean.InpatientBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.PatientReportContract;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IMEventListener;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data :
 */
public class PatientReportModel extends BaseModel implements PatientReportContract.Model {

    @Override
    public void qryAllocatedPatienList(AllocatedPatientRequest allocatedPatientRequest, Callback callback) {
        if (allocatedPatientRequest != null) {
            mApi.qryallAllocatedPatientList(allocatedPatientRequest).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    Log.e(TAG, "qryAllocatedPatienList: " + result.getData().getRows());
                    if (result.getCode() == 0) {
                        callback.onSuccess(result.getData().getRows(), 1000);

                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }

                }
            }, error -> {
                Log.e(TAG, "qryAllocatedPatienList: " + error.getMessage());
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }

    @Override
    public void qryByNameAllocatedPatienList(AllocatedPatientRequest allocatedPatientRequest, Callback callback) {
        if (allocatedPatientRequest != null) {
            mApi.qryallAllocatedPatientList(allocatedPatientRequest).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    Log.e(TAG, "qryAllocatedPatienList: " + result.getData().getRows());
                    if (result.getCode() == 0) {
                        if (!EmptyUtil.isEmpty(result.getData().getRows())) {
                            callback.onSuccess(result.getData().getRows(), 1000);
                        } else {
                            callback.onFailedLog("未查询到患者", 1001);
                        }

                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }

                }
            }, error -> {
                Log.e(TAG, "qryAllocatedPatienList: " + error.getMessage());
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }


    }

    @Override
    public void qryUnregisterPatienList(AllocatedPatientRequest allocatedPatientRequest, Callback callback) {
        if (allocatedPatientRequest != null) {
            mApi.qryallAllocatedPatientList(allocatedPatientRequest).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    Log.e(TAG, "qryAllocatedPatienList: " + result.getData().getRows());
                    if (result.getCode() == 0) {
                        if (!EmptyUtil.isEmpty(result.getData().getRows())) {
                            callback.onSuccess(result.getData().getRows(), 1000);
                        } else {
                            callback.onFailedLog("未查询到患者", 1001);
                        }

                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }

                }
            }, error -> {
                Log.e(TAG, "qryAllocatedPatienList: " + error.getMessage());
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }

    @Override
    public void getInpartientList(String depatmentID, Callback callback) {
        mApi.getInpatientAreaList(depatmentID).subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        List<InpatientBean> data = (List<InpatientBean>) result.getData();
                        callback.onSuccess(data, 1000);
                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            } else {

                callback.onFailedLog(result.getMessage(), 1001);
            }
        }, error -> {
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }
}
