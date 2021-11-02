package com.bitvalue.health.model.cloudclinicmodel;

import com.bitvalue.health.api.requestbean.GetHistoryApi;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.cloudcliniccontract.WriterCaseHistoryContract;
import com.bitvalue.health.util.EmptyUtil;

import java.util.ArrayList;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 10/29
 */
public class WriterCaseHistoryModel extends BaseModel implements WriterCaseHistoryContract.WriterCaseHistoryModel {
    @Override
    public void qryUserMedicalCase(GetHistoryApi qryMedicalCase, Callback callback) {
        if (!EmptyUtil.isEmpty(qryMedicalCase)) {
            mApi.qryUserMedicalCase(qryMedicalCase).subscribeOn(Schedulers.io()).subscribe(r -> {
                if (!EmptyUtil.isEmpty(r)) {
                    if (r.getCode() == 0) {
                        if (!EmptyUtil.isEmpty(r.getData())) {
                            ArrayList<SaveCaseApi> caseApiArrayList = r.getData();
                            callback.onSuccess(caseApiArrayList, 1000);
                        }
                    } else {
                        callback.onFailedLog(r.getMessage(), 1001);
                    }
                }
            }, error -> {
                callback.onFailedLog(error.getMessage(), 1001);
            });

        }


    }

    @Override
    public void commitCaseHistory(SaveCaseApi caseHistorybean, Callback callback) {
        if (!EmptyUtil.isEmpty(caseHistorybean)) {
            mApi.commitCaseHistory(caseHistorybean).subscribeOn(Schedulers.io()).subscribe(result -> {
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        if (!EmptyUtil.isEmpty(result.getData())) {
                            callback.onSuccess(result.getData(), 1000);
                        }
                    } else {
                        callback.onFailedLog(result.getMessage(), 1001);
                    }
                }
            },error->{
                callback.onFailedLog(error.getMessage(),1001);
            });

        }
    }
}
