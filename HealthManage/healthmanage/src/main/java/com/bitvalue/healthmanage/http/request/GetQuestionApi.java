package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

public class GetQuestionApi implements IRequestApi {
    public int pageSize;
    public int start;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/qryQuestByKeyWord";
    }
}
