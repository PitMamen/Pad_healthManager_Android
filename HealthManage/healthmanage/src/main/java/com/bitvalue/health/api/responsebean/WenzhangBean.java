package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data :
 */
public class WenzhangBean implements Serializable {
    public String id;
    public String url;
    public String title;


    public WenzhangBean(String id, String url, String title) {
        this.id = id;
        this.url = url;
        this.title = title;
    }
}
