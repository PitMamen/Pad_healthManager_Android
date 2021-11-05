package com.bitvalue.health.api.requestbean.filemodel;

/**
 * @ClassName HisDataSfxx
 * @Description: 收费信息
 * @Author ZhangFayu
 * @Date 2019/9/24
 * @Version V1.0
 **/
public class HisDataSfxx {
    // 。退费标志  1：收费；2：退费
    private String tfbz;
    // 。处方号
    private String cfidh;
    // 。相关医嘱ID
    private String yzid;
    // 。发票号
    private String fph;

    // 01 = 住院费
    //02 = 诊疗费
    //03 = 治疗费
    //04 = 护理费
    //05 = 手术费
    //06 = 检查费
    //07 = 化验费
    //08 = 摄片费
    //09 = 透视费
    //10 = 输血费
    //11 = 输氧费
    //12 = 西药费
    //13 = 中成药费
    //14 = 中草药费
    //15 = 其它费用
    // 。明细费用类别
    private String mxfylb;
    // 。医院明细费用类别名称
    private String mxfylbmc;
    // 。收费/退费时间
    private long stfsj;
    // 。明细项目编码
    private String mxxmbm;
    // 。平台明细项目名称
    private String mxxmmc;
    // 。明细项目单位
    private String mxxmdw;
    // 。明细项目单价
    private String mxxmdj;
    // 。明细项目数量
    private Double mxxmsl;
    // 。明细项目金额
    private Double mxxmje;

    public String getTfbz() {
        return tfbz;
    }

    public void setTfbz(String tfbz) {
        this.tfbz = tfbz;
    }

    public String getCfidh() {
        return cfidh;
    }

    public void setCfidh(String cfidh) {
        this.cfidh = cfidh;
    }

    public String getYzid() {
        return yzid;
    }

    public void setYzid(String yzid) {
        this.yzid = yzid;
    }

    public String getFph() {
        return fph;
    }

    public void setFph(String fph) {
        this.fph = fph;
    }

    public String getMxfylb() {
        return mxfylb;
    }

    public void setMxfylb(String mxfylb) {
        this.mxfylb = mxfylb;
    }

    public String getMxfylbmc() {
        return mxfylbmc;
    }

    public void setMxfylbmc(String mxfylbmc) {
        this.mxfylbmc = mxfylbmc;
    }

    public long getStfsj() {
        return stfsj;
    }

    public void setStfsj(long stfsj) {
        this.stfsj = stfsj;
    }

    public String getMxxmbm() {
        return mxxmbm;
    }

    public void setMxxmbm(String mxxmbm) {
        this.mxxmbm = mxxmbm;
    }

    public String getMxxmmc() {
        return mxxmmc;
    }

    public void setMxxmmc(String mxxmmc) {
        this.mxxmmc = mxxmmc;
    }

    public String getMxxmdw() {
        return mxxmdw;
    }

    public void setMxxmdw(String mxxmdw) {
        this.mxxmdw = mxxmdw;
    }

    public String getMxxmdj() {
        return mxxmdj;
    }

    public void setMxxmdj(String mxxmdj) {
        this.mxxmdj = mxxmdj;
    }

    public Double getMxxmsl() {
        return mxxmsl;
    }

    public void setMxxmsl(Double mxxmsl) {
        this.mxxmsl = mxxmsl;
    }

    public Double getMxxmje() {
        return mxxmje;
    }

    public void setMxxmje(Double mxxmje) {
        this.mxxmje = mxxmje;
    }
}
