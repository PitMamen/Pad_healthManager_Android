package com.bitvalue.sdk.base.util.net;

/**
 * 网络请求返回的json对象
 */
public class Result<T> {
    public T data;
    public int code;
    public String message;

    public boolean success;
}
