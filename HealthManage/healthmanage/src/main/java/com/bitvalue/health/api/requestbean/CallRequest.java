package com.bitvalue.health.api.requestbean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 04/29
 */
public class CallRequest implements Serializable {
    public String distPhone;  //患者号码
    public String sourcePhone;//
    public long continueTime; //剩余通话时间

}
