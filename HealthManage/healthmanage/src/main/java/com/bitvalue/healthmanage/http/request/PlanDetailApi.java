package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

public class PlanDetailApi implements IRequestApi {
    public String templateId;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/qryPlanTemplateDetail";
    }
}
