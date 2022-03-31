package com.bitvalue.health.model.planmodel;

import static com.bitvalue.health.util.Constants.API_HEALTH;

import com.hjq.http.config.IRequestApi;

/**
 * @author created by bitvalue
 * @data : 03/30
 */
public class EndUuserPlanApi implements IRequestApi {
    public String id;

    @Override
    public String getApi() {
        return API_HEALTH + "/patient/endUserPlan";
    }
}
