package com.bitvalue.health.util.chatUtil;

import com.bitvalue.health.api.responsebean.WenzhangBean;
import com.bitvalue.sdk.collab.helper.CustomMessage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CustomCaseHistoryMessage extends CustomMessage implements Serializable {
    public String title;
    public String content;
    public String url;
    public String id;
    public Map<String,String> maparticleList;
    public List<WenzhangBean> beanList;

    /**
     *
     * cbzd: "肥胖症"
     * dept: "内科"
     * jzlx: "门诊"
     * jzsj: "2021-08-21"
     * jzyy: "湘雅2"
     * type: "CustomUploadMessage"
     * userGoodsId: "175"
     * userId: "33"
     *
     */
}
