package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 01/27
 */
public class InpatientBean implements Serializable {
    private int id;
    private String departmentId;
    private String inpatientAreaName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getInpatientAreaName() {
        return inpatientAreaName;
    }

    public void setInpatientAreaName(String inpatientAreaName) {
        this.inpatientAreaName = inpatientAreaName;
    }


    @Override
    public String toString() {
        return "InpatientBean{" +
                "id=" + id +
                ", departmentId='" + departmentId + '\'' +
                ", inpatientAreaName='" + inpatientAreaName + '\'' +
                '}';
    }
}
