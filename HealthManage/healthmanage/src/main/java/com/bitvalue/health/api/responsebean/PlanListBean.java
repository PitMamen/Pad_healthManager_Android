package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/***
 * 我的健康管理计划(套餐)响应实体
 */
public class PlanListBean implements Serializable {
    public String templateId;
    public String templateName;
    public String basetimeType;
    public String userId;
    public String status = "0";
    public long createTime;
    public long updateTime;
    public int userNum;
}
