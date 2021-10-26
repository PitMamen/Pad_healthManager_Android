package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

public class GetArticleApi implements IRequestApi {
    public int articleNum;
    @Override
    public String getApi() {
//        return Constants.API_HEALTH +  "/health/patient/mostUsefulArticleWithNum/{articleNum}";
        return Constants.API_HEALTH +  "/health/patient/mostUsefulArticleWithNum";
    }
}
