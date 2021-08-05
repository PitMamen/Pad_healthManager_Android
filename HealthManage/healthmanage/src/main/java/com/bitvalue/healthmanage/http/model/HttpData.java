package com.bitvalue.healthmanage.http.model;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 统一接口数据结构
 */
public class HttpData<T> {

    /** 返回码
     *
     * 0 正常
     *  10000 参数错误
     *  10001 认证失败
     *  10002 参数缺失
     *  10003 用户不存在
     *  10004 密码错误
     *  20000 系统错误
     *  30000 没有授权
     * */
    private int code;
    /** 提示语 */
    private String msg;
    public String message;
    /** 数据 */
    private T data;

    private boolean success;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}