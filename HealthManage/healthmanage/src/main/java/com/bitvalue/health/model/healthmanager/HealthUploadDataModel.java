package com.bitvalue.health.model.healthmanager;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.HealthUploadDataContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data :
 */
public class HealthUploadDataModel extends BaseModel implements HealthUploadDataContract.Model {
    @Override
    public void queryHealthPlanContent(String contentId, String planType, String userId, Callback callback) {
        mApi.queryHealthPlanContentByUpload(contentId,planType,userId).subscribeOn(Schedulers.io()).subscribe(result->{
            if (!EmptyUtil.isEmpty(result)){
                if (result.getCode()==0){
                    if (!EmptyUtil.isEmpty(result.getData())){
                        callback.onSuccess(result.getData(),1000);
                    }else {
                        callback.onFailedLog(result.getMessage(),1001);
                    }
                }
            }
        },error->{
            callback.onFailedLog(error.getMessage(),1001);
        });
    }
}
