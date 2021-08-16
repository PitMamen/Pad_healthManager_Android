package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

public class GetArticleApi implements IRequestApi {
    public int articleNum;
    @Override
    public String getApi() {
//        return Constants.API_HEALTH +  "/health/patient/mostUsefulArticleWithNum/{articleNum}";
        return Constants.API_HEALTH +  "/health/patient/mostUsefulArticleWithNum";
    }
}
