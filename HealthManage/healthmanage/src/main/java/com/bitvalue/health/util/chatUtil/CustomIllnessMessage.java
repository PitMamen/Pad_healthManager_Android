package com.bitvalue.health.util.chatUtil;

import com.bitvalue.sdk.collab.helper.CustomMessage;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 05/05
 * 病情概述
 */
public class CustomIllnessMessage extends CustomMessage {
    public String content;
    public String imageList;
    public String time;


    @Override
    public String toString() {
        return "CustomIllnessMessage{" +
                "content='" + content + '\'' +
                ", imageList=" + imageList +
                ", time='" + time + '\'' +
                '}';
    }
}
