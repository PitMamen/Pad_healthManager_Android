package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 * 获取健康评估详情
 */
public class GetCustomAnalyseApi implements IRequestApi {
    public String id;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/getUserEevaluate";
    }
}
