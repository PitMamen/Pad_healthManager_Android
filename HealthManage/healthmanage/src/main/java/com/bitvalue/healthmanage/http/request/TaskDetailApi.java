package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

/**
 */
public final class TaskDetailApi implements IRequestApi {

    public String contentId;
    public String planType;//健康计划类型【Evaluate：健康体检及评估 Quest：随访跟踪 DrugGuide：用药指导 Knowledge：健康科普 Remind：健康提醒 OutsideInformation：院外资料】
    public String userId;//发送的患者的userId

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/health/patient/queryHealthPlanContent";
    }

}