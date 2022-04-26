package com.bitvalue.health.api.responsebean;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/12/07
 * desc   : 用户信息
 */
public final class AccountInfoBean {
    public int accountId;
    public String userName;
    public String phone;
    public String roleName;
    public Object openId;
    public String imAppId;

    public UserInfo user;

    public static class UserInfo {
        public int accountId;
        public int userId;
        public String userName;
        public String userSex;
        public String userType;
        public String phone;
        public int hospitalId;
        public String hospitalName;
        public int departmentId;
        public String departmentName;//科室名称
        public String professionalTitle;//职级
        public int userAge;
        public String userSig;

        public String identificationType;
        public String identificationNo;
        public String departmentCode;
        public String hospitalCode;
        public String doctorBrief;
        public String avatarUrl;

        /**
         *
         *     				"accountId": 61,
         *     				"userId": 57,
         *     				"phone": null,
         *     				"userName": "符医生",
         *     				"userSex": "男",
         *     				"userType": "DOCTOR",
         *     				"identificationType": "01",
         *     				"identificationNo": "430522199805162356",
         *     				"professionalTitle": "主任医师",
         *     				"departmentId": 3,
         *     				"departmentCode": null,
         *     				"departmentName": "血液内科",
         *     				"hospitalCode": null,
         *     				"hospitalName": "中南大学湘雅二医院",
         *     				"doctorBrief": null,
         *     				"avatarUrl": null,
         *     				"userAge": 23,
         *     				"userSig": "eJyrVgrxCdZLrSjILEpVsjI0sDAw0AELlaUWKVkpGekZKEH4xSnZiQUFmSlARSYGBqYm5kYm5hCZzJTUvJLMtEywBlNzmPrMdCC3qrIkozwgoLzKsiK0stzMJCzA1SepyiTIPygiwM*owNvYNUU7XNvMv8TAwhaqsSQzF*QSM2NDE0MLE0PTWgAYkC8O"
         *
         */

        @Override
        public String toString() {
            return "UserInfo{" +
                    "accountId=" + accountId +
                    ", userId=" + userId +
                    ", userName='" + userName + '\'' +
                    ", userSex='" + userSex + '\'' +
                    ", userType='" + userType + '\'' +
                    ", phone='" + phone + '\'' +
                    ", hospitalId=" + hospitalId +
                    ", hospitalName='" + hospitalName + '\'' +
                    ", departmentId=" + departmentId +
                    ", departmentName='" + departmentName + '\'' +
                    ", professionalTitle='" + professionalTitle + '\'' +
                    ", userAge=" + userAge +
                    ", userSig='" + userSig + '\'' +
                    ", identificationType='" + identificationType + '\'' +
                    ", identificationNo='" + identificationNo + '\'' +
                    ", departmentCode='" + departmentCode + '\'' +
                    ", hospitalCode='" + hospitalCode + '\'' +
                    ", doctorBrief='" + doctorBrief + '\'' +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    '}';
        }
    }
}