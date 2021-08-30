package com.bitvalue.healthmanage.http.response;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

import java.io.Serializable;

public class TaskPlanDetailBean implements Serializable {
    public int id;
    public String userId;
    public String questName;
    public String questContent;
    public String createTime;
    public String questUrl;


    public String contentId;
    public String type;
    public String remindName;
    public String remindContent;
    public String voiceList;
    public String videoList;
    public String picList;
    public String articleList;
    public String remindUser;


    public String articleId;
    public String knowUrl;
    public String knowContent;
}
