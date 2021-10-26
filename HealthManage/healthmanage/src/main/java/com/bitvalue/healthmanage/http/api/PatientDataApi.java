package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 * 查看患者上传资料
 */
public final class PatientDataApi implements IRequestApi {
    public String userId;//发送的患者的userId

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/health/doctor/qryUserLocalVisit";
    }

}