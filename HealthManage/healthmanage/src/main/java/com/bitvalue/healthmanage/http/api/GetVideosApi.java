package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

/***
 * 根据关键字搜索视频
 */
public class GetVideosApi implements IRequestApi {
    public int pageSize;
    public int start;
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/health/doctor/qryVedioByKeyWord";
    }
}
