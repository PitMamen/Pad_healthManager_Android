package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

/**
 * 个人设置 我的看诊记录 接口
 */
public final class ChatLogApi implements IRequestApi {
    public String type = "";//就诊类型（1：健康管理 2：云门诊 3：联合门诊）;查询所有记录不传值

    public String keyWord;
    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/medical/doctor/qryMyMedicalRecords";
    }

}