package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;

import retrofit2.http.PUT;

/**
 * @author created by bitvalue
 * @data : 04/24
 */
public class PlanCheckEamResponseBean implements Serializable {
    public int id;

    //examType
    public String examType;
    public String examName;
    public String examDescribe;

    // checkType
    public String checkType;
    public String checkName;
    public String checkDescribe;

    public String contentId;
    public String goodsId;
    public String userId;
    public long createdTime;
    public List<TaskDetailBean.HealthImagesDTO> healthImages;


}
