package com.bitvalue.health.util.chatUtil;

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.sdk.collab.helper.CustomMessage;

import java.io.Serializable;
import java.util.List;

public class CustomWenJuanMessage extends CustomMessage implements Serializable {
    public String url;
    public String name;
    public String id;
    public List<QuestionResultBean.ListDTO> questionlistDTOS;


    @Override
    public String toString() {
        return "CustomWenJuanMessage{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", questionlistDTOS=" + questionlistDTOS +
                '}';
    }
}
