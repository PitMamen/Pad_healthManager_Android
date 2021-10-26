package com.bitvalue.healthmanage.widget.tasks;

import com.bitvalue.healthmanage.http.bean.PlanDetailResult;
import com.bitvalue.healthmanage.widget.tasks.bean.SavePlanApi;

public interface DataInterface {
    //提交数据时的获取数据
    SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO getData();
    //组装预览数据
    PlanDetailResult.UserPlanDetailsDTO getAssembleData();
}
