package com.bitvalue.healthmanage.http.request;

import com.hjq.http.config.IRequestApi;

public class ClientsApi implements IRequestApi {
    @Override
    public String getApi() {
        return "/health/doctor/qryMyPatients";
    }
}
