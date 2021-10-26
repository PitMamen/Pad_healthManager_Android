package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

public class GetPlanIdApi implements IRequestApi {
    public String goodsId;
    public String userId;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/patient/queryPlanId";
    }
}
