package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

public class GetPlanDetailApi implements IRequestApi {
    public String planId;
    @Override
    public String getApi() {
//        return Constants.API_HEALTH +  "/health/patient/mostUsefulArticleWithNum/{articleNum}";
        return Constants.API_HEALTH +  "/health/patient/queryHealthPlan";
    }
}
