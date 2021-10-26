package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

/***
 * 根据输入的标题查询文章
 */
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
