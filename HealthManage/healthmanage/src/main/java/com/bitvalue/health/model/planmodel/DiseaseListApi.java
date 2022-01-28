package com.bitvalue.health.model.planmodel;

import static com.bitvalue.health.util.Constants.API_ACCOUNT;

import com.hjq.http.config.IRequestApi;

/**
 * @author created by bitvalue
 * @data : 01/27
 */
public class DiseaseListApi implements IRequestApi {
    public int departmentId;
    @Override
    public String getApi() {
        return API_ACCOUNT+"/businessManagement/getDiseaseList/";
    }
}
