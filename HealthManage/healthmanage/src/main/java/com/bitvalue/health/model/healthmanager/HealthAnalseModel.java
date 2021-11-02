package com.bitvalue.health.model.healthmanager;

import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.HealthAnalseContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data :
 */
public class HealthAnalseModel extends BaseModel implements HealthAnalseContract.Model {
    @Override
    public void commitAnalse(SaveAnalyseApi requestBean, Callback callback) {
        mApi.commitHealthAnaly(requestBean).subscribeOn(Schedulers.io()).subscribe(result->{
            if (!EmptyUtil.isEmpty(result)){
                if (result.getCode()==0){
                    if (!EmptyUtil.isEmpty(result.getData())){
                        callback.onSuccess(result.getData(),1000);
                    }else {
                        callback.onFailedLog(result.getMessage(),1001);
                    }
                }else {
                    callback.onFailedLog(result.getMessage(),1001);
                }
            }
        },error->{
            callback.onFailedLog(error.getMessage(),1001);
        });
    }
}
