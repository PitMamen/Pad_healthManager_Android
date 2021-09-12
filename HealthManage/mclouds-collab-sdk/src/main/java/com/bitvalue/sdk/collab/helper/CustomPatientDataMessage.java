package com.bitvalue.sdk.collab.helper;

import java.io.Serializable;

public class CustomPatientDataMessage extends CustomMessage implements Serializable {
//    public String msgText;
//    public String title;
//    public String content;
//    public String msgDetailId;


    public String userId;
    public String contentId;
    public String cbzd;
    public String dept;
    public String jzlx;
    public String jzsj;
    public String jzyy;
    public String userGoodsId;

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
