package com.bitvalue.health.util.customview.task;


import com.bitvalue.health.api.requestbean.SavePlanApi;
import com.bitvalue.health.api.responsebean.PlanDetailResult;

public interface DataInterface {
    //提交数据时的获取数据
    SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO getData();
    //组装预览数据
    PlanDetailResult.UserPlanDetailsDTO getAssembleData();
}
