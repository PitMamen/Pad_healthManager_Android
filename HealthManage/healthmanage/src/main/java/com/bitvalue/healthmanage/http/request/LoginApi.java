package com.bitvalue.healthmanage.http.request;

import com.hjq.http.config.IRequestApi;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 用户登录
 */
public final class LoginApi implements IRequestApi {

    @Override
    public String getApi() {
        return "/login";
    }

    /** 手机号 */
    private String username;
    /** 登录密码 */
    private String password;
    private int loginType = 1;//pad写死1

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public LoginApi setUsername(String username) {
        this.username = username;
        return this;
    }

    public LoginApi setPassword(String password) {
        this.password = password;
        return this;
    }
}