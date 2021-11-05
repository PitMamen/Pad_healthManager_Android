package com.bitvalue.health.api.requestbean.filemodel;

/**
 * @ClassName HisDataJyjgzb
 * @Description: 检验结果指标
 * @Author ZhangFayu
 * @Date 2019/9/24
 * @Version V1.0
 **/
public class HisDataJyjgzb {
    // 。。检测指标代码
    private String jczbdm;
    // 。。医院检测指标名称
    private String yyjczbmc;
    // 。。检测方法
    private String jcff;
    // 。。检测指标名称
    private String jczbmc;
    // 。。检测指标结果
    private String jczbjg;
    // 。。LOINC编码
    private String loinc;
    // 。。参考值范围
    private String ckz;
    // 。。计量单位
    private String jldw;
    // 。。异常提示
    private String ycts;
    // 。。检验定量结果
    private Double jydljg;
    // 。。检验定量结果计算单位
    private String jydljgjsdw;
    // 。。检验报告结果
    private String jybgjg;
    // 。。检验报告备注
    private String jybgbz;

    public String getJczbdm() {
        return jczbdm;
    }

    public void setJczbdm(String jczbdm) {
        this.jczbdm = jczbdm;
    }

    public String getYyjczbmc() {
        return yyjczbmc;
    }

    public void setYyjczbmc(String yyjczbmc) {
        this.yyjczbmc = yyjczbmc;
    }

    public String getJcff() {
        return jcff;
    }

    public void setJcff(String jcff) {
        this.jcff = jcff;
    }

    public String getJczbmc() {
        return jczbmc;
    }

    public void setJczbmc(String jczbmc) {
        this.jczbmc = jczbmc;
    }

    public String getJczbjg() {
        return jczbjg;
    }

    public void setJczbjg(String jczbjg) {
        this.jczbjg = jczbjg;
    }

    public String getLoinc() {
        return loinc;
    }

    public void setLoinc(String loinc) {
        this.loinc = loinc;
    }

    public String getCkz() {
        return ckz;
    }

    public void setCkz(String ckz) {
        this.ckz = ckz;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getYcts() {
        return ycts;
    }

    public void setYcts(String ycts) {
        this.ycts = ycts;
    }

    public Double getJydljg() {
        return jydljg;
    }

    public void setJydljg(Double jydljg) {
        this.jydljg = jydljg;
    }

    public String getJydljgjsdw() {
        return jydljgjsdw;
    }

    public void setJydljgjsdw(String jydljgjsdw) {
        this.jydljgjsdw = jydljgjsdw;
    }

    public String getJybgjg() {
        return jybgjg;
    }

    public void setJybgjg(String jybgjg) {
        this.jybgjg = jybgjg;
    }

    public String getJybgbz() {
        return jybgbz;
    }

    public void setJybgbz(String jybgbz) {
        this.jybgbz = jybgbz;
    }

    @Override
    public String toString() {
        return "HisDataJyjgzb{" +
                "jczbdm='" + jczbdm + '\'' +
                ", yyjczbmc='" + yyjczbmc + '\'' +
                ", jcff='" + jcff + '\'' +
                ", jczbmc='" + jczbmc + '\'' +
                ", jczbjg='" + jczbjg + '\'' +
                ", loinc='" + loinc + '\'' +
                ", ckz='" + ckz + '\'' +
                ", jldw='" + jldw + '\'' +
                ", ycts='" + ycts + '\'' +
                ", jydljg=" + jydljg +
                ", jydljgjsdw='" + jydljgjsdw + '\'' +
                ", jybgjg='" + jybgjg + '\'' +
                ", jybgbz='" + jybgbz + '\'' +
                '}';
    }
}
