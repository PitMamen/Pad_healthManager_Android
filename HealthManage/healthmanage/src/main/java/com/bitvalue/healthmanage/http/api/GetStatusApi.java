package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 *
 */
public final class GetStatusApi implements IRequestApi {
    public String attendanceStatus;//就诊状态（1：待就诊 2：就诊中 3：已完成）
    public String id;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/health/patient/getPatientAppointmentById";
    }
}