package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 12/07
 */
public class QuickReplyBean implements Serializable {
    private String replyString;


    public QuickReplyBean(String replyString) {
        this.replyString = replyString;
    }

    public String getReplyString() {
        return replyString;
    }

    public void setReplyString(String replyString) {
        this.replyString = replyString;
    }
}
