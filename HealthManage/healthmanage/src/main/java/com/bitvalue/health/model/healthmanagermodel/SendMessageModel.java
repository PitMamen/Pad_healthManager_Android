package com.bitvalue.health.model.healthmanagermodel;

import android.annotation.SuppressLint;

import com.bitvalue.health.api.requestbean.SendUserRemind;
import com.bitvalue.health.api.responsebean.SaveAnalyseApi;
import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.SendMessageContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SendMessageModel extends BaseModel implements SendMessageContract.Model {
    @Override
    public void unSubscribe() {

    }

    @Override
    public void addSubscribe(Observable subscription) {

    }

    @SuppressLint("CheckResult")
    @Override
    public void sendUserEevaluate(SaveAnalyseApi requestBean, Callback callback) {
        mApi.commitHealthAnaly(requestBean)
                .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())//主线程接收
                    .subscribe(result->{
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
    @SuppressLint("CheckResult")
    @Override
    public void sendUserRemind(SendUserRemind sendUserRemind, Callback callback) {
        mApi.sendUserRemind(sendUserRemind)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//主线程接收
                .subscribe(result->{
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
