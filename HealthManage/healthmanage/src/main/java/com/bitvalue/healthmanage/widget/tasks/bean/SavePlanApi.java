package com.bitvalue.healthmanage.widget.tasks.bean;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

import java.util.List;

public class SavePlanApi implements IRequestApi {
    public List<DiseaseDTO> disease;
    public GoodsInfoDTO goodsInfo;
    public List<TemplateTaskDTO> templateTask;
    public String templateName;
    public String basetimeType;

    //成功后返回的字段
    public String createTime;
    public String templateId;
    public String updateTime;
    public String userId;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/health/doctor/savePlanTemplate";
    }


    public static class GoodsInfoDTO {
        public String goodsName;
        public String goodsDescribe;
        public String goodsSpec;
        public String imgList;
        public String status;
    }

    public static class DiseaseDTO {
        public String diseaseCode;
        public String diseaseName;
    }

    public static class TemplateTaskDTO {
        public String execTime;
        public String taskName;
        public List<TemplateTaskContentDTO> templateTaskContent;

        public static class TemplateTaskContentDTO {
            public String taskType;
            public ContentDetailDTO contentDetail;

            public static class ContentDetailDTO {
                //健康问卷属性
                public String questName;
                public String questId;

                //科普文章属性
                public String knowUrl;
                public String knowContent;

                //健康提醒属性
                public String remindName;
                public String remindContent;
                public String voiceList;
            }
        }
    }
}
