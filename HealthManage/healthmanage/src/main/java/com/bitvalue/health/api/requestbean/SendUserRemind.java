package com.bitvalue.health.api.requestbean;

import java.util.List;

public class SendUserRemind {

    private List<ArticleInfoDTO> articleInfo;
    private String articleList;
    private Integer contentId;
    private String createTime;
    private Integer id;
    private String picList;
    private String remindContent;
    private String remindName;
    private String remindUser;
    private Integer type;
    private String userId;
    private List<VedioInfoDTO> vedioInfo;
    private String videoList;
    private String voiceList;

    public List<ArticleInfoDTO> getArticleInfo() {
        return articleInfo;
    }

    public void setArticleInfo(List<ArticleInfoDTO> articleInfo) {
        this.articleInfo = articleInfo;
    }

    public String getArticleList() {
        return articleList;
    }

    public void setArticleList(String articleList) {
        this.articleList = articleList;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicList() {
        return picList;
    }

    public void setPicList(String picList) {
        this.picList = picList;
    }

    public String getRemindContent() {
        return remindContent;
    }

    public void setRemindContent(String remindContent) {
        this.remindContent = remindContent;
    }

    public String getRemindName() {
        return remindName;
    }

    public void setRemindName(String remindName) {
        this.remindName = remindName;
    }

    public String getRemindUser() {
        return remindUser;
    }

    public void setRemindUser(String remindUser) {
        this.remindUser = remindUser;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<VedioInfoDTO> getVedioInfo() {
        return vedioInfo;
    }

    public void setVedioInfo(List<VedioInfoDTO> vedioInfo) {
        this.vedioInfo = vedioInfo;
    }

    public String getVideoList() {
        return videoList;
    }

    public void setVideoList(String videoList) {
        this.videoList = videoList;
    }

    public String getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(String voiceList) {
        this.voiceList = voiceList;
    }
}
