package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 *
 */
public final class ReportStatusApi implements IRequestApi {
    public String attendanceStatus;//就诊状态（1：待就诊 2：就诊中 3：已完成）
    public String id;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/medical/doctor/updateAttendanceStatus";
    }
}