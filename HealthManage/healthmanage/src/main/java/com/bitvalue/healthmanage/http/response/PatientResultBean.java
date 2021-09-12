package com.bitvalue.healthmanage.http.response;

public class PatientResultBean {


    public UserInfoDTO userInfo;
    public String suggestion;
    public String typeName;
    public String diagnosis;
    public long beginTime;
    public int type;
    public String userId;

    public static class UserInfoDTO {
        public String userSex;
        public int accountId;
        public boolean isDefault;
        public String phone;
        public String identificationNo;
        public String identificationType;
        public String userType;
        public String userName;
        public String relationship;
        public int userId;
        public int userAge;
    }
}
