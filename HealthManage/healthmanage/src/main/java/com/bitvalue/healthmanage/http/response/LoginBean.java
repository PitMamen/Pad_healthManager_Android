package com.bitvalue.healthmanage.http.response;

import com.tencent.imsdk.relationship.UserInfo;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 登录返回
 */
public final class LoginBean {

    private String jwt;//相当于token
    private UserInfoBean user;

    public String getToken() {
        return jwt;
    }
}