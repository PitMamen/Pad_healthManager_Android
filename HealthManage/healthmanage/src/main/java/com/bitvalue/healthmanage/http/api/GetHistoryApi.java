package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

/***
 * 查询病例接口
 */
public class GetHistoryApi implements IRequestApi {
    public String userId;
    public String appointmentId;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/medical/doctor/qryUserMedicalCase";
    }
}
