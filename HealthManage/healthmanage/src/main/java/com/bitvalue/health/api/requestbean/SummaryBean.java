package com.bitvalue.health.api.requestbean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 05/23
 * 问诊小结 实体
 */
public class SummaryBean implements Serializable {
    private Object dealDetail; ///
    private String rightsName;
    private String createTime;
    private String dealImages;
    private String dealResult;
    private String dealType;
    private String dealUser;
    private String dealUserName;
    private String remark;
    private String tradeId;
    private String userId;


    public static class DealDetail implements Serializable {
        public String chiefComplaint;   //病情描述
        public String treatment;  //医生建议

        public String getChiefComplaint() {
            return chiefComplaint;
        }

        public void setChiefComplaint(String chiefComplaint) {
            this.chiefComplaint = chiefComplaint;
        }

        public String getTreatment() {
            return treatment;
        }

        public void setTreatment(String treatment) {
            this.treatment = treatment;
        }
    }


    public String getRightsName() {
        return rightsName;
    }

    public void setRightsName(String rightsName) {
        this.rightsName = rightsName;
    }

    public Object getDealDetail() {
        return dealDetail;
    }

    public void setDealDetail(DealDetail dealDetail) {
        this.dealDetail = dealDetail;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDealImages() {
        return dealImages;
    }

    public void setDealImages(String dealImages) {
        this.dealImages = dealImages;
    }

    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDealUser() {
        return dealUser;
    }

    public void setDealUser(String dealUser) {
        this.dealUser = dealUser;
    }

    public String getDealUserName() {
        return dealUserName;
    }

    public void setDealUserName(String dealUserName) {
        this.dealUserName = dealUserName;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
