package com.bitvalue.sdk.collab.helper;

public abstract class CustomMessage {
    public String description;
//    public String extension;
    public String type;


    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }
}
