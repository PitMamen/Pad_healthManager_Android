package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

public class DeleteMissionApi implements IRequestApi {
    public String templateId;
    public String taskId;
    public String id;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/delPlanTemplateTaskContent";
    }
}
