package com.bitvalue.healthmanage.http.bean;

public class PlanBean {
    public String name;
    public String status;

    public PlanBean() {
    }

    public PlanBean(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public PlanBean(String name) {
        this.name = name;
    }
}
