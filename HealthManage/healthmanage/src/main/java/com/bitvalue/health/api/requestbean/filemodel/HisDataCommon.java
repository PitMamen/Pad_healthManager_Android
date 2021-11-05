package com.bitvalue.health.api.requestbean.filemodel;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName HisDataCommon
 * @Description: 门诊、住院数据 公共部分
 * @Author ZhangFayu
 * @Date 2019/9/24
 * @Version V1.0
 **/
public class HisDataCommon {
    // 患者ID 对应表中 卡号
    private String hzid;
    // ID类型 对应表中 卡类型
    private String idlx;
    // 证件号码
    private String zjhm;
    // 证件类型
    private String zjlx;
    // 医院证件类型名称
    private String yyzjlxmc;
    // 姓名
    private String xm;
    // 性别
    private String xb;
    // 医院性别名称
    private String yyxbmc;
    // 年龄
    private Double nl;
    // 出生日期
    private String csrq;
    // 出生地
    private String csd;
    // 居住地址
    private String jzdz;
    // 工作单位邮编
    private String gzdwyb;
    // 工作单位名称
    private String gzdwmc;
    // 工作单位地址
    private String gzdwdz;
    // 婚姻状况
    private String hyzk;
    // 医院婚姻状况名称
    private String yyhyzkmc;
    // 民族
    private String mz;
    // 医院民族名称
    private String yymzmc;
    // 电话号码
    private String dhhm;
    // 手机号码
    private String sjhm;
    // 户口地址
    private String hkdz;
    // 户口地址邮编
    private String hkdzyb;
    // 联系人姓名
    private String lxrxm;
    // 联系人关系
    private String lxrgx;
    // 联系人关系名称
    private String lxrgxmc;
    // 联系人地址
    private String lxrdz;
    // 联系人邮编
    private String lxryb;
    // 联系人电话
    private String lxrdh;
    // 现病史
    private String xbs;
    // 既往史
    private String jws;
    // 个人史
    private String grs;
    // 婚姻史
    private String hys;
    // 过敏史
    private String gms;
    // 家族史
    private String jzs;
    // 记录类型
    private String jllx;

    private long sjzlsj;

    // 仪器检查 list
    List<HisDataYqjc> yqjc;
    // 实验室检验 list
    List<HisDataSysjc> sysjc;
    // 手术信息
    List<HisDataSsxx> ssxx;

    // 收费信息
    List<HisDataSfxx> sfxx;

    public String getHzid() {
        return hzid;
    }

    public void setHzid(String hzid) {
        this.hzid = hzid;
    }

    public String getIdlx() {
        return idlx;
    }

