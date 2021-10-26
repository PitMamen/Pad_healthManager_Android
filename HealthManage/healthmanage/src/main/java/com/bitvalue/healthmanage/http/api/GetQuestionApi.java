package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

public class GetQuestionApi implements IRequestApi {
    public String keyWord;
    public int pageSize;
    public int start;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/qryQuestByKeyWord";
    }
}
