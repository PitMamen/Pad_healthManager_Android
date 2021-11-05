package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

public class GetLogsApi implements IRequestApi {
    public String hospitalCode;//配置的，测试和生产环境不一样
    public String month = "36";
    public String recordType;//门诊 menzhen   住院 zhuyuan  全部 all
    public String userId;

    //不做分页了
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/getPatientHospitalRecordList";
    }

    public static class LogBean{
        public String recordType;
        public String hospitalCode;
        public String docId;
        public String doctorName;
        public String hospitalName;
        public String serialNumber;
        public String hospitalDepartment;
        public String departmentDiagnosis;
        public String happenedTime;
    }
}
