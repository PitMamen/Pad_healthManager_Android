package com.bitvalue.health.api.responsebean;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 登录返回
 */
public final class LoginBean {

    private String jwt;//相当于token
    private String userSig;//腾讯云的登录密码，后台生成
    private AccountInfoBean account;

    public String getJwt() {
        return jwt;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public AccountInfoBean getAccount() {
        return account;
    }

    public void setAccount(AccountInfoBean account) {
        this.account = account;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public AccountInfoBean getUser() {
        return account;
    }

    public void setUser(AccountInfoBean account) {
        this.account = account;
    }

    public String getToken() {
        return jwt;
    }
}