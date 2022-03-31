package com.bitvalue.health.model.planmodel;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 * @author created by bitvalue
 * @data : 03/26
 */
public class GetAnserListApi implements IRequestApi {
    public int current;
    public int size;
    public int userId;


    @Override
    public String getApi() {
        return Constants.API_TDUCK + "/user/project/result/page";
    }
}
