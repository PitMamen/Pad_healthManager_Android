package com.bitvalue.health.util.chatUtil;

import com.bitvalue.sdk.collab.helper.CustomMessage;

import java.util.ArrayList;

/**
 * 问诊小结
 */
public class CustomAnalyseMessage extends CustomMessage {
    public String title;    //标题  = 问诊小结
    public String content;  //问诊小结内容
    public String contentId;  //患者 userID
    public String msgDetailId;  // null
    public String time; //发送问诊小结的 时间
    public String dealName; //执行人 = 医生姓名
}
