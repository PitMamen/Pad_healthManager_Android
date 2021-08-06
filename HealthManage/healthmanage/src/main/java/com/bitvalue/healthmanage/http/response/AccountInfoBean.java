package com.bitvalue.healthmanage.http.response;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 用户信息
 */
public final class AccountInfoBean {
    public int accountId;
    public String userName;
    public String phone;
    public String roleName;
    public Object openId;

    public UserInfo user;

    public static class UserInfo{
        public int accountId;
        public int userId;
        public String userName;
        public String userSex;
        public String userType;
        public String phone;
        public int hospitalId;
        public String hospitalName;
        public int departmentId;
        public String departmentName;
        public String professionalTitle;
        public int userAge;
        public String userSig;
    }
}