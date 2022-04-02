package com.bitvalue.health.api.requestbean.filemodel;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 * @author created by bitvalue
 * @data : 04/01
 */
public class SystemRemindObj implements IRequestApi {

   public String userId;
   public String remindType;

    @Override
    public String getApi() {
        return Constants.API_HEALTH+"/sys/sysRemind";
    }
}
