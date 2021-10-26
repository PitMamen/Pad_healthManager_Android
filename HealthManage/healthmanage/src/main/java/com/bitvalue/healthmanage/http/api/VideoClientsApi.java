package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 * 云门诊就诊列表查询
 */
public class VideoClientsApi implements IRequestApi {
    public String attendanceStatus;  //就诊状态（1：待就诊 2：视频就诊 3：处方病例 4：就诊完成））;查询所有未完成状态不传
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/medical/doctor/qryMyMedicalPatients";
    }
}
