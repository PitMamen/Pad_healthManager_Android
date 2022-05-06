package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 05/06
 */
public class MessageInfoData implements Serializable {
  public String content;
  public String imageList;
  public String tradeId;
  public String time;


  @Override
  public String toString() {
    return "MessageInfo{" +
            "content='" + content + '\'' +
            ", imageList='" + imageList + '\'' +
            '}';
  }
}
