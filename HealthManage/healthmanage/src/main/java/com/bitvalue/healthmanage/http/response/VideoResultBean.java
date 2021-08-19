package com.bitvalue.healthmanage.http.response;

import java.util.List;

public class VideoResultBean {

    public List<ListDTO> list;
    public int total;

    public static class ListDTO {
        public String author;
        public String brief;
        public String categoryId;
        public String categoryName;
        public int clickNum;
        public String createTime;
        public Boolean isVisible;
        public String previewUrl;
        public String publishTime;
        public String publisherName;
        public int publisherUserId;
        public String source;
        public String title;
        public String updateTime;
        public int vedioId;
        public String vedioUrl;

        //新增的type字段，用以区分Eventbus消息的类型，用于不同的页面关注
        public String videoFor;
    }
}
