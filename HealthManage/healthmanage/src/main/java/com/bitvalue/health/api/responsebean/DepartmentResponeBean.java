package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 01/26
 */
public class DepartmentResponeBean implements Serializable {
    private int departmentId;
    private String departmentName;
    private int hospitalId;
    private int parentId;
    private List<ChildrenDTO> children;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<ChildrenDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenDTO> children) {
        this.children = children;
    }

    public static class ChildrenDTO {
        private int departmentId;
        private String departmentName;
        private int hospitalId;
        private int parentId;
        private Object children;

        public int getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(int departmentId) {
            this.departmentId = departmentId;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public int getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(int hospitalId) {
            this.hospitalId = hospitalId;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public Object getChildren() {
            return children;
        }

        public void setChildren(Object children) {
            this.children = children;
        }
    }
}
