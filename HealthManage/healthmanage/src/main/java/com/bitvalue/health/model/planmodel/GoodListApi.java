package com.bitvalue.health.model.planmodel;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 * @author created by bitvalue
 * @data : 02/08
 */
public class GoodListApi implements IRequestApi {
    public int departmentId;
    public String goodsType;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/health/patient/queryGoodsList";
    }
}
