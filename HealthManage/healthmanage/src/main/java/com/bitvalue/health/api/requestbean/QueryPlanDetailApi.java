package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 04/24
 */
public class QueryPlanDetailApi implements IRequestApi, Serializable {
    public String userId;
    public String contentId;
    public String planType;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/health/patient/queryHealthPlanContent";
    }
}
