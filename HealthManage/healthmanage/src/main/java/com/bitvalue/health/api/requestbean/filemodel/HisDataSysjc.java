package com.bitvalue.health.api.requestbean.filemodel;

import java.util.List;

/**
 * @ClassName HisDataSysjc
 * @Description: 实验室检验
 * @Author ZhangFayu
 * @Date 2019/9/24
 * @Version V1.0
 **/
public class HisDataSysjc {
    // 。检验报告单号 2019-10-14 增加
    private String bgdh;
    // 。报告人工号
    private String bgrgh;
    // 。报告人姓名
    private String bgrxm;
    // 。申请科室编码
    private String sqks;
    // 。申请科室名称
    private String sqksmc;
    // 。报告日期
    private String bgrq;
    // 。采集日期
    private long cjrq;
    // 。检验日期
    private long jyrq;
    // 。平台标本代码
    private String ptbbdm;
    // 。医院标本名称
    private String yybbmc;
    // 。报告单类别编码
    private String bgdlbbm;
    // 。报告单类别名称
    private String bgdlbmc;
    // 。报告备注
    private String bgbz;

    // 。检验结果指标 list
    List<HisDataJyjgzb> jyjgzb;
    // 。细菌结果 list
    List<HisDataXjjg> xjjg;
    // 。药敏结果 list
    List<HisDataYmjg> ymjg;

    public String getBgdh() {
        return bgdh;
    }

    public void setBgdh(String bgdh) {
        this.bgdh = bgdh;
    }

    public String getBgrgh() {
        return bgrgh;
    }

    public void setBgrgh(String bgrgh) {
        this.bgrgh = bgrgh;
    }

    public String getBgrxm() {
        return bgrxm;
    }

    public void setBgrxm(String bgrxm) {
        this.bgrxm = bgrxm;
    }

    public String getSqks() {
        return sqks;
    }

    public void setSqks(String sqks) {
        this.sqks = sqks;
    }

    public String getSqksmc() {
        return sqksmc;
    }

    public void setSqksmc(String sqksmc) {
        this.sqksmc = sqksmc;
    }

    public String getBgrq() {
        return bgrq;
    }

    public void setBgrq(String bgrq) {
        this.bgrq = bgrq;
    }

    public long getCjrq() {
        return cjrq;
    }

    public void setCjrq(long cjrq) {
        this.cjrq = cjrq;
    }

    public long getJyrq() {
        return jyrq;
    }

    public void setJyrq(long jyrq) {
        this.jyrq = jyrq;
    }

    public String getPtbbdm() {
        return ptbbdm;
    }

    public void setPtbbdm(String ptbbdm) {
        this.ptbbdm = ptbbdm;
    }

    public String getYybbmc() {
        return yybbmc;
    }

    public void setYybbmc(String yybbmc) {
        this.yybbmc = yybbmc;
    }

    public String getBgdlbbm() {
        return bgdlbbm;
    }

    public void setBgdlbbm(String bgdlbbm) {
        this.bgdlbbm = bgdlbbm;
    }

    public String getBgdlbmc() {
        return bgdlbmc;
    }

    public void setBgdlbmc(String bgdlbmc) {
        this.bgdlbmc = bgdlbmc;
    }

    public String getBgbz() {
        return bgbz;
    }

    public void setBgbz(String bgbz) {
        this.bgbz = bgbz;
    }

    public List<HisDataJyjgzb> getJyjgzb() {
        return jyjgzb;
    }

    public void setJyjgzb(List<HisDataJyjgzb> jyjgzb) {
        this.jyjgzb = jyjgzb;
    }

    public List<HisDataXjjg> getXjjg() {
        return xjjg;
    }

    public void setXjjg(List<HisDataXjjg> xjjg) {
        this.xjjg = xjjg;
    }

    public List<HisDataYmjg> getYmjg() {
        return ymjg;
    }

    public void setYmjg(List<HisDataYmjg> ymjg) {
        this.ymjg = ymjg;
    }

    @Override
    public String toString() {
        return "HisDataSysjc{" +
                "bgdh='" + bgdh + '\'' +
                ", bgrgh='" + bgrgh + '\'' +
                ", bgrxm='" + bgrxm + '\'' +
                ", sqks='" + sqks + '\'' +
                ", sqksmc='" + sqksmc + '\'' +
                ", bgrq='" + bgrq + '\'' +
                ", cjrq=" + cjrq +
                ", jyrq=" + jyrq +
                ", ptbbdm='" + ptbbdm + '\'' +
                ", yybbmc='" + yybbmc + '\'' +
                ", bgdlbbm='" + bgdlbbm + '\'' +
                ", bgdlbmc='" + bgdlbmc + '\'' +
                ", bgbz='" + bgbz + '\'' +
                ", jyjgzb=" + jyjgzb +
                ", xjjg=" + xjjg +
                ", ymjg=" + ymjg +
                '}';
    }
}
