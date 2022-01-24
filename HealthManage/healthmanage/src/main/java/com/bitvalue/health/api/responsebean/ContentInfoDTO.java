package com.bitvalue.health.api.responsebean;

import java.util.List;

public class ContentInfoDTO {
    public Integer id;
    public String examType;
    public String examName;
    public String examDescribe;
    public String contentId;
    public Integer goodsId;
    public String userId;
    public  String questUrl;
    public String remindContent;
    public String voiceList;
    public String knowContent;
    public String knowUrl;
    public String articleTitle;
    public List<HealthImagesDTO> healthImages;

    public String getQuestUrl() {
        return questUrl;
    }

    public void setQuestUrl(String questUrl) {
        this.questUrl = questUrl;
    }

    public String getRemindContent() {
        return remindContent;
    }

    public void setRemindContent(String remindContent) {
        this.remindContent = remindContent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Object getExamDescribe() {
        return examDescribe;
    }


    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setExamDescribe(String examDescribe) {
        this.examDescribe = examDescribe;
    }

    public List<HealthImagesDTO> getHealthImages() {
        return healthImages;
    }

    public void setHealthImages(List<HealthImagesDTO> healthImages) {
        this.healthImages = healthImages;
    }
}
