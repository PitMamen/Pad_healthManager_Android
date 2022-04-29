package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data :
 */
public class CallResultBean implements Serializable {
   public String subid;
   public String telX;

  @Override
  public String toString() {
    return "CallResultBean{" +
            "subid='" + subid + '\'' +
            ", telX='" + telX + '\'' +
            '}';
  }
}
