package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 01/07
 * 查看患者上传资料实体
 */
public final class TaskDetailBean implements Serializable {

    private int id;
    private String userId;
    private String hospital;
    private String dept;
    private String visitTime;
    private String visitType;
    private String diagnosis;
    private String userGoodsId;
    private long contentId;
    private List<HealthImagesDTO> healthImagesList;
    private UserInfo userInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getUserGoodsId() {
        return userGoodsId;
    }

    public void setUserGoodsId(String userGoodsId) {
        this.userGoodsId = userGoodsId;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public List<HealthImagesDTO> getHealthImages() {
        return healthImagesList;
    }

    public void setHealthImages(List<HealthImagesDTO> healthImages) {
        this.healthImagesList = healthImages;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfo implements Serializable{
        private String accountId;
        private String userSex;
        private boolean isDefault;
        private String phone;
        private String identificationNo;
        private String identificationType;
        private String userType;
        private String userName;
        private String relationship;
        private String userSig;
        private String userId;
        private int userAge;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getUserSex() {
            return userSex;
        }

        public void setUserSex(String userSex) {
            this.userSex = userSex;
        }

        public boolean isDefault() {
            return isDefault;
        }

        public void setDefault(boolean aDefault) {
            isDefault = aDefault;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIdentificationNo() {
            return identificationNo;
        }

        public void setIdentificationNo(String identificationNo) {
            this.identificationNo = identificationNo;
        }

        public String getIdentificationType() {
            return identificationType;
        }

        public void setIdentificationType(String identificationType) {
            this.identificationType = identificationType;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public String getUserSig() {
            return userSig;
        }

        public void setUserSig(String userSig) {
            this.userSig = userSig;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getUserAge() {
            return userAge;
        }

        public void setUserAge(int userAge) {
            this.userAge = userAge;
        }
    }



    public static class HealthImagesDTO implements Serializable {
        public int id;
        public String userId;
        public String imageType;
        public String sourceId;
        public String fileId;
        public String fileUrl;
        public String previewFileId;
        public String previewFileUrl;
        public String createTime;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getImageType() {
            return imageType;
        }

        public void setImageType(String imageType) {
            this.imageType = imageType;
        }

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getPreviewFileId() {
            return previewFileId;
        }

        public void setPreviewFileId(String previewFileId) {
            this.previewFileId = previewFileId;
        }

        public String getPreviewFileUrl() {
            return previewFileUrl;
        }

        public void setPreviewFileUrl(String previewFileUrl) {
            this.previewFileUrl = previewFileUrl;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        @Override
        public String toString() {
            return "HealthImagesDTO{" +
                    "id=" + id +
                    ", userId='" + userId + '\'' +
                    ", imageType='" + imageType + '\'' +
                    ", sourceId='" + sourceId + '\'' +
                    ", fileId='" + fileId + '\'' +
                    ", fileUrl='" + fileUrl + '\'' +
                    ", previewFileId='" + previewFileId + '\'' +
                    ", previewFileUrl='" + previewFileUrl + '\'' +
                    ", createTime='" + createTime + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "TaskDetailBean{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", hospital='" + hospital + '\'' +
                ", dept='" + dept + '\'' +
                ", visitTime='" + visitTime + '\'' +
                ", visitType='" + visitType + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", userGoodsId='" + userGoodsId + '\'' +
                ", contentId=" + contentId +
                ", healthImages=" + healthImagesList +
                '}';
    }
}