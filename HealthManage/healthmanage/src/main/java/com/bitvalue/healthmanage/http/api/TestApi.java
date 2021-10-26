package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

public class TestApi implements IRequestApi {
    @Override
    public String getApi() {
        return Constants.API_ACCOUNT + "/test/hello";
    }
}
