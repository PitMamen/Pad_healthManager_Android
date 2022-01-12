package com.bitvalue.health.model.mytodolist;

import android.util.Log;

import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.mytodolistcontact.MyToDoListContact;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data :
 */
public class MyToDoListModel extends BaseModel implements MyToDoListContact.MyToDoListModel {
    @Override
    public void qryPatientList(RequestNewLeaveBean requestNewLeaveBean, Callback callback) {
        if (null != requestNewLeaveBean) {
            mApi.qryPatientList(requestNewLeaveBean).subscribeOn(Schedulers.io()).subscribe(listApiResult -> {
                if (!EmptyUtil.isEmpty(listApiResult)) {
                    if (listApiResult.getCode() == 0) {
                        NewLeaveBean newLeaveBeanList = listApiResult.getData();
                        Log.e(TAG, "qryPatientList: " + newLeaveBeanList);
                        if (!EmptyUtil.isEmpty(listApiResult.getData().getRows())){
                            callback.onSuccess(listApiResult.getData().getRows(),1000);
                        }else {
                            callback.onFailedLog("未加载到患者",1001);
                        }
                    } else {
                        callback.onFailedLog(listApiResult.getMessage(), 1001);
                    }

                } else {

                    callback.onFailedLog("listApiResult = null", 1001);
                }
            }, error -> {
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }

    @Override
    public void qryWaitoutList(RequestNewLeaveBean requestNewLeaveBean, Callback callback) {
        if (null != requestNewLeaveBean) {
            mApi.qryPatientList(requestNewLeaveBean).subscribeOn(Schedulers.io()).subscribe(listApiResult -> {
                if (!EmptyUtil.isEmpty(listApiResult)) {
                    if (listApiResult.getCode() == 0) {
                        NewLeaveBean newLeaveBeanList = listApiResult.getData();
                        Log.e(TAG, "qryPatientList: " + newLeaveBeanList);
                        if (!EmptyUtil.isEmpty(listApiResult.getData().getRows())){
                            callback.onSuccess(listApiResult.getData().getRows(),1000);
                        }else {
                            callback.onFailedLog("未加载到患者",1001);
                        }
                    } else {
                        callback.onFailedLog(listApiResult.getMessage(), 1001);
                    }

                } else {

                    callback.onFailedLog("listApiResult = null", 1001);
                }
            }, error -> {
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }
}