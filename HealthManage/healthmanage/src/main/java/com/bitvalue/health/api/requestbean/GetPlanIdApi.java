package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

public class GetPlanIdApi implements IRequestApi {
    public String goodsId;
    public String userId;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/patient/queryPlanId";
    }
}
