package com.bitvalue.healthmanage.widget.tasks;

import com.bitvalue.healthmanage.http.response.PlanDetailResult;
import com.bitvalue.healthmanage.widget.tasks.bean.SavePlanApi;

public interface DataInterface {
    SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO getData();
    PlanDetailResult.UserPlanDetailsDTO getAssembleData();
}
