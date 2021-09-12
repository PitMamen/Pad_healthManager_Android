package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

/**
 */
public final class PatientDataApi implements IRequestApi {
    public String userId;//发送的患者的userId

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/health/doctor/qryUserLocalVisit";
    }

}