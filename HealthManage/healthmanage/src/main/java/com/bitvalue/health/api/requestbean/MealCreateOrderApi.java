package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 * @author created by bitvalue
 * @data : 01/05
 */
public class MealCreateOrderApi implements IRequestApi {

    public String beginTime;
    public String doctorId;
    public String patientId;
    public String templateId;

    @Override
    public String getApi() {
        return Constants.API_HEALTH +"/health/doctor/distributePlan";
    }
}
