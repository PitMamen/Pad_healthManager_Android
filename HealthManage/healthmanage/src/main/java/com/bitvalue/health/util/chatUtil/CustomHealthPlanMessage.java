package com.bitvalue.health.util.chatUtil;

import com.bitvalue.sdk.collab.helper.CustomMessage;

public class CustomHealthPlanMessage extends CustomMessage {
    public String time;
    public String docuserId;
    public String title;


    @Override
    public String toString() {
        return "CustomPhoneConsultationMessage{" +
                "time='" + time + '\'' +
                ", docuserId='" + docuserId + '\'' +
                '}';
    }

}
