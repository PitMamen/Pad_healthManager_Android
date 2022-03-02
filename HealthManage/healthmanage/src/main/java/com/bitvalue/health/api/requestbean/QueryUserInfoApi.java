package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 * @author created by bitvalue
 * @data : 02/17
 */
public class QueryUserInfoApi implements IRequestApi {
    public String userId;
    public String token;


    @Override
    public String getApi() {
        return Constants.API_ACCOUNT +  "/getUserInfo";
    }
}
