package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 01/10
 * 病种 实体
 */
public class DiseaseSpeciesBean implements Serializable {

    private String diseasespeciesName;


    public DiseaseSpeciesBean(String diseasespeciesName) {
        this.diseasespeciesName = diseasespeciesName;
    }

    public String getDiseasespeciesName() {
        return diseasespeciesName;
    }

    public void setDiseasespeciesName(String diseasespeciesName) {
        this.diseasespeciesName = diseasespeciesName;
    }
}
