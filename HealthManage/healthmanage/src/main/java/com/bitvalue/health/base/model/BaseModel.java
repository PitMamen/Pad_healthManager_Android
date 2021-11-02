package com.bitvalue.health.base.model;



import com.bitvalue.health.api.CommonService;
import com.bitvalue.health.net.NetEngine;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


/**
 * @author pxk on 2021/10/25
 */
public class BaseModel implements IModel {
    protected String TAG = this.getClass().getSimpleName();
    protected CommonService mApi;
    private Disposable disposable;
    private Observable mCompositeSubscription;

    public BaseModel() {
        this.mApi = NetEngine.getInstance().getService();
    }

    @Override
    public void unSubscribe() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void addSubscribe(Observable subscription) {
        if (disposable == null) {
            disposable = subscription.subscribe();
        }
        mCompositeSubscription.blockingSubscribe();
    }
}
