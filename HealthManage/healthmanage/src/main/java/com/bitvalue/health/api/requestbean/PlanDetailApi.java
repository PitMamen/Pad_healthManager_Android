package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

public class PlanDetailApi implements IRequestApi {
    public String templateId;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/qryPlanTemplateDetail";
    }
}
