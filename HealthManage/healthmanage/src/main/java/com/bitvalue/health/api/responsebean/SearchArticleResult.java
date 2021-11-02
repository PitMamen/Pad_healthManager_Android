package com.bitvalue.health.api.responsebean;

import java.util.List;

public class SearchArticleResult {

    public List<ArticleBean> list;
    public int total;

    public static class ListDTO {
        public int articleId;
        public String categoryId;
        public String categoryName;
        public String title;
        public String brief;
        public String articleType;
        public String templateId;
        public String author;
        public String source;
        public String publisherUserId;
        public String content;
        public String publisherName;
        public boolean isVisible;
        public String publishTime;
        public String createTime;
        public String updateTime;
        public String extraData;
        public String previewUrl;
        public String clickNum;
    }
}
