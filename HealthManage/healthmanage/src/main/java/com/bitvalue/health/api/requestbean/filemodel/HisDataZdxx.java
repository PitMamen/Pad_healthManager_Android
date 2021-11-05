package com.bitvalue.health.api.requestbean.filemodel;

/**
 * @ClassName HisDataZdxx
 * @Description: 诊断信息
 * @Author ZhangFayu
 * @Date 2019/9/24
 * @Version V1.0
 **/
public class HisDataZdxx {
    // 。诊断类型区分
    private String zdlxqf;
    // 。诊断类别代码
    private String zdlbdm;
    // 。医院诊断类别代码名称
    private String zdlbmc;
    // 。诊断时间
    private long zdsj;
    // 。诊断编码
    private String zdbm;
    // 。医院诊断编码名称 2019-10-14 增加
    private String zdbmmc;
    // 。诊断编码类型
    private String bmlx;
    // 。诊断说明
    private String zdsm;
    // 。主要诊断标志
    private String zyzdbz;
    // 。疑似诊断标志
    private String yzdbz;
    // 。入院病情
    private String rybq;
    // 。出院情况编码
    private String cyqkbm;

    public String getZdlxqf() {
        return zdlxqf;
    }

    public void setZdlxqf(String zdlxqf) {
        this.zdlxqf = zdlxqf;
    }

    public String getZdlbdm() {
        return zdlbdm;
    }

    public void setZdlbdm(String zdlbdm) {
        this.zdlbdm = zdlbdm;
    }

    public String getZdlbmc() {
        return zdlbmc;
    }

    public void setZdlbmc(String zdlbmc) {
        this.zdlbmc = zdlbmc;
    }

    public long getZdsj() {
        return zdsj;
    }

    public void setZdsj(long zdsj) {
        this.zdsj = zdsj;
    }

    public String getZdbm() {
        return zdbm;
    }

    public void setZdbm(String zdbm) {
        this.zdbm = zdbm;
    }

    public String getZdbmmc() {
        return zdbmmc;
    }

    public void setZdbmmc(String zdbmmc) {
        this.zdbmmc = zdbmmc;
    }

    public String getBmlx() {
        return bmlx;
    }

    public void setBmlx(String bmlx) {
        this.bmlx = bmlx;
    }

    public String getZdsm() {
        return zdsm;
    }

    public void setZdsm(String zdsm) {
        this.zdsm = zdsm;
    }

    public String getZyzdbz() {
        return zyzdbz;
    }

    public void setZyzdbz(String zyzdbz) {
        this.zyzdbz = zyzdbz;
    }

    public String getYzdbz() {
        return yzdbz;
    }

    public void setYzdbz(String yzdbz) {
        this.yzdbz = yzdbz;
    }

    public String getRybq() {
        return rybq;
    }

    public void setRybq(String rybq) {
        this.rybq = rybq;
    }

    public String getCyqkbm() {
        return cyqkbm;
    }

    public void setCyqkbm(String cyqkbm) {
        this.cyqkbm = cyqkbm;
    }
}
