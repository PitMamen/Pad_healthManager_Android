package com.bitvalue.health.api.requestbean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 01/18
 */
public class AllocatedPatientRequest implements Serializable {
    public String userName;
    public String bqmc;
    public String cyzd;
    public String existsPlanFlag;   //1  已分配   2  未分配
    public int pageNo;
    public int pageSize;


}
