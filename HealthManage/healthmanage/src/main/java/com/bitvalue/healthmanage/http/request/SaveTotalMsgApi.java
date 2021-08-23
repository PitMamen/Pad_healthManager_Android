package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.VideoResultBean;
import com.hjq.http.config.IRequestApi;

import java.io.File;
import java.util.List;

/**
 *    desc   : 上传图片
 */
public final class SaveTotalMsgApi implements IRequestApi {

    public int contentId;
    public String createTime;
    public String picList;
    public String videoList;
    public String voiceList;
    public String remindContent;
    public String remindName;//
    public String remindUser;//发送者id，也就是医生的userId
    public String userId;
    public String articleList;
    public int id;
    public int type;//类型（1：计划内提醒 2：计划外提醒 3：临时提醒）
    public List<ArticleBean> articleInfo;
    public List<VideoResultBean.ListDTO> vedioInfo;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/health/doctor/sendUserRemind";
    }


    public SaveTotalMsgApi setImage(File file) {
        return this;
    }
}