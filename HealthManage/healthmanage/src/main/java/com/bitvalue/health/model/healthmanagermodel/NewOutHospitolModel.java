package com.bitvalue.health.model.healthmanagermodel;

import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.NewOutHospitolContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data :
 */
public class NewOutHospitolModel extends BaseModel implements NewOutHospitolContract.Model {
    @Override
    public void getAllLeaveHospitolPatients(RequestNewLeaveBean requestNewLeaveBean, Callback callback) {
        if (null != requestNewLeaveBean) {
            mApi.getAllNewLeaveHospitolPatients(requestNewLeaveBean).subscribeOn(Schedulers.io()).subscribe(listApiResult -> {
                if (!EmptyUtil.isEmpty(listApiResult)) {
                    if (listApiResult.getCode() == 0) {
                        if (!EmptyUtil.isEmpty(listApiResult.getData().getRows())){
                            callback.onSuccess(listApiResult.getData().getRows(),1000);
                        }else {
                            callback.onFailedLog("未加载到新出院患者!",1001);
                        }
                    } else {
                        callback.onFailedLog(listApiResult.getMessage(), 1001);
                    }

                } else {

                    callback.onFailedLog("未加载到新出院患者", 1001);
                }
            }, error -> {
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }
    }



    @Override
    public void qryPatientByName(RequestNewLeaveBean requestNewLeaveBean, Callback callback) {
        if (null != requestNewLeaveBean) {
            mApi.qryPatientList(requestNewLeaveBean).subscribeOn(Schedulers.io()).subscribe(listApiResult -> {
                if (!EmptyUtil.isEmpty(listApiResult)) {
                    if (listApiResult.getCode() == 0) {
                        NewLeaveBean resultData = listApiResult.getData();
                        if (!EmptyUtil.isEmpty(listApiResult.getData().getRows())){
                            callback.onSuccess(listApiResult.getData().getRows(),1000);
                        }else {
                            callback.onFailedLog("未查询到患者",1001);
                        }

                    } else {
                        callback.onFailedLog(listApiResult.getMessage(), 1001);
                    }

                } else {

                    callback.onFailedLog("未查询到患者", 1001);
                }
            }, error -> {
                callback.onFailedLog(error.getMessage(), 1001);
            });
        }

    }



}
