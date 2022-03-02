package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 02/23
 */
public class PatientBaseInfoBean implements Serializable {


    private BaseInfoDTO baseInfo;
    private ExternalInfoDTO externalInfo;

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
        private Object healthRecordCode;
        private String userName;
        private String userSex;
        private String birthday;
        private String identificationNo;

        public Object getHealthRecordCode() {
            return healthRecordCode;
        }

        public void setHealthRecordCode(Object healthRecordCode) {
            this.healthRecordCode = healthRecordCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserSex() {
            return userSex;
        }

        public void setUserSex(String userSex) {
            this.userSex = userSex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getIdentificationNo() {
            return identificationNo;
        }

        public void setIdentificationNo(String identificationNo) {
            this.identificationNo = identificationNo;
        }
    }

    public static class ExternalInfoDTO {
        private String phone;
        private String bloodType;
        private String rh;
        private int height;
        private int weight;
        private String address;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBloodType() {
            return bloodType;
        }

        public void setBloodType(String bloodType) {
            this.bloodType = bloodType;
        }

        public String getRh() {
            return rh;
        }

        public void setRh(String rh) {
            this.rh = rh;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public Object getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
