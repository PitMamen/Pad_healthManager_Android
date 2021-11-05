package com.bitvalue.health.api.requestbean.filemodel;

/**
 * @ClassName HisDataYzxx
 * @Description: 医嘱信息
 * @Author ZhangFayu
 * @Date 2019/9/24
 * @Version V1.0
 **/
public class HisDataYzxx {
    // 。撤销标志
    private String cxbz;
    // 。病区
    private String bq;
    // 。下达科室编码
    private String xdksbm;
    // 。下达科室名称
    private String xdksmc;
    // 。医嘱下达人工号
    private String xdrgh;
    // 。医嘱下达人姓名
    private String xdrxm;
    // 。医嘱下达时间
    private long yzxdsj;
    // 。执行科室编码
    private String zxksbm;
    // 。执行科室名称
    private String zxksmc;
    // 。医嘱执行时间
    private long yzzxsj;
    // 。医嘱终止时间
    private long yzzzsj;
    // 。医嘱说明
    private String yzsm;
    // 。医嘱组号
    private String yzzh;
    // 。医嘱类别
    private String yzlb;
    // 。医嘱明细编码
    private String yzmxbm;
    // 。医嘱明细名称
    private String yzmxmc;
    // 。是否药品
    private String yzlx;
    // 。药品规格
    private String ypgg;
    // 。药品用法
    private String ypyf;
    // 。用药频度
    private String yypd;
    // 。每次使用剂量
    private String mcsyjl;
    // 。每次使用剂量单位
    private String mcsyjldw;
    // 。每次使用数量
    private Double mcsl;
    // 。每次使用数量单位
    private String mcsldw;
    // 。给药途径(用法)
    private String yf;
    // 。用药天数
    private Double yyts;
    // 。皮试判别
    private String pspb;
    // 。发药数量
    private Double ypsl;
    // 。发药数量单位
    private String ypdw;
    // 。中药煎煮法代码
    private String jydm;
    // 。检查部位
    private String jcbw;
    // 。医嘱备注信息
    private String yzbzxx;
    // 。医嘱取消日期时间
    private long yzqxrqsj;
    // 。备注
    private String bz;

    public String getCxbz() {
        return cxbz;
    }

    public void setCxbz(String cxbz) {
        this.cxbz = cxbz;
    }

    public String getBq() {
        return bq;
    }

    public void setBq(String bq) {
        this.bq = bq;
    }

    public String getXdksbm() {
        return xdksbm;
    }

    public void setXdksbm(String xdksbm) {
        this.xdksbm = xdksbm;
    }

    public String getXdksmc() {
        return xdksmc;
    }

    public void setXdksmc(String xdksmc) {
        this.xdksmc = xdksmc;
    }

    public String getXdrgh() {
        return xdrgh;
    }

    public void setXdrgh(String xdrgh) {
        this.xdrgh = xdrgh;
    }

    public String getXdrxm() {
        return xdrxm;
    }

    public void setXdrxm(String xdrxm) {
        this.xdrxm = xdrxm;
    }

    public long getYzxdsj() {
        return yzxdsj;
    }

    public void setYzxdsj(long yzxdsj) {
        this.yzxdsj = yzxdsj;
    }

    public String getZxksbm() {
        return zxksbm;
    }

    public void setZxksbm(String zxksbm) {
        this.zxksbm = zxksbm;
    }

    public String getZxksmc() {
        return zxksmc;
    }

    public void setZxksmc(String zxksmc) {
        this.zxksmc = zxksmc;
    }

    public long getYzzxsj() {
        return yzzxsj;
    }

    public void setYzzxsj(long yzzxsj) {
        this.yzzxsj = yzzxsj;
    }

    public long getYzzzsj() {
        return yzzzsj;
    }

    public void setYzzzsj(long yzzzsj) {
        this.yzzzsj = yzzzsj;
    }

    public String getYzsm() {
        return yzsm;
    }

    public void setYzsm(String yzsm) {
        this.yzsm = yzsm;
    }

    public String getYzzh() {
        return yzzh;
    }

    public void setYzzh(String yzzh) {
        this.yzzh = yzzh;
    }

    public String getYzlb() {
        return yzlb;
    }

    public void setYzlb(String yzlb) {
        this.yzlb = yzlb;
    }

    public String getYzmxbm() {
        return yzmxbm;
    }

    public void setYzmxbm(String yzmxbm) {
        this.yzmxbm = yzmxbm;
    }

    public String getYzmxmc() {
        return yzmxmc;
    }

    public void setYzmxmc(String yzmxmc) {
        this.yzmxmc = yzmxmc;
    }

    public String getYzlx() {
        return yzlx;
    }

    public void setYzlx(String yzlx) {
        this.yzlx = yzlx;
    }

    public String getYpgg() {
        return ypgg;
    }

    public void setYpgg(String ypgg) {
        this.ypgg = ypgg;
    }

    public String getYpyf() {
        return ypyf;
    }

    public void setYpyf(String ypyf) {
        this.ypyf = ypyf;
    }

    public String getYypd() {
        return yypd;
    }

    public void setYypd(String yypd) {
        this.yypd = yypd;
    }

    public String getMcsyjl() {
        return mcsyjl;
    }

    public void setMcsyjl(String mcsyjl) {
        this.mcsyjl = mcsyjl;
    }

    public String getMcsyjldw() {
        return mcsyjldw;
    }

    public void setMcsyjldw(String mcsyjldw) {
        this.mcsyjldw = mcsyjldw;
    }

    public Double getMcsl() {
        return mcsl;
    }

    public void setMcsl(Double mcsl) {
        this.mcsl = mcsl;
    }

    public String getMcsldw() {
        return mcsldw;
    }

    public void setMcsldw(String mcsldw) {
        this.mcsldw = mcsldw;
    }

    public String getYf() {
        return yf;
    }

    public void setYf(String yf) {
        this.yf = yf;
    }

    public Double getYyts() {
        return yyts;
    }

    public void setYyts(Double yyts) {
        this.yyts = yyts;
    }

    public String getPspb() {
        return pspb;
    }

    public void setPspb(String pspb) {
        this.pspb = pspb;
    }

    public Double getYpsl() {
        return ypsl;
    }

    public void setYpsl(Double ypsl) {
        this.ypsl = ypsl;
    }

    public String getYpdw() {
        return ypdw;
    }

    public void setYpdw(String ypdw) {
        this.ypdw = ypdw;
    }

    public String getJydm() {
        return jydm;
    }

    public void setJydm(String jydm) {
        this.jydm = jydm;
    }

    public String getJcbw() {
        return jcbw;
    }

    public void setJcbw(String jcbw) {
        this.jcbw = jcbw;
    }

    public String getYzbzxx() {
        return yzbzxx;
    }

    public void setYzbzxx(String yzbzxx) {
        this.yzbzxx = yzbzxx;
    }

    public long getYzqxrqsj() {
        return yzqxrqsj;
    }

    public void setYzqxrqsj(long yzqxrqsj) {
        this.yzqxrqsj = yzqxrqsj;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
