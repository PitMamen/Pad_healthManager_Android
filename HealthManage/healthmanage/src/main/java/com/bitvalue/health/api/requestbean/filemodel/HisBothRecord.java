package com.bitvalue.health.api.requestbean.filemodel;

import java.util.List;

/**
 * @ClassName HisZyRecord
 * @Description: 住院数据
 * @Author ZhangFayu
 * @Date 2019/9/24
 * @Version V1.0
 **/
public class HisBothRecord extends HisDataCommon {
    public TbCisMain cismain;
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


    // 医院就诊类型名称
    private String jzlxmc;
    // 诊断编码
    private String zdbm0;
    // 医院诊断名称
    private String zdmc;
    // 诊断编码类型
    private String bmlx0;
    // 入院科室
    private String ryks;
    // 医院入院科室名称
    private String ryksmc;
    // 出院科室
    private String cyks;
    // 医院出院科室名称
    private String cyksmc;
    // 入院时间
    private String rysj;
    // 出院时间
    private long cysj;
    // 住院天数
    private Integer zyts;
    // 入院诊断
    private String ryzd;
    // 门诊诊断
    private String mzzd;
    // 出院诊断
    private String cyzd;
    // 入院时主要症状及体征
    private String ryzztz;
    // 实验室检查及主要会诊
    private String jchz;
    // 住院期间特殊检查
    private String tsjc;
    // 诊疗过程
    private String zlgc;
    // 合并症
    private String hbz;
    // 出院时情况
    private String cyqk;
    // 出院医嘱
    private String cyyz;
    // 治疗结果
    private String zljg;
    //治疗结果名称
    private String zljgmc;
    // 出院时症状与体征
    private String cyszzytz;
    // 阳性辅助检査结果
    private String yxfzjjg;

    // 诊断信息
    List<HisDataZdxx> zdxx;    //HisDataZdxx
    // 医嘱信息
    List<HisDataYzxx> yzxx;

    public String getJzrq() {
        return jzrq;
    }

