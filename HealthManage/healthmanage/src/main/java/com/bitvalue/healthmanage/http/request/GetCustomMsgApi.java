package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

public class GetCustomMsgApi implements IRequestApi {
    public String id;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/getUserRemind";
    }
}