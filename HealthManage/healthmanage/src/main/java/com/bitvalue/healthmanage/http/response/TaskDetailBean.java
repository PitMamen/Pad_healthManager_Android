package com.bitvalue.healthmanage.http.response;

import java.util.List;

/**
 * 
 */
public final class TaskDetailBean {

    public int id;
    public String userId;
    public String hospital;
    public String dept;
    public String visitTime;
    public String visitType;
    public String diagnosis;
    public String userGoodsId;
    public long contentId;
    public List<HealthImagesDTO> healthImages;

    public static class HealthImagesDTO {
        public int id;
        public String userId;
        public String imageType;
        public String sourceId;
        public String fileId;
        public String fileUrl;
        public String previewFileId;
        public String previewFileUrl;
        public String createTime;
    }
}