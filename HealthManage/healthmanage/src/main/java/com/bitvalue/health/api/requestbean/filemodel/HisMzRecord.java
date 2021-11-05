package com.bitvalue.health.api.requestbean.filemodel;

import java.util.List;


/**
 * @ClassName HisMzRecord
 * @Description: 门诊数据
 * @Author ZhangFayu
 * @Date 2019/9/24
 * @Version V1.0
 **/
public class HisMzRecord extends HisDataCommon {
    // 就诊类型
    private String jzlx;
    // 就诊日期
    private String jzrq;
    // 医疗机构代码
    private String yljgdm;
    // 就诊科室代码
    private String jzksdm;
    // 医院就诊科室名称
    private String yyjzksmc;
    // 主诊医生工号
    private String zzysgh;
    // 主诊医生姓名
    private String zzysxm;
    // 门诊诊断编码
    private String mzzdbm;
    // 医院门诊诊断编码名称
    private String yymzzdbmmc;
    // 编码类型
    private String bmlx;
    // 门诊诊断说明
    private String mzzdsm;
    // 主诉
    private String zs;
    // 症状描述
    private String zzms;
    // 体格检査
    private String tgjc;

    // 处方信息 list
    List<HisDataCfxx> cfxx;

    public String getJzlx() {
        return jzlx;
    }

    public void setJzlx(String jzlx) {
        this.jzlx = jzlx;
    }

    public String getJzrq() {
        return jzrq;
    }

    public void setJzrq(String jzrq) {
        this.jzrq = jzrq;
    }

    public String getYljgdm() {
        return yljgdm;
    }

    public void setYljgdm(String yljgdm) {
        this.yljgdm = yljgdm;
    }

    public String getJzksdm() {
        return jzksdm;
    }

    public void setJzksdm(String jzksdm) {
        this.jzksdm = jzksdm;
    }

    public String getYyjzksmc() {
        return yyjzksmc;
    }

    public void setYyjzksmc(String yyjzksmc) {
        this.yyjzksmc = yyjzksmc;
    }

    public String getZzysgh() {
        return zzysgh;
    }

    public void setZzysgh(String zzysgh) {
        this.zzysgh = zzysgh;
    }

    public String getZzysxm() {
        return zzysxm;
    }

    public void setZzysxm(String zzysxm) {
        this.zzysxm = zzysxm;
    }

    public String getMzzdbm() {
        return mzzdbm;
    }

    public void setMzzdbm(String mzzdbm) {
        this.mzzdbm = mzzdbm;
    }

    public String getYymzzdbmmc() {
        return yymzzdbmmc;
    }

    public void setYymzzdbmmc(String yymzzdbmmc) {
        this.yymzzdbmmc = yymzzdbmmc;
    }

    public String getBmlx() {
        return bmlx;
    }

    public void setBmlx(String bmlx) {
        this.bmlx = bmlx;
    }

    public String getMzzdsm() {
        return mzzdsm;
    }

    public void setMzzdsm(String mzzdsm) {
        this.mzzdsm = mzzdsm;
    }

    public String getZs() {
        return zs;
    }

    public void setZs(String zs) {
        this.zs = zs;
    }

    public String getZzms() {
        return zzms;
    }

    public void setZzms(String zzms) {
        this.zzms = zzms;
    }

    public String getTgjc() {
        return tgjc;
    }

    public void setTgjc(String tgjc) {
        this.tgjc = tgjc;
    }

    public List<HisDataCfxx> getCfxx() {
        return cfxx;
    }

    public void setCfxx(List<HisDataCfxx> cfxx) {
        this.cfxx = cfxx;
    }
}
