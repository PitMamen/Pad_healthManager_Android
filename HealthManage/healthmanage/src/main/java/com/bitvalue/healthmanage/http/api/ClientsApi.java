package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 * 患者健康管理 获取患者接口
 */
public class ClientsApi implements IRequestApi {
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/qryMyPatients";
    }
}
