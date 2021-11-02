package com.bitvalue.health.callback;

import android.util.Log;


import com.bitvalue.health.base.presenter.BasePresenter;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

import java.lang.ref.WeakReference;


public class CallBackAdapter implements Callback {

    private WeakReference<BasePresenter> basePresenterWeakReference;

    public CallBackAdapter(){ }

    public CallBackAdapter(BasePresenter basePresenter){
        basePresenterWeakReference = new WeakReference<>(basePresenter);
    }

    @Override
    public void onFinish(int what) {
        Log.i("onFinish", "----完成----" + what);
    }

    @Override
    public void onCurrenStripWithTotal(int curren, int total, int what, String name) {
        Log.i("onCurrenStripWithTotal", "----curren----" + curren + "------total-----" + total + "  ---what--" + what+"  name--"+name);
    }

    @Override
    public void onFailedLog(String str, int what) {
        Log.i("onFailedLog", "----" + str + "-----" + what);
    }


    @Override
    public void onSuccessLog(String str, int what) {
        Log.i("onSuccessLog", "----" + str + "-----" + what);
    }

    @Override
    public void onError(String erromessge, int what) {
        Log.e("CallBackAdapter", "onError: " + erromessge);
        if(basePresenterWeakReference != null && basePresenterWeakReference.get() != null){
            basePresenterWeakReference.get().onNetError();
        }
    }

    @Override
    public void onSuccess(Object o, int what) {

    }

    @Override
    public void onExcuteCmd(int cmd, String content) {

    }

    @Override
    public void loginV2TIMSuccess(V2TIMConversationResult v2TIMConversationResult) {

    }


    @Override
    public void loginV2TIMFail(int code, String desc) {

    }

    @Override
    public void IMloginSuccess(Object o) {

    }

    @Override
    public void IMloginFail(String module, int code, String desc) {

    }

    @Override
    public void IMgetNewMessage(V2TIMMessage v2TIMMessage) {

    }
}
