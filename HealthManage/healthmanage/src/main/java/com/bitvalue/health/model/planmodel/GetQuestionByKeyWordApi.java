package com.bitvalue.health.model.planmodel;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 * @author created by bitvalue
 * @data : 03/25
 */
public class GetQuestionByKeyWordApi implements IRequestApi {
    public int pageSize;
    public int start;
    public String keyWord;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/health/doctor/qryQuestByKeyWord";
    }
}
