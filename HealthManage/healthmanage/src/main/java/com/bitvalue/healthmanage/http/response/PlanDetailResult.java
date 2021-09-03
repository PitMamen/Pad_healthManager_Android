package com.bitvalue.healthmanage.http.response;

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
    }
}
