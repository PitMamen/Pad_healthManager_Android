package com.bitvalue.healthmanage.http.bean;

import java.util.List;

/**
 * 
 */
public final class PatientDataResult {

    public UserInfoDTO userInfo;
    public long visitTime;
    public List<HealthImagesListDTO> healthImagesList;
    public String contentId;
    public String diagnosis;
    public String dept;
    public int id;
    public String hospital;
    public String userId;
    public String userGoodsId;
    public String visitType;

    public static class UserInfoDTO {
        public String userSex;
        public String identificationType;
        public String userName;
        public int userId;
        public String cardNo;
        public int userAge;
        public int accountId;
        public boolean isDefault;
        public String phone;
        public String identificationNo;
        public String userType;
        public String relationship;
        public String userSig;
    }

    public static class HealthImagesListDTO {
        public int id;
        public String userId;
        public String imageType;
        public String sourceId;
        public String fileId;
        public String fileUrl;
        public String previewFileId;
        public String previewFileUrl;
        public String createTime;
    }
}