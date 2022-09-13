package com.bitvalue.health.model.healthfilesmodel;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

public class MRDetailRequestApi  implements IRequestApi {

    public String dataOwnerId;
    public String dataUserId;
    public String serialNumber;
    public String recordType;
    public String hospitalCode;

    @Override
    public String getApi() {
//        return Constants.API_HEALTH + "/health/doctor/getPatientHospitalRecordDetail";
        return "/ehr-api/ehr/v1/getRecord";
    }
}
