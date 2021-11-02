package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

public class GetVideosApi implements IRequestApi {
    public int pageSize;
    public int start;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/qryVedioByKeyWord";
    }
}
