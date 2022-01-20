package com.bitvalue.health.api.responsebean;

public class TaskInfoDTO {
    private Integer id;
    private Integer planId;
    private Integer taskId;
    private String planType;
    private String planDescribe;
    private String contentId;
    private String execTime;
    private Integer execFlag;
    private Integer noticeFlag;

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

    public String getExecTime() {
        return execTime;
    }

    public void setExecTime(String execTime) {
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
}
