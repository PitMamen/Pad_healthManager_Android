package com.bitvalue.health.callback;


import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;

public interface Callback {

    void onFinish(int what);

    void onCurrenStripWithTotal(int curren, int total, int what, String name);

    /**
     * 网络请求成功但是接口处理失败（业务上的失败）
     * @param str
     * @param what
     */
    void onFailedLog(String str, int what);

    void onFailed(Object o,int what);

    void onSuccessLog(String str, int what);

    /**
     * 网络故障
     * @param erromessge
     * @param what
     */
    void onError(String erromessge, int what);

    /**
     * 网络请求成功并返回成功
     * @param o
     * @param what
     */
    void onSuccess(Object o, int what);

    void onExcuteCmd(int cmd, String content);

    void loginV2TIMSuccess(V2TIMConversationResult v2TIMConversationResult);

    void loginV2TIMFail(int code,String desc);

    void IMloginSuccess(Object o);

    void IMloginFail(String module, final int code, final String desc);

    void IMgetNewMessage(V2TIMMessage v2TIMMessage);
}