    public void setJzrq(String jzrq) {
        this.jzrq = jzrq;
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

    public String getJzlx() {
        return jzlx;
    }

    public void setJzlx(String jzlx) {
        this.jzlx = jzlx;
    }

    public String getJzlxmc() {
        return jzlxmc;
    }

    public void setJzlxmc(String jzlxmc) {
        this.jzlxmc = jzlxmc;
    }

    public String getZdbm0() {
        return zdbm0;
    }

    public void setZdbm0(String zdbm0) {
        this.zdbm0 = zdbm0;
    }

    public String getZdmc() {
        return zdmc;
    }

    public void setZdmc(String zdmc) {
        this.zdmc = zdmc;
    }

    public String getBmlx0() {
        return bmlx0;
    }

    public void setBmlx0(String bmlx0) {
        this.bmlx0 = bmlx0;
    }

    public String getRyks() {
        return ryks;
    }

    public void setRyks(String ryks) {
        this.ryks = ryks;
    }

    public String getRyksmc() {
        return ryksmc;
    }

    public void setRyksmc(String ryksmc) {
        this.ryksmc = ryksmc;
    }

    public String getCyks() {
        return cyks;
    }

    public void setCyks(String cyks) {
        this.cyks = cyks;
    }

    public String getCyksmc() {
        return cyksmc;
    }

    public void setCyksmc(String cyksmc) {
        this.cyksmc = cyksmc;
    }

    public String getRysj() {
        return rysj;
    }

    public void setRysj(String rysj) {
        this.rysj = rysj;
    }

    public long getCysj() {
        return cysj;
    }

    public void setCysj(long cysj) {
        this.cysj = cysj;
    }

    public String getYljgdm() {
        return yljgdm;
    }

    public void setYljgdm(String yljgdm) {
        this.yljgdm = yljgdm;
    }

    public Integer getZyts() {
        return zyts;
    }

    public void setZyts(Integer zyts) {
        this.zyts = zyts;
    }

    public String getRyzd() {
        return ryzd;
    }

    public void setRyzd(String ryzd) {
        this.ryzd = ryzd;
    }

    public String getMzzd() {
        return mzzd;
    }

    public void setMzzd(String mzzd) {
        this.mzzd = mzzd;
    }

    public String getCyzd() {
        return cyzd;
    }

    public void setCyzd(String cyzd) {
        this.cyzd = cyzd;
    }

    public String getRyzztz() {
        return ryzztz;
    }

    public void setRyzztz(String ryzztz) {
        this.ryzztz = ryzztz;
    }

    public String getJchz() {
        return jchz;
    }

    public void setJchz(String jchz) {
        this.jchz = jchz;
    }

    public String getTsjc() {
        return tsjc;
    }

    public void setTsjc(String tsjc) {
        this.tsjc = tsjc;
    }

    public String getZlgc() {
        return zlgc;
    }

    public void setZlgc(String zlgc) {
        this.zlgc = zlgc;
    }

    public String getHbz() {
        return hbz;
    }

    public void setHbz(String hbz) {
        this.hbz = hbz;
    }

    public String getCyqk() {
        return cyqk;
    }

    public void setCyqk(String cyqk) {
        this.cyqk = cyqk;
    }

    public String getCyyz() {
        return cyyz;
    }

    public void setCyyz(String cyyz) {
        this.cyyz = cyyz;
    }

    public String getZljg() {
        return zljg;
    }

    public void setZljg(String zljg) {
        this.zljg = zljg;
    }

    public String getZljgmc() {
        return zljgmc;
    }

    public void setZljgmc(String zljgmc) {
        this.zljgmc = zljgmc;
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

    public String getCyszzytz() {
        return cyszzytz;
    }

    public void setCyszzytz(String cyszzytz) {
        this.cyszzytz = cyszzytz;
    }

    public String getYxfzjjg() {
        return yxfzjjg;
    }

    public void setYxfzjjg(String yxfzjjg) {
        this.yxfzjjg = yxfzjjg;
    }

    public List<HisDataZdxx> getZdxx() {
        return zdxx;
    }

    public void setZdxx(List<HisDataZdxx> zdxx) {
        this.zdxx = zdxx;
    }

    public List<HisDataYzxx> getYzxx() {
        return yzxx;
    }

    public void setYzxx(List<HisDataYzxx> yzxx) {
        this.yzxx = yzxx;
    }


    public TbCisMain getCismain() {
        return cismain;
    }

    public void setCismain(TbCisMain cismain) {
        this.cismain = cismain;
    }


    @Override
    public String toString() {
        return "HisBothRecord{" +
                "cismain'"+cismain.toString()+'\''+
                ", jzlx='" + jzlx + '\'' +
                ", jzrq='" + jzrq + '\'' +
                ", yljgdm='" + yljgdm + '\'' +
                ", jzksdm='" + jzksdm + '\'' +
                ", yyjzksmc='" + yyjzksmc + '\'' +
                ", zzysgh='" + zzysgh + '\'' +
                ", zzysxm='" + zzysxm + '\'' +
                ", mzzdbm='" + mzzdbm + '\'' +
                ", yymzzdbmmc='" + yymzzdbmmc + '\'' +
                ", bmlx='" + bmlx + '\'' +
                ", mzzdsm='" + mzzdsm + '\'' +
                ", zs='" + zs + '\'' +
                ", zzms='" + zzms + '\'' +
                ", tgjc='" + tgjc + '\'' +
                ", cfxx=" + cfxx +
                ", jzlxmc='" + jzlxmc + '\'' +
                ", zdbm0='" + zdbm0 + '\'' +
                ", zdmc='" + zdmc + '\'' +
                ", bmlx0='" + bmlx0 + '\'' +
                ", ryks='" + ryks + '\'' +
                ", ryksmc='" + ryksmc + '\'' +
                ", cyks='" + cyks + '\'' +
                ", cyksmc='" + cyksmc + '\'' +
                ", rysj='" + rysj + '\'' +
                ", cysj=" + cysj +
                ", zyts=" + zyts +
                ", ryzd='" + ryzd + '\'' +
                ", mzzd='" + mzzd + '\'' +
                ", cyzd='" + cyzd + '\'' +
                ", ryzztz='" + ryzztz + '\'' +
                ", jchz='" + jchz + '\'' +
                ", tsjc='" + tsjc + '\'' +
                ", zlgc='" + zlgc + '\'' +
                ", hbz='" + hbz + '\'' +
                ", cyqk='" + cyqk + '\'' +
                ", cyyz='" + cyyz + '\'' +
                ", zljg='" + zljg + '\'' +
                ", zljgmc='" + zljgmc + '\'' +
                ", cyszzytz='" + cyszzytz + '\'' +
                ", yxfzjjg='" + yxfzjjg + '\'' +
                ", zdxx=" + zdxx +
                ", yzxx=" + yzxx +
                '}';
    }
}
