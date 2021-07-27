package com.bitvalue.healthmanage.http.request;

import com.hjq.http.config.IRequestApi;

public class TestApi implements IRequestApi {
    @Override
    public String getApi() {
        return "/test/hello";
    }
}