    public void setIdlx(String idlx) {
        this.idlx = idlx;
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

    public String getYyzjlxmc() {
        return yyzjlxmc;
    }

    public void setYyzjlxmc(String yyzjlxmc) {
        this.yyzjlxmc = yyzjlxmc;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getYyxbmc() {
        return yyxbmc;
    }

    public void setYyxbmc(String yyxbmc) {
        this.yyxbmc = yyxbmc;
    }

    public Double getNl() {
        return nl;
    }

    public void setNl(Double nl) {
        this.nl = nl;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getCsd() {
        return csd;
    }

    public void setCsd(String csd) {
        this.csd = csd;
    }

    public String getJzdz() {
        return jzdz;
    }

    public void setJzdz(String jzdz) {
        this.jzdz = jzdz;
    }

    public String getGzdwyb() {
        return gzdwyb;
    }

    public void setGzdwyb(String gzdwyb) {
        this.gzdwyb = gzdwyb;
    }

    public String getGzdwmc() {
        return gzdwmc;
    }

    public void setGzdwmc(String gzdwmc) {
        this.gzdwmc = gzdwmc;
    }

    public String getGzdwdz() {
        return gzdwdz;
    }

    public void setGzdwdz(String gzdwdz) {
        this.gzdwdz = gzdwdz;
    }

    public String getHyzk() {
        return hyzk;
    }

    public void setHyzk(String hyzk) {
        this.hyzk = hyzk;
    }

    public String getYyhyzkmc() {
        return yyhyzkmc;
    }

    public void setYyhyzkmc(String yyhyzkmc) {
        this.yyhyzkmc = yyhyzkmc;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getYymzmc() {
        return yymzmc;
    }

    public void setYymzmc(String yymzmc) {
        this.yymzmc = yymzmc;
    }

    public String getDhhm() {
        return dhhm;
    }

    public void setDhhm(String dhhm) {
        this.dhhm = dhhm;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getHkdz() {
        return hkdz;
    }

    public void setHkdz(String hkdz) {
        this.hkdz = hkdz;
    }

    public String getHkdzyb() {
        return hkdzyb;
    }

    public void setHkdzyb(String hkdzyb) {
        this.hkdzyb = hkdzyb;
    }

    public String getLxrxm() {
        return lxrxm;
    }

    public void setLxrxm(String lxrxm) {
        this.lxrxm = lxrxm;
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

    public String getLxrdz() {
        return lxrdz;
    }

    public void setLxrdz(String lxrdz) {
        this.lxrdz = lxrdz;
    }

    public String getLxryb() {
        return lxryb;
    }

    public void setLxryb(String lxryb) {
        this.lxryb = lxryb;
    }

    public String getLxrdh() {
        return lxrdh;
    }

    public void setLxrdh(String lxrdh) {
        this.lxrdh = lxrdh;
    }

    public String getXbs() {
        return xbs;
    }

    public void setXbs(String xbs) {
        this.xbs = xbs;
    }

    public String getJws() {
        return jws;
    }

    public void setJws(String jws) {
        this.jws = jws;
    }

    public String getGrs() {
        return grs;
    }

    public void setGrs(String grs) {
        this.grs = grs;
    }

    public String getHys() {
        return hys;
    }

    public void setHys(String hys) {
        this.hys = hys;
    }

    public String getGms() {
        return gms;
    }

    public void setGms(String gms) {
        this.gms = gms;
    }

    public String getJzs() {
        return jzs;
    }

    public void setJzs(String jzs) {
        this.jzs = jzs;
    }

    public String getJllx() {
        return jllx;
    }

    public void setJllx(String jllx) {
        this.jllx = jllx;
    }

    public long getSjzlsj() {
        return sjzlsj;
    }

    public void setSjzlsj(long sjzlsj) {
        this.sjzlsj = sjzlsj;
    }

    public List<HisDataYqjc> getYqjc() {
        return yqjc;
    }

    public void setYqjc(List<HisDataYqjc> yqjc) {
        this.yqjc = yqjc;
    }

    public List<HisDataSysjc> getSysjc() {
        return sysjc;
    }

    public void setSysjc(List<HisDataSysjc> sysjc) {
        this.sysjc = sysjc;
    }

    public List<HisDataSsxx> getSsxx() {
        return ssxx;
    }

    public void setSsxx(List<HisDataSsxx> ssxx) {
        this.ssxx = ssxx;
    }


    public List<HisDataSfxx> getSfxx() {
        return sfxx;
    }

    public void setSfxx(List<HisDataSfxx> sfxx) {
        this.sfxx = sfxx;
    }

    public List<Float> getSfxxList(List<HisDataSfxx> sfxxBeanList) {
        //初始化费用列表
        List<Float> floatList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if(i==14){
                floatList.add(0.1f);
            }else {
                floatList.add(0f);
            }
        }

        if(sfxxBeanList==null||sfxxBeanList.size()==0){
            LogUtil.e("我的收费===");
            return floatList;
        }

        for (int i = 0; i < sfxxBeanList.size(); i++) {
            HisDataSfxx sfxx=sfxxBeanList.get(i);
//            if(sfxx!=null){
//                LogMy.e("=类别="+sfxx.getMxfylb()+"==标志="+sfxx.getTfbz());
//            }else {
//                LogMy.e("=类别===空="+i);
//            }

            if(sfxx!=null&&sfxx.getMxfylb()!=null&&sfxx.getTfbz()!=null&&"1".equals(sfxx.getTfbz())){
                double temp=0;
                switch (sfxx.getMxfylb()){
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

                    //一下是数据库字段 转为省级字段
                    // 01 = 住院费
                    //02 = 诊疗费
                    //03 = 治疗费
                    //04 = 护理费
                    //05 = 手术费
                    //06 = 治疗费 =03
                    //07 = 化验费
                    //08 = 摄片费
                    //09 = 透视费
                    //10 = 输血费
                    //11 = 输氧费
                    //12 = 西药费
                    //13 = 中成药费
                    //14 = 中草药费
                    //15 = 其它费用

                    case "01":
                        temp=floatList.get(0)+sfxx.getMxxmje();
                        floatList.set(0,(float) temp);
                        break;
                    case "02":
                        temp=floatList.get(1)+sfxx.getMxxmje();
                        floatList.set(1,(float) temp);
                        break;
                    case "03":
                        temp=floatList.get(2)+sfxx.getMxxmje();
                        floatList.set(2,(float) temp);
                        break;
                    case "04":
                        temp=floatList.get(3)+sfxx.getMxxmje();
                        floatList.set(3,(float) temp);
                        break;
                    case "05":
                        temp=floatList.get(4)+sfxx.getMxxmje();
                        floatList.set(4,(float) temp);
                        break;
                    case "06":
                        temp=floatList.get(5)+sfxx.getMxxmje();
                        floatList.set(5,(float) temp);
                        break;
                    case "07":
                        temp=floatList.get(6)+sfxx.getMxxmje();
                        floatList.set(6,(float) temp);
                        break;
                    case "08":
                        temp=floatList.get(7)+sfxx.getMxxmje();
                        floatList.set(7,(float) temp);
                        break;
                    case "09":
                        temp=floatList.get(8)+sfxx.getMxxmje();
                        floatList.set(8,(float) temp);
                        break;
                    case "10":
                        temp=floatList.get(9)+sfxx.getMxxmje();
                        floatList.set(9,(float) temp);
                        break;
                    case "11":
                        temp=floatList.get(10)+sfxx.getMxxmje();
                        floatList.set(10,(float) temp);
                        break;
                    case "12":
                        temp=floatList.get(11)+sfxx.getMxxmje();
                        floatList.set(11,(float) temp);
                        break;
                    case "13":
                        temp=floatList.get(12)+sfxx.getMxxmje();
                        floatList.set(12,(float) temp);
                        break;
                    case "14":
                        temp=floatList.get(13)+sfxx.getMxxmje();
                        floatList.set(13,(float) temp);
                        break;
                    case "15":
                        temp=floatList.get(14)+sfxx.getMxxmje();
                        floatList.set(14,(float) temp);
                        break;
                }
            }
        }
        LogUtil.e("=真实收费==="+sfxxBeanList.size());
        return floatList;
    }

    @Override
    public String toString() {
        return "HisDataCommon{" +
                "hzid='" + hzid + '\'' +
                ", idlx='" + idlx + '\'' +
                ", zjhm='" + zjhm + '\'' +
                ", zjlx='" + zjlx + '\'' +
                ", yyzjlxmc='" + yyzjlxmc + '\'' +
                ", xm='" + xm + '\'' +
                ", xb='" + xb + '\'' +
                ", yyxbmc='" + yyxbmc + '\'' +
                ", nl=" + nl +
                ", csrq='" + csrq + '\'' +
                ", csd='" + csd + '\'' +
                ", jzdz='" + jzdz + '\'' +
                ", gzdwyb='" + gzdwyb + '\'' +
                ", gzdwmc='" + gzdwmc + '\'' +
                ", gzdwdz='" + gzdwdz + '\'' +
                ", hyzk='" + hyzk + '\'' +
                ", yyhyzkmc='" + yyhyzkmc + '\'' +
                ", mz='" + mz + '\'' +
                ", yymzmc='" + yymzmc + '\'' +
                ", dhhm='" + dhhm + '\'' +
                ", sjhm='" + sjhm + '\'' +
                ", hkdz='" + hkdz + '\'' +
                ", hkdzyb='" + hkdzyb + '\'' +
                ", lxrxm='" + lxrxm + '\'' +
                ", lxrgx='" + lxrgx + '\'' +
                ", lxrgxmc='" + lxrgxmc + '\'' +
                ", lxrdz='" + lxrdz + '\'' +
                ", lxryb='" + lxryb + '\'' +
                ", lxrdh='" + lxrdh + '\'' +
                ", xbs='" + xbs + '\'' +
                ", jws='" + jws + '\'' +
                ", grs='" + grs + '\'' +
                ", hys='" + hys + '\'' +
                ", gms='" + gms + '\'' +
                ", jzs='" + jzs + '\'' +
                ", jllx='" + jllx + '\'' +
                ", sjzlsj=" + sjzlsj +
                ", yqjc=" + yqjc +
                ", sysjc=" + sysjc +
                ", ssxx=" + ssxx +
                ", sfxx=" + sfxx +
                '}';
    }
}
