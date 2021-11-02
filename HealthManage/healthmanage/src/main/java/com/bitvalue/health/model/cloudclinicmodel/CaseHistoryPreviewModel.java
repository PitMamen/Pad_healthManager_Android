package com.bitvalue.health.model.cloudclinicmodel;

import com.bitvalue.health.api.requestbean.GetHistoryApi;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.cloudcliniccontract.CaseHistoryPreviewContract;
import com.bitvalue.health.util.EmptyUtil;

import java.util.ArrayList;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 10/29
 */
public class CaseHistoryPreviewModel extends BaseModel implements CaseHistoryPreviewContract.CaseHistoryFreViewModel {
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
}
