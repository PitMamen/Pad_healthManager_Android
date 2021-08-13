package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

import java.io.File;
import java.util.List;

/**
 *    desc   : 上传图片
 */
public final class SaveTotalMsgApi implements IRequestApi {

    public int contentId;
    public String createTime;
//    public List<String> picList;
    public String picList;
//    public List<String> videoList;
    public String videoList;
//    public List<String> voiceList;
    public String voiceList;
    public String remindContent;
    public String remindName;
//    public List<String> userId;
    public String userId;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/health/doctor/saveUserRemind";
    }


    public SaveTotalMsgApi setImage(File file) {
        return this;
    }
}