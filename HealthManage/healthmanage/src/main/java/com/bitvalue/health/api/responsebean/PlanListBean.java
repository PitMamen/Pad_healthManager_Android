package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/***
 * 我的健康管理计划(套餐)响应实体
 */
public class PlanListBean implements Serializable,Comparable<PlanListBean> {
    public String templateId;
    public String templateName;
    public String basetimeType;
    public String userId;
    public String status = "0";
    public String diseaseName = "";
    public long createTime;
    public long updateTime;
    public int userNum;
    public String belong;
    public String deptName;

    public boolean isChecked;


    @Override
    public int compareTo(PlanListBean planListBean) {
        return planListBean.userNum - this.userNum;
    }
}
