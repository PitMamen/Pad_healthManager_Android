package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

import java.io.File;

/**
 */
public final class SaveAnalyseApi implements IRequestApi {

    public int contentId;
    public String evalContent;
    public String evalTime;
    public String evalUser;//医生的userId
    public String id;
    public String userId;//发送的患者的userId
    public int type;//类型（1：计划内提醒 2：计划外提醒 3：临时提醒）

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/health/doctor/sendUserEevaluate";
    }

}