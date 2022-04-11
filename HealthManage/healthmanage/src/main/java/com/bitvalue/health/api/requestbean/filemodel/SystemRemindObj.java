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
    public int eventType;   //4  个案管理师完成处理    5  医生开始问诊    6医生结束问诊
    public String infoDetail;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/sys/sysRemind";
    }
}
