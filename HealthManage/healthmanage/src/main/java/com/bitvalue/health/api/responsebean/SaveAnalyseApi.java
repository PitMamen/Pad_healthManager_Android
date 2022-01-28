package com.bitvalue.health.api.responsebean;

import com.hjq.http.config.IRequestApi;

/**
 */
public final class SaveAnalyseApi   {
    public String contentId;
    public String evalContent;
    public String evalTime;
    public String planId;
    public String evalUser;//医生的userId
    public String describe;
    public String id;
    public String taskId;
    public String userId;//发送的患者的userId
    public int type;//类型（1：计划内提醒 2：计划外提醒 3：临时提醒）

}