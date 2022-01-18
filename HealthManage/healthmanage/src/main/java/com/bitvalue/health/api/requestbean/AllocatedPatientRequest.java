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
    public String existsPlanFlag;
    public String isRegister;
    public int pageNo;
    public int pageSize;


}
