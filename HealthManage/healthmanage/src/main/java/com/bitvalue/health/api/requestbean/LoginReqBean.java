package com.bitvalue.health.api.requestbean;

/***
 *登录请求实体
 *
 * 用户名
 * 密码
 * 登录类型
 */
public class LoginReqBean {
    private String userName;
    private String password;
    private int loginType;

    /***
     *
     * @param userName
     * @param password
     * @param loginType
     */
    public LoginReqBean(String userName, String password, int loginType) {
        this.userName = userName;
        this.password = password;
        this.loginType = loginType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }
}
