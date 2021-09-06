package com.bitvalue.healthmanage.widget.tasks.bean;

import java.io.Serializable;

public class GetMissionObj implements Serializable {
    private int TaskNo;
    private int MissionNo;

    public GetMissionObj() {
    }

    public GetMissionObj(int taskNo, int missionNo) {
        TaskNo = taskNo;
        MissionNo = missionNo;
    }

    public int getTaskNo() {
        return TaskNo;
    }

    public void setTaskNo(int taskNo) {
        TaskNo = taskNo;
    }

    public int getMissionNo() {
        return MissionNo;
    }

    public void setMissionNo(int missionNo) {
        MissionNo = missionNo;
    }
}
