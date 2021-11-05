package com.bitvalue.health.model.settingmodel;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.settingcontract.AddQuestionContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data :
 */
public class AddQuestionModel extends BaseModel implements AddQuestionContract.Model {
    @Override
    public void qryQuestByKeyWord(int pageSize, int start, String keyWord, Callback callback) {
        mApi.qryQuestByKeyWord(pageSize,start,keyWord).subscribeOn(Schedulers.io()).subscribe(result->{
            if (!EmptyUtil.isEmpty(result)){
                if (result.getCode()==0){
                    if (!EmptyUtil.isEmpty(result.getData())){
                        callback.onSuccess(result.getData(),1000);
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
