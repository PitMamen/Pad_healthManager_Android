package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

public class GetHistoryApi implements IRequestApi {
    public String userId;
    public String appointmentId;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/medical/doctor/qryUserMedicalCase";
    }
}
