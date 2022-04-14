package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 04/13
 * 审核记录 响应实体
 */
public class DataReViewRecordResponse implements Serializable {


    private String createTime;
    private String dealDetail;
    private String dealImages;
    private String dealResult;
    private String dealType;
    private String dealUser;
    private String dealUserName;
    private int id;
    private String remark;
    private String tradeId;
    private String userId;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDealDetail() {
        return dealDetail;
    }

    public void setDealDetail(String dealDetail) {
        this.dealDetail = dealDetail;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    @Override
    public String toString() {
        return "DataReViewRecordResponse{" +
                "createTime='" + createTime + '\'' +
                ", dealDetail='" + dealDetail + '\'' +
                ", dealImages='" + dealImages + '\'' +
                ", dealResult='" + dealResult + '\'' +
                ", dealType='" + dealType + '\'' +
                ", dealUser='" + dealUser + '\'' +
                ", dealUserName='" + dealUserName + '\'' +
                ", id=" + id +
                ", remark='" + remark + '\'' +
                ", tradeId='" + tradeId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
