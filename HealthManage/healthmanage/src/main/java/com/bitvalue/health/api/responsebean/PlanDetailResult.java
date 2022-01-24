package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;

public class PlanDetailResult implements Serializable {
    public int planId;
    public String userId;
    public int goodsId;
    public String planName;
    public String startDate;
    public String endDate;
    public String createTime;
    public List<UserPlanDetailsDTO> userPlanDetails;


    public static class DetailImage implements Serializable {

        private Integer id;
        private String userId;
        private String imageType;
        private String sourceId;
        private String fileId;
        private String fileUrl;
        private String previewFileId;
        private String previewFileUrl;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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


    }

    public static class UserPlanDetailsDTO2 implements Serializable{

        private Integer id;
        private Integer planId;
        private Integer taskId;
        private String planType;
        private String planDescribe;
        private String contentId;
        private Long execTime;
        private Integer execFlag;
        private Integer noticeFlag;
        private ContentInfoDTO contentInfo;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPlanId() {
            return planId;
        }

        public void setPlanId(Integer planId) {
            this.planId = planId;
        }

        public Integer getTaskId() {
            return taskId;
        }

        public void setTaskId(Integer taskId) {
            this.taskId = taskId;
        }

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }

        public String getPlanDescribe() {
            return planDescribe;
        }

        public void setPlanDescribe(String planDescribe) {
            this.planDescribe = planDescribe;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }

        public Long getExecTime() {
            return execTime;
        }

        public void setExecTime(Long execTime) {
            this.execTime = execTime;
        }

        public Integer getExecFlag() {
            return execFlag;
        }

        public void setExecFlag(Integer execFlag) {
            this.execFlag = execFlag;
        }

        public Integer getNoticeFlag() {
            return noticeFlag;
        }

        public void setNoticeFlag(Integer noticeFlag) {
            this.noticeFlag = noticeFlag;
        }

        public ContentInfoDTO getContentInfo() {
            return contentInfo;
        }

        public void setContentInfo(ContentInfoDTO contentInfo) {
            this.contentInfo = contentInfo;
        }
    }



    public static class UserPlanDetailsDTO implements Serializable {
        public int id;
        public int planId;
        public String planType;//planType   Quest   Remind  Knowledge   DrugGuide
        public String planDescribe;
        public long contentId;
        public String execTime;
        public int execFlag;//计划执行完标识（1：已执行 0：未执行）
        public int noticeFlag;


        public boolean isCurrent;//新增字段，是否为当前条目

        //为预览新增的字段
        public String questUrl;
        public String remindContent;
        public String voiceList;
        public String content;
        public String title;
        public List<DetailImage> healthImages;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPlanId() {
            return planId;
        }

        public void setPlanId(int planId) {
            this.planId = planId;
        }

        public String getPlanType() {
            return planType;
        }

        public List<DetailImage> getHealthImages() {
            return healthImages;
        }

        public void setHealthImages(List<DetailImage> healthImages) {
            this.healthImages = healthImages;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }

        public String getPlanDescribe() {
            return planDescribe;
        }

        public void setPlanDescribe(String planDescribe) {
            this.planDescribe = planDescribe;
        }

        public long getContentId() {
            return contentId;
        }

        public void setContentId(long contentId) {
            this.contentId = contentId;
        }

        public String getExecTime() {
            return execTime;
        }

        public void setExecTime(String execTime) {
            this.execTime = execTime;
        }

        public int getExecFlag() {
            return execFlag;
        }

        public void setExecFlag(int execFlag) {
            this.execFlag = execFlag;
        }

        public int getNoticeFlag() {
            return noticeFlag;
        }

        public void setNoticeFlag(int noticeFlag) {
            this.noticeFlag = noticeFlag;
        }

        public boolean isCurrent() {
            return isCurrent;
        }

        public void setCurrent(boolean current) {
            isCurrent = current;
        }

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

        public String getVoiceList() {
            return voiceList;
        }

        public void setVoiceList(String voiceList) {
            this.voiceList = voiceList;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


}
