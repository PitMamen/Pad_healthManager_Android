package com.bitvalue.health.api.requestbean.filemodel;

/**
 * @ClassName HisDataYqjc
 * @Description: 仪器检查
 * @Author ZhangFayu
 * @Date 2019/9/24
 * @Version V1.0
 **/
public class HisDataYqjc {
    // 。检查项目代码
    private String jcxmdm;
    // 。检查名称
    private String jcmc;
    // 。检查时间
    private long jcsj;
    // 。检查类型
    private String jclx;
    // 。检查类型名称
    private String jclxmc;
    // 。申请科室编码
    private String sqks;
    // 。申请科室名称
    private String sqksmc;
    // 。检查科室编码
    private String jcks;
    // 。检查科室名称
    private String jcksmc;
    // 。报告日期
    private String bgrq;
    // 。报告时间
    private long bgsj;
    // 。报告人工号
    private String bgrgh;
    // 。报告人姓名
    private String bgrxm;
    // 。检查部位与方法
    private String jcbwff;
    // 。检查部位ACR编码
    private String jcbwacr;
    // 。影像表现或检查所见
    private String yxbxjcsj;
    // 。检查诊断或提示
    private String yxzdts;
    // 。备注或建议
    private String bzhjy;

    public String getJcxmdm() {
        return jcxmdm;
    }

    public void setJcxmdm(String jcxmdm) {
        this.jcxmdm = jcxmdm;
    }

    public String getJcmc() {
        return jcmc;
    }

    public void setJcmc(String jcmc) {
        this.jcmc = jcmc;
    }

    public long getJcsj() {
        return jcsj;
    }

    public void setJcsj(long jcsj) {
        this.jcsj = jcsj;
    }

    public String getJclx() {
        return jclx;
    }

    public void setJclx(String jclx) {
        this.jclx = jclx;
    }

    public String getJclxmc() {
        return jclxmc;
    }

    public void setJclxmc(String jclxmc) {
        this.jclxmc = jclxmc;
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

    public String getJcks() {
        return jcks;
    }

    public void setJcks(String jcks) {
        this.jcks = jcks;
    }

    public String getJcksmc() {
        return jcksmc;
    }

    public void setJcksmc(String jcksmc) {
        this.jcksmc = jcksmc;
    }

    public String getBgrq() {
        return bgrq;
    }

    public void setBgrq(String bgrq) {
        this.bgrq = bgrq;
    }

    public long getBgsj() {
        return bgsj;
    }

    public void setBgsj(long bgsj) {
        this.bgsj = bgsj;
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

    public String getJcbwff() {
        return jcbwff;
    }

    public void setJcbwff(String jcbwff) {
        this.jcbwff = jcbwff;
    }

    public String getJcbwacr() {
        return jcbwacr;
    }

    public void setJcbwacr(String jcbwacr) {
        this.jcbwacr = jcbwacr;
    }

    public String getYxbxjcsj() {
        return yxbxjcsj;
    }

    public void setYxbxjcsj(String yxbxjcsj) {
        this.yxbxjcsj = yxbxjcsj;
    }

    public String getYxzdts() {
        return yxzdts;
    }

    public void setYxzdts(String yxzdts) {
        this.yxzdts = yxzdts;
    }

    public String getBzhjy() {
        return bzhjy;
    }

    public void setBzhjy(String bzhjy) {
        this.bzhjy = bzhjy;
    }

    @Override
    public String toString() {
        return "HisDataYqjc{" +
                "jcxmdm='" + jcxmdm + '\'' +
                ", jcmc='" + jcmc + '\'' +
                ", jcsj=" + jcsj +
                ", jclx='" + jclx + '\'' +
                ", jclxmc='" + jclxmc + '\'' +
                ", sqks='" + sqks + '\'' +
                ", sqksmc='" + sqksmc + '\'' +
                ", jcks='" + jcks + '\'' +
                ", jcksmc='" + jcksmc + '\'' +
                ", bgrq='" + bgrq + '\'' +
                ", bgsj=" + bgsj +
                ", bgrgh='" + bgrgh + '\'' +
                ", bgrxm='" + bgrxm + '\'' +
                ", jcbwff='" + jcbwff + '\'' +
                ", jcbwacr='" + jcbwacr + '\'' +
                ", yxbxjcsj='" + yxbxjcsj + '\'' +
                ", yxzdts='" + yxzdts + '\'' +
                ", bzhjy='" + bzhjy + '\'' +
                '}';
    }
}
