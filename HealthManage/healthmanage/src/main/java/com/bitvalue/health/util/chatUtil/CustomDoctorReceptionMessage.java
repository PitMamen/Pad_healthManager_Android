package com.bitvalue.health.util.chatUtil;

import com.bitvalue.sdk.collab.helper.CustomMessage;

/**
 * @author created by bitvalue
 * @data : 05/07
 */
public class CustomDoctorReceptionMessage extends CustomMessage {
    public String content;
    public String docName;
    public String docId;
    public String tradeId;
    public String docdeptName;
    public String time;


    @Override
    public String toString() {
        return "CustomDoctorReceptionMessage{" +
                "content='" + content + '\'' +
                ", docName='" + docName + '\'' +
                ", tradeId='" + tradeId + '\'' +
                ", docdeptId='" + docdeptName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
