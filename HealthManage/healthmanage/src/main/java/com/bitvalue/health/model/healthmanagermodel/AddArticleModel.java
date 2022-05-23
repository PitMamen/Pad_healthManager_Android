package com.bitvalue.health.model.healthmanagermodel;

import android.util.Log;

import com.bitvalue.health.base.model.BaseModel;
import com.bitvalue.health.callback.Callback;
import com.bitvalue.health.contract.healthmanagercontract.AddArticleContract;
import com.bitvalue.health.util.EmptyUtil;

import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data :
 */
public class AddArticleModel extends BaseModel implements AddArticleContract.Model {
    @Override
    public void getUsefulArticle(int pageSize, int startPage, String deptCode, Callback callback) {
        mApi.getUsefullArticle(pageSize, startPage, deptCode,"2").subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        callback.onSuccess(result.getData().getList(), 1000);
                    } else {
                        //null
                        Log.e(TAG, "getUsefulArticle == null  ");
                    }
                } else {
                    callback.onFailedLog(result.getMessage(), 1001);
                }
            }
        }, error -> {
            Log.e(TAG, "getUsefulArticle: "+error.getMessage() );
            callback.onFailedLog(error.getMessage(), 1001);
        });
    }

    @Override
    public void qryArticleByTitle(int pageSize, int start, String title, Callback callback) {
        mApi.qryarticleByTitle(pageSize, start, title,"2").subscribeOn(Schedulers.io()).subscribe(result -> {
            if (!EmptyUtil.isEmpty(result)) {
                if (result.getCode() == 0) {
                    if (!EmptyUtil.isEmpty(result.getData())) {
                        callback.onSuccess(result.getData(), 1000);
                    } else {
                        Log.e(TAG, "qryArticleByTitle == null ");
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
