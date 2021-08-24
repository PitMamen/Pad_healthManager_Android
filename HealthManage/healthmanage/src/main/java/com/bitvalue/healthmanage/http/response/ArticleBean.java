package com.bitvalue.healthmanage.http.response;

import java.io.Serializable;

public class ArticleBean implements Serializable {

    public int articleId;
    public String articleType;
    public String author;
    public String brief;
    public String categoryId;
    public String categoryName;
    public int clickNum;
    public String content;
    public String createTime;
    public String extraData;
    public boolean isVisible;
    public String previewUrl;
    public String publishTime;
    public String publisherName;
    public int publisherUserId;
    public String source;
    public String templateId;
    public String title;
    public String updateTime;


    public ArticleBean() {
    }

    public ArticleBean(String title, int articleIdd) {
        this.articleId = articleId;
        this.title = title;
    }
}
