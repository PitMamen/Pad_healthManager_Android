package com.bitvalue.health.api.responsebean;

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

    public int MissionNo;
    public int TaskNo;

    public ArticleBean() {
    }

    public ArticleBean(String title, int articleIdd) {
        this.articleId = articleId;
        this.title = title;
    }

    @Override
    public String toString() {
        return "ArticleBean{" +
                "articleId=" + articleId +
                ", articleType='" + articleType + '\'' +
                ", author='" + author + '\'' +
                ", brief='" + brief + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", clickNum=" + clickNum +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", extraData='" + extraData + '\'' +
                ", isVisible=" + isVisible +
                ", previewUrl='" + previewUrl + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", publisherName='" + publisherName + '\'' +
                ", publisherUserId=" + publisherUserId +
                ", source='" + source + '\'' +
                ", templateId='" + templateId + '\'' +
                ", title='" + title + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", MissionNo=" + MissionNo +
                ", TaskNo=" + TaskNo +
                '}';
    }
}
