package com.bitvalue.health.api.requestbean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 04/29
 */
public class CallRequest implements Serializable {
   public String distPhone;  //患者号码
  public String endTime; //结束时间  先不填
  public String sourcePhone;// 医生电话
  public String startTime;//开始时间 先不填

}
