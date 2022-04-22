package com.bitvalue.health.api.requestbean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 04/22
 */
public class RequestPlanPatientListBean  implements Serializable {
    public String userName;
    public String deptCode;
    public int pageNo;
    public int pageSize;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
