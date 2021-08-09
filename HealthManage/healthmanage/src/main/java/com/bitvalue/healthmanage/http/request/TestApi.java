package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

public class TestApi implements IRequestApi {
    @Override
    public String getApi() {
        return Constants.API_ACCOUNT + "/test/hello";
    }
}
