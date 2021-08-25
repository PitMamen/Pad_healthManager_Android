package com.bitvalue.healthmanage.http.response;

import java.io.Serializable;

public class PlanListBean implements Serializable {
    public String templateId;
    public String templateName;
    public String basetimeType;
    public String userId;
    public String status = "0";
    public long createTime;
    public long updateTime;
}
