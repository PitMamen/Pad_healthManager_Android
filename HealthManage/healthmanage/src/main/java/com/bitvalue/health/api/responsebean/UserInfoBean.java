package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 02/18
 */
public class UserInfoBean implements Serializable {
        private UserDTO user;

        public UserDTO getUser() {
            return user;
        }

        public void setUser(UserDTO user) {
            this.user = user;
        }

        public static class UserDTO {
            private int accountId;
            private int userId;
            private String userName;
            private String userSex;
            private String userType;
            private String phone;
            private String relationship;
            private String identificationType;
            private String identificationNo;
            private int userAge;
            private Object userSig;
            private boolean isDefault;

            public int getAccountId() {
                return accountId;
            }

            public void setAccountId(int accountId) {
                this.accountId = accountId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
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

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getRelationship() {
                return relationship;
            }

            public void setRelationship(String relationship) {
                this.relationship = relationship;
            }

            public String getIdentificationType() {
                return identificationType;
            }

            public void setIdentificationType(String identificationType) {
                this.identificationType = identificationType;
            }

            public String getIdentificationNo() {
                return identificationNo;
            }

            public void setIdentificationNo(String identificationNo) {
                this.identificationNo = identificationNo;
            }

            public int getUserAge() {
                return userAge;
            }

            public void setUserAge(int userAge) {
                this.userAge = userAge;
            }

            public Object getUserSig() {
                return userSig;
            }

            public void setUserSig(Object userSig) {
                this.userSig = userSig;
            }

            public boolean isIsDefault() {
                return isDefault;
            }

            public void setIsDefault(boolean isDefault) {
                this.isDefault = isDefault;
            }


            @Override
            public String toString() {
                return "UserDTO{" +
                        "accountId=" + accountId +
                        ", userId=" + userId +
                        ", userName='" + userName + '\'' +
                        ", userSex='" + userSex + '\'' +
                        ", userType='" + userType + '\'' +
                        ", phone='" + phone + '\'' +
                        ", relationship='" + relationship + '\'' +
                        ", identificationType='" + identificationType + '\'' +
                        ", identificationNo='" + identificationNo + '\'' +
                        ", userAge=" + userAge +
                        ", userSig=" + userSig +
                        ", isDefault=" + isDefault +
                        '}';
            }
        }
    }
