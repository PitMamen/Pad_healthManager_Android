package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.google.gson.annotations.SerializedName;
import com.hjq.http.config.IRequestApi;

public final class PatientDataMoreApi implements IRequestApi {

    public String userId;

    @Override
    public String getApi() {
        return Constants.API_ACCOUNT + "/userInfo/getBaseInfo";
    }

    public static class PatientDataMoreResponse{

        public BaseInfoDTO baseInfo;
        public ExternalInfoDTO externalInfo;

        public BaseInfoDTO getBaseInfo() {
            return baseInfo;
        }

        public void setBaseInfo(BaseInfoDTO baseInfo) {
            this.baseInfo = baseInfo;
        }

        public ExternalInfoDTO getExternalInfo() {
            return externalInfo;
        }

        public void setExternalInfo(ExternalInfoDTO externalInfo) {
            this.externalInfo = externalInfo;
        }

        public static class BaseInfoDTO {
            @SerializedName("birthday")
            private String birthdayX;
            private String healthRecordCode;
            @SerializedName("identificationNo")
            private String identificationNoX;
            @SerializedName("userName")
            private String userNameX;
            @SerializedName("userSex")
            private String userSexX;

            public String getBirthdayX() {
                return birthdayX;
            }

            public void setBirthdayX(String birthdayX) {
                this.birthdayX = birthdayX;
            }

            public String getHealthRecordCode() {
                return healthRecordCode;
            }

            public void setHealthRecordCode(String healthRecordCode) {
                this.healthRecordCode = healthRecordCode;
            }

            public String getIdentificationNoX() {
                return identificationNoX;
            }

            public void setIdentificationNoX(String identificationNoX) {
                this.identificationNoX = identificationNoX;
            }

            public String getUserNameX() {
                return userNameX;
            }

            public void setUserNameX(String userNameX) {
                this.userNameX = userNameX;
            }

            public String getUserSexX() {
                return userSexX;
            }

            public void setUserSexX(String userSexX) {
                this.userSexX = userSexX;
            }

            @Override
            public String toString() {
                return "BaseInfoDTO{" +
                        "birthdayX='" + birthdayX + '\'' +
                        ", healthRecordCode='" + healthRecordCode + '\'' +
                        ", identificationNoX='" + identificationNoX + '\'' +
                        ", userNameX='" + userNameX + '\'' +
                        ", userSexX='" + userSexX + '\'' +
                        '}';
            }
        }


        public static class ExternalInfoDTO{

            private String address;
            private String bloodType;
            private int height;
            private String phone;
            private String rh;
            private int weight;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getBloodType() {
                return bloodType;
            }

            public void setBloodType(String bloodType) {
                this.bloodType = bloodType;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getRh() {
                return rh;
            }

            public void setRh(String rh) {
                this.rh = rh;
            }

            public int getWeight() {
                return weight;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }

            @Override
            public String toString() {
                return "ExternalInfoDTO{" +
                        "address='" + address + '\'' +
                        ", bloodType='" + bloodType + '\'' +
                        ", height=" + height +
                        ", phone='" + phone + '\'' +
                        ", rh='" + rh + '\'' +
                        ", weight=" + weight +
                        '}';
            }
        }





    }

}