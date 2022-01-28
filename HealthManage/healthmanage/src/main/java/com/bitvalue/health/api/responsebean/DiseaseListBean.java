package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 01/27
 */
public class DiseaseListBean implements Serializable {
    public int id;
    public int departmentId;
    public String diseaseName;


    @Override
    public String toString() {
        return "DiseaseList{" +
                "id=" + id +
                ", departmentId=" + departmentId +
                ", diseaseName='" + diseaseName + '\'' +
                '}';
    }
}
