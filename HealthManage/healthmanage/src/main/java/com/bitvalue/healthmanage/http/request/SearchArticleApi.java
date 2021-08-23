package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

public class SearchArticleApi implements IRequestApi {
    public String title;
    public int pageSize = 10;
    public int start = 1;
    @Override
    public String getApi() {
//        return Constants.API_HEALTH +  "/health/patient/mostUsefulArticleWithNum/{articleNum}";
        return Constants.API_HEALTH +  "/health/patient/articleByTitle";
    }
}