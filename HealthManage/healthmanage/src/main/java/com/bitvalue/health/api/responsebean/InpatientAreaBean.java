package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 01/10
 * 病区实体
 */
public class InpatientAreaBean implements Serializable {
  private String name;

    public InpatientAreaBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
