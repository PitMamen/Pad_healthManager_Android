package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author created by bitvalue
 * @data : 12/06
 * 新出院患者实体
 */
public class NewLeaveBean implements Serializable {
    private int pageNo;
    private int pageSize;
    private List<?> rainbow;
    private List<RowsDTO> rows;
    private int totalPage;
    private int totalRows;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<?> getRainbow() {
        return rainbow;
    }

    public void setRainbow(List<?> rainbow) {
        this.rainbow = rainbow;
    }

    public List<RowsDTO> getRows() {
        return rows;
    }

    public void setRows(List<RowsDTO> rows) {
        this.rows = rows;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public static class RowsDTO implements Serializable {
        public TaskDeatailBean taskDeatailBean;
        private String age;
        private String cardNo;
        private String cardType;
        private String createTime;
        private String diagDate;
        private String diagnosis;
        private String diagnosisCode;
        private String hospitalCode;
        private String cyzd;
        private int id;
        private String idNumber;
        private InfoDetailDTO infoDetail;
        private List<PlanInfoDetailDTO> planInfo;
        private String remark;
        private String sex;
        private String updateTime;
        private String updateUser;
        private String updateUserName;
        private String userId;
        private String userName;
        private String hzbq;//患者标签
        private String receiveTime = "";  //接收消息时间
        private String bqmc;// 病区名称
        private String cysj;//出院时间
        private String ks;//科室代码
        private String ksmc; //科室名称
        public String id_plan;//点击随访计划中某一个计划的 id,用于结束计划的
        public String planId;  //自定义添加的planId，后台没有
        private String taskId;  //自定义添加的taskId，后台没有
        private String sendPlanType;// 自定义添加的发送消息时判断发送类型 ：Evaluate健康评估  Remind健康提醒
        public String rightsName = "视频咨询";//权益类型(图文咨询 视频问诊，，，这个字段是自己定义的)

        public boolean isConsultation = false;//区分是 问诊还是咨询
        public boolean isShowCollection = true;  //是否显示 预诊收集控件（默认显示,只有从咨询界面跳转进去的才不显示）
        public boolean isShowSendRemind = true;  //是否显示 发送提醒控件（默认显示,只有从咨询界面跳转进去的才不显示）


        //add
        public boolean isShowCheck = true;
        public boolean isChecked = false;
        public boolean hasNewMessage = false;
        public int newMsgNum = 0;


        @Override
        public String toString() {
            return "RowsDTO{" +
                    "age='" + age + '\'' +
                    ", cardNo='" + cardNo + '\'' +
                    ", cardType='" + cardType + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", diagDate='" + diagDate + '\'' +
                    ", diagnosis='" + diagnosis + '\'' +
                    ", diagnosisCode='" + diagnosisCode + '\'' +
                    ", hospitalCode='" + hospitalCode + '\'' +
                    ", id=" + id +
                    ", idNumber='" + idNumber + '\'' +
                    ", infoDetail=" + infoDetail +
                    ", planInfo=" + planInfo +
                    ", remark='" + remark + '\'' +
                    ", sex='" + sex + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", updateUser='" + updateUser + '\'' +
                    ", updateUserName='" + updateUserName + '\'' +
                    ", userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", hzbq='" + hzbq + '\'' +
                    ", receiveTime='" + receiveTime + '\'' +
                    ", bqmc='" + bqmc + '\'' +
                    ", cysj='" + cysj + '\'' +
                    ", ks='" + ks + '\'' +
                    ", ksmc='" + ksmc + '\'' +
                    ", isShowCheck=" + isShowCheck +
                    ", isChecked=" + isChecked +
                    ", hasNewMessage=" + hasNewMessage +
                    ", newMsgNum=" + newMsgNum +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof RowsDTO) {
                RowsDTO user = (RowsDTO) obj;
                if (this.userId == null) {
                    return true;
                }
                if (this.userId.equals(user.userId)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }



        public String getSendPlanType() {
            return sendPlanType;
        }

        public void setSendPlanType(String sendPlanType) {
            this.sendPlanType = sendPlanType;
        }

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getCyzd() {
            return cyzd;
        }

        public void setCyzd(String cyzd) {
            this.cyzd = cyzd;
        }

        public String getBqmc() {
            return bqmc;
        }

        public void setBqmc(String bqmc) {
            this.bqmc = bqmc;
        }

        public String getCysj() {
            return cysj;
        }

        public void setCysj(String cysj) {
            this.cysj = cysj;
        }

        public String getKs() {
            return ks;
        }

        public void setKs(String ks) {
            this.ks = ks;
        }

        public String getKsmc() {
            return ksmc;
        }

        public void setKsmc(String ksmc) {
            this.ksmc = ksmc;
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId);
        }


        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }


        public int getNewMsgNum() {
            return newMsgNum;
        }

        public void setNewMsgNum(int newMsgNum) {
            this.newMsgNum = newMsgNum;
        }

        public String getHzbq() {
            return hzbq;
        }

        public void setHzbq(String hzbq) {
            this.hzbq = hzbq;
        }

        public boolean isHasNewMessage() {
            return hasNewMessage;
        }

        public void setHasNewMessage(boolean hasNewMessage) {
            this.hasNewMessage = hasNewMessage;
        }

        public boolean isShowCheck() {
            return isShowCheck;
        }

        public void setShowCheck(boolean showCheck) {
            isShowCheck = showCheck;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDiagDate() {
            return diagDate;
        }

        public void setDiagDate(String diagDate) {
            this.diagDate = diagDate;
        }

        public String getDiagnosis() {
            return diagnosis;
        }

        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }

        public String getDiagnosisCode() {
            return diagnosisCode;
        }

        public void setDiagnosisCode(String diagnosisCode) {
            this.diagnosisCode = diagnosisCode;
        }

        public String getHospitalCode() {
            return hospitalCode;
        }

        public void setHospitalCode(String hospitalCode) {
            this.hospitalCode = hospitalCode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public InfoDetailDTO getInfoDetail() {
            return infoDetail;
        }

        public void setInfoDetail(InfoDetailDTO infoDetail) {
            this.infoDetail = infoDetail;
        }

        public List<PlanInfoDetailDTO> getPlanInfo() {
            return planInfo;
        }

        public void setPlanInfo(List<PlanInfoDetailDTO> planInfo) {
            this.planInfo = planInfo;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public String getUpdateUserName() {
            return updateUserName;
        }

        public void setUpdateUserName(String updateUserName) {
            this.updateUserName = updateUserName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public static class PlanInfoDetailDTO implements Serializable {
            private String id;
            private String userId;
            private String goodsId;
            private String templateId;
            private String planId;
            private String goodsName;
            private String planName;
            private String surplusConsultingNum;
            private String startDate;
            private String goodsSpec;
            private String contentText;
            private String evalTime;
            private String owner;
            private String endTime;
            private String patientName;
            private boolean checked;

            @Override
            public String toString() {
                return "PlanInfoDetailDTO{" +
                        "id='" + id + '\'' +
                        ", userId='" + userId + '\'' +
                        ", goodsId='" + goodsId + '\'' +
                        ", templateId='" + templateId + '\'' +
                        ", planId='" + planId + '\'' +
                        ", goodsName='" + goodsName + '\'' +
                        ", planName='" + planName + '\'' +
                        ", surplusConsultingNum='" + surplusConsultingNum + '\'' +
                        ", startDate='" + startDate + '\'' +
                        ", goodsSpec='" + goodsSpec + '\'' +
                        ", endTime='" + endTime + '\'' +
                        '}';
            }

            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }

            public String getPatientName() {
                return patientName;
            }

            public void setPatientName(String patientName) {
                this.patientName = patientName;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getTemplateId() {
                return templateId;
            }

            public void setTemplateId(String templateId) {
                this.templateId = templateId;
            }

            public String getPlanId() {
                return planId;
            }

            public void setPlanId(String planId) {
                this.planId = planId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getPlanName() {
                return planName;
            }

            public void setPlanName(String planName) {
                this.planName = planName;
            }

            public String getSurplusConsultingNum() {
                return surplusConsultingNum;
            }

            public void setSurplusConsultingNum(String surplusConsultingNum) {
                this.surplusConsultingNum = surplusConsultingNum;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public String getGoodsSpec() {
                return goodsSpec;
            }

            public void setGoodsSpec(String goodsSpec) {
                this.goodsSpec = goodsSpec;
            }

            public String getContentText() {
                return contentText;
            }

            public void setContentText(String contentText) {
                this.contentText = contentText;
            }

            public String getEvalTime() {
                return evalTime;
            }

            public void setEvalTime(String evalTime) {
                this.evalTime = evalTime;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }
        }


        public static class InfoDetailDTO implements Serializable {
            private String csd;
            private String csrq;
            private String cxjmjkdabh;
            private String dhhm;
            private String fkdq;
            private String gj;
            private String gjmc;
            private String gzdwdhhm;
            private String gzdwdz;
            private String gzdwyb;
            private String hkdzyb;
            private String hyzk;
            private String hyzkmc;
            private String hzlx;
            private String jdrqsj;
            private String jdyljgzzdm;
            private String jdzxm;
            private String jkkh;
            private String kh;
            private String klx;
            private String lxrdh;
            private String lxrgx;
            private String lxrgxmc;
            private String lxrxm;
            private String lxryb;
            private String mj;
            private String mz;
            private String mzmc;
            private String sjhm;
            private String xb;
            private String xbmc;
            private int xgbz;
            private String xm;
            private String yljgdm;
            private String ywscsj;
            private String yydah;
            private String zjhm;
            private String zjlx;
            private String zjlxmc;


            @Override
            public String toString() {
                return "InfoDetailDTO{" +
                        "csd='" + csd + '\'' +
                        ", csrq='" + csrq + '\'' +
                        ", cxjmjkdabh='" + cxjmjkdabh + '\'' +
                        ", dhhm='" + dhhm + '\'' +
                        ", fkdq='" + fkdq + '\'' +
                        ", gj='" + gj + '\'' +
                        ", gjmc='" + gjmc + '\'' +
                        ", gzdwdhhm='" + gzdwdhhm + '\'' +
                        ", gzdwyb='" + gzdwyb + '\'' +
                        ", hkdzyb='" + hkdzyb + '\'' +
                        ", hyzk='" + hyzk + '\'' +
                        ", hyzkmc='" + hyzkmc + '\'' +
                        ", hzlx='" + hzlx + '\'' +
                        ", jdrqsj='" + jdrqsj + '\'' +
                        ", jdyljgzzdm='" + jdyljgzzdm + '\'' +
                        ", jdzxm='" + jdzxm + '\'' +
                        ", jkkh='" + jkkh + '\'' +
                        ", kh='" + kh + '\'' +
                        ", klx='" + klx + '\'' +
                        ", lxrdh='" + lxrdh + '\'' +
                        ", lxrgx='" + lxrgx + '\'' +
                        ", lxrgxmc='" + lxrgxmc + '\'' +
                        ", lxrxm='" + lxrxm + '\'' +
                        ", lxryb='" + lxryb + '\'' +
                        ", mj='" + mj + '\'' +
                        ", mz='" + mz + '\'' +
                        ", mzmc='" + mzmc + '\'' +
                        ", sjhm='" + sjhm + '\'' +
                        ", xb='" + xb + '\'' +
                        ", xbmc='" + xbmc + '\'' +
                        ", xgbz=" + xgbz +
                        ", xm='" + xm + '\'' +
                        ",gzdwdz'" + gzdwdz + '\'' +
                        ", yljgdm='" + yljgdm + '\'' +
                        ", ywscsj='" + ywscsj + '\'' +
                        ", yydah='" + yydah + '\'' +
                        ", zjhm='" + zjhm + '\'' +
                        ", zjlx='" + zjlx + '\'' +
                        ", zjlxmc='" + zjlxmc + '\'' +
                        '}';
            }

            public String getGzdwdz() {
                return gzdwdz;
            }

            public void setGzdwdz(String gzdwdz) {
                this.gzdwdz = gzdwdz;
            }

            public String getCsd() {
                return csd;
            }

            public void setCsd(String csd) {
                this.csd = csd;
            }

            public String getCsrq() {
                return csrq;
            }

            public void setCsrq(String csrq) {
                this.csrq = csrq;
            }

            public String getCxjmjkdabh() {
                return cxjmjkdabh;
            }

            public void setCxjmjkdabh(String cxjmjkdabh) {
                this.cxjmjkdabh = cxjmjkdabh;
            }

            public String getDhhm() {
                return dhhm;
            }

            public void setDhhm(String dhhm) {
                this.dhhm = dhhm;
            }

            public String getFkdq() {
                return fkdq;
            }

            public void setFkdq(String fkdq) {
                this.fkdq = fkdq;
            }

            public String getGj() {
                return gj;
            }

            public void setGj(String gj) {
                this.gj = gj;
            }

            public String getGjmc() {
                return gjmc;
            }

            public void setGjmc(String gjmc) {
                this.gjmc = gjmc;
            }

            public String getGzdwdhhm() {
                return gzdwdhhm;
            }

            public void setGzdwdhhm(String gzdwdhhm) {
                this.gzdwdhhm = gzdwdhhm;
            }

            public String getGzdwyb() {
                return gzdwyb;
            }

            public void setGzdwyb(String gzdwyb) {
                this.gzdwyb = gzdwyb;
            }

            public String getHkdzyb() {
                return hkdzyb;
            }

            public void setHkdzyb(String hkdzyb) {
                this.hkdzyb = hkdzyb;
            }

            public String getHyzk() {
                return hyzk;
            }

            public void setHyzk(String hyzk) {
                this.hyzk = hyzk;
            }

            public String getHyzkmc() {
                return hyzkmc;
            }

            public void setHyzkmc(String hyzkmc) {
                this.hyzkmc = hyzkmc;
            }

            public String getHzlx() {
                return hzlx;
            }

            public void setHzlx(String hzlx) {
                this.hzlx = hzlx;
            }

            public String getJdrqsj() {
                return jdrqsj;
            }

            public void setJdrqsj(String jdrqsj) {
                this.jdrqsj = jdrqsj;
            }

            public String getJdyljgzzdm() {
                return jdyljgzzdm;
            }

            public void setJdyljgzzdm(String jdyljgzzdm) {
                this.jdyljgzzdm = jdyljgzzdm;
            }

            public String getJdzxm() {
                return jdzxm;
            }

            public void setJdzxm(String jdzxm) {
                this.jdzxm = jdzxm;
            }

            public String getJkkh() {
                return jkkh;
            }

            public void setJkkh(String jkkh) {
                this.jkkh = jkkh;
            }

            public String getKh() {
                return kh;
            }

            public void setKh(String kh) {
                this.kh = kh;
            }

            public String getKlx() {
                return klx;
            }

            public void setKlx(String klx) {
                this.klx = klx;
            }

            public String getLxrdh() {
                return lxrdh;
            }

            public void setLxrdh(String lxrdh) {
                this.lxrdh = lxrdh;
            }

            public String getLxrgx() {
                return lxrgx;
            }

            public void setLxrgx(String lxrgx) {
                this.lxrgx = lxrgx;
            }

            public String getLxrgxmc() {
                return lxrgxmc;
            }

            public void setLxrgxmc(String lxrgxmc) {
                this.lxrgxmc = lxrgxmc;
            }

            public String getLxrxm() {
                return lxrxm;
            }

            public void setLxrxm(String lxrxm) {
                this.lxrxm = lxrxm;
            }

            public String getLxryb() {
                return lxryb;
            }

            public void setLxryb(String lxryb) {
                this.lxryb = lxryb;
            }

            public String getMj() {
                return mj;
            }

            public void setMj(String mj) {
                this.mj = mj;
            }

            public String getMz() {
                return mz;
            }

            public void setMz(String mz) {
                this.mz = mz;
            }

            public String getMzmc() {
                return mzmc;
            }

            public void setMzmc(String mzmc) {
                this.mzmc = mzmc;
            }

            public String getSjhm() {
                return sjhm;
            }

            public void setSjhm(String sjhm) {
                this.sjhm = sjhm;
            }

            public String getXb() {
                return xb;
            }

            public void setXb(String xb) {
                this.xb = xb;
            }

            public String getXbmc() {
                return xbmc;
            }

            public void setXbmc(String xbmc) {
                this.xbmc = xbmc;
            }

            public int getXgbz() {
                return xgbz;
            }

            public void setXgbz(int xgbz) {
                this.xgbz = xgbz;
            }

            public String getXm() {
                return xm;
            }

            public void setXm(String xm) {
                this.xm = xm;
            }

            public String getYljgdm() {
                return yljgdm;
            }

            public void setYljgdm(String yljgdm) {
                this.yljgdm = yljgdm;
            }

            public String getYwscsj() {
                return ywscsj;
            }

            public void setYwscsj(String ywscsj) {
                this.ywscsj = ywscsj;
            }

            public String getYydah() {
                return yydah;
            }

            public void setYydah(String yydah) {
                this.yydah = yydah;
            }

            public String getZjhm() {
                return zjhm;
            }

            public void setZjhm(String zjhm) {
                this.zjhm = zjhm;
            }

            public String getZjlx() {
                return zjlx;
            }

            public void setZjlx(String zjlx) {
                this.zjlx = zjlx;
            }

            public String getZjlxmc() {
                return zjlxmc;
            }

            public void setZjlxmc(String zjlxmc) {
                this.zjlxmc = zjlxmc;
            }
        }
    }
}
