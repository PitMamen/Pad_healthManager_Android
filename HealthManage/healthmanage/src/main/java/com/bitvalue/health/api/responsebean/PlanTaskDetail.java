package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;

public class PlanTaskDetail implements Serializable {
    public int planId;
    public String userId;
    public int goodsId;
    public String planName;
    public String startDate;
    public String endDate;
    public String createTime;
    public String owner;
    public String taskId;
    public List<UserPlanDetailsDTO> userPlanDetails;



    public static class UserPlanDetailsDTO implements Serializable{

        public Integer id;
        public Integer planId;
        public Integer taskId;
        public String planType;
        public String planDescribe;
        public String contentId;
        public Long execTime;
        public Integer execFlag;
        public Integer noticeFlag;
        public ContentInfoDTO contentInfo;

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




}
