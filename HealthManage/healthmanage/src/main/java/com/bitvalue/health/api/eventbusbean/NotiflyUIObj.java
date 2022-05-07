package com.bitvalue.health.api.eventbusbean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 05/07
 */
public class NotiflyUIObj implements Serializable {
    public String tradid;


    public NotiflyUIObj(String tradid) {
        this.tradid = tradid;
    }
}
