package com.bitvalue.health.api.requestbean.filemodel;

/**
 * @ClassName HisDataYmjg
 * @Description: 药敏结果
 * @Author ZhangFayu
 * @Date 2019/9/24
 * @Version V1.0
 **/
public class HisDataYmjg {
    // 。。细菌代号
    private String xjdh;
    // 。。药敏代码
    private String ymdm;
    // 。。药敏名称
    private String ymmc;
    // 。。检测结果描述
    private String jcjg;
    // 。。纸片含药量
    private String zphyl;
    // 。。抑菌浓度
    private String yjnd;
    // 。。抑菌环直径
    private String yjhzj;

    public String getXjdh() {
        return xjdh;
    }

    public void setXjdh(String xjdh) {
        this.xjdh = xjdh;
    }

    public String getYmdm() {
        return ymdm;
    }

    public void setYmdm(String ymdm) {
        this.ymdm = ymdm;
    }

    public String getYmmc() {
        return ymmc;
    }

    public void setYmmc(String ymmc) {
        this.ymmc = ymmc;
    }

    public String getJcjg() {
        return jcjg;
    }

    public void setJcjg(String jcjg) {
        this.jcjg = jcjg;
    }

    public String getZphyl() {
        return zphyl;
    }

    public void setZphyl(String zphyl) {
        this.zphyl = zphyl;
    }

    public String getYjnd() {
        return yjnd;
    }

    public void setYjnd(String yjnd) {
        this.yjnd = yjnd;
    }

    public String getYjhzj() {
        return yjhzj;
    }

    public void setYjhzj(String yjhzj) {
        this.yjhzj = yjhzj;
    }
}
