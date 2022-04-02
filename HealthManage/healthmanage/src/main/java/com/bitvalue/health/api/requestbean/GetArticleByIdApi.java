package com.bitvalue.health.api.requestbean;

import static com.bitvalue.health.util.Constants.API_HEALTH;

import com.hjq.http.config.IRequestApi;

/**
 * @author created by bitvalue
 * @data : 04/01
 * 根据文章id 查询文章
 */
public class GetArticleByIdApi implements IRequestApi {
    public int id;
    @Override
    public String getApi() {
        return API_HEALTH+"/health/patient/articleById";
    }
}
