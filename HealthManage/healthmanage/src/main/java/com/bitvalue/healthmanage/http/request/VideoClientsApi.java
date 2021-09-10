package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

public class VideoClientsApi implements IRequestApi {
    public String attendanceStatus;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/medical/doctor/qryMyMedicalPatients";
    }
}
