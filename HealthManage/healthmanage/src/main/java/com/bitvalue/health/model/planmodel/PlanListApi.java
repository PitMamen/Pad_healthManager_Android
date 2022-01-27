package com.bitvalue.health.model.planmodel;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

public class PlanListApi implements IRequestApi {
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/qryMyPlanTemplates/?status=1";
    }
}
