package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 11/11
 */
public class VisitBean implements Serializable {
    private String hospitolName;
    private String startTime;
    private String endTime;
    private String visitStatus;

    public VisitBean(String hospitolName, String startTime, String endTime, String visitStatus) {
        this.hospitolName = hospitolName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.visitStatus = visitStatus;
    }

    public String getHospitolName() {
        return hospitolName;
    }

    public void setHospitolName(String hospitolName) {
        this.hospitolName = hospitolName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        this.visitStatus = visitStatus;
    }
}
