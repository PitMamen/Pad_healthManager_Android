package com.bitvalue.healthmanage.widget.tasks.bean;

import androidx.annotation.NonNull;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

import org.jetbrains.annotations.NotNull;

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
        public String goodsSpec = "";
        public String imgList = "";
        public String status;
    }

    public static class DiseaseDTO {
        public String diseaseCode;
        public String diseaseName;
    }

    public static class TemplateTaskDTO {
        public String execTime;
        public String taskName;
        public String templateId;
        public String taskId;
        public List<TemplateTaskContentDTO> templateTaskContent;

        public static class TemplateTaskContentDTO {
            public String taskType;
            public String taskId;
            public String contentId;
            public String id;
            public ContentDetailDTO contentDetail;

            public static class ContentDetailDTO {
                public String id = "";
                //健康问卷属性
                public String questName = "";
                public String questId = "";

                //科普文章属性
                public String knowUrl = "";
                public String knowContent = "";
                public String articleId = "";
                public String title = "";

                //健康提醒属性
                public String remindName = "";
                public String remindContent = "";
                public String voiceList = "";
                public String picList = "";
                public String videoList = "";
            }
        }
    }
}
