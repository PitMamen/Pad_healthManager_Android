package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
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
