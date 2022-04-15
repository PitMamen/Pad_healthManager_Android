package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/21
 */
public class MyRightBean implements Serializable {
    private int id;
    private String userId;
    private int goodsId;
    private long beginTime;
    private long endTime;
    private int talkNum;
    private int talkedNum;
    private Object updateTime;
    private Object remark;
    private String goodsName;
    private String goodsSpec;
    private String deptName;
    private String requesting;
    private String belong;
    private List<UserGoodsAttr> userGoodsAttr;


    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getTalkNum() {
        return talkNum;
    }

    public void setTalkNum(int talkNum) {
        this.talkNum = talkNum;
    }

    public int getTalkedNum() {
        return talkedNum;
    }

    public void setTalkedNum(int talkedNum) {
        this.talkedNum = talkedNum;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public List<UserGoodsAttr> getUserGoodsAttr() {
        return userGoodsAttr;
    }

    public void setUserGoodsAttr(List<UserGoodsAttr> userGoodsAttr) {
        this.userGoodsAttr = userGoodsAttr;
    }


    public static class UserGoodsAttr {

        private int id;
        private int usergoodsId;
        private String userId;
        private String goodsId;
        private String attrName;
        private String attrValue;
        private String usedValue;
        private long updateTime;
        private DataRemark remark;


        public void setRemark(DataRemark remark) {
            this.remark = remark;
        }

        public DataRemark getRemark() {
            return remark;
        }

        public static class DataRemark{
            private String uploadDocFlag;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUsergoodsId() {
            return usergoodsId;
        }

        public void setUsergoodsId(int usergoodsId) {
            this.usergoodsId = usergoodsId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public String getAttrValue() {
            return attrValue;
        }

        public void setAttrValue(String attrValue) {
            this.attrValue = attrValue;
        }

        public String getUsedValue() {
            return usedValue;
        }

        public void setUsedValue(String usedValue) {
            this.usedValue = usedValue;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }



    }


}
