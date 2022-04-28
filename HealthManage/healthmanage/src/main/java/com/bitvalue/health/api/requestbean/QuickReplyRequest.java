package com.bitvalue.health.api.requestbean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 04/28
 */
public class QuickReplyRequest implements Serializable {
  public String content;
  public String icdCodes;
  public String id;
  public String useNum;
  public String userId;
  public int wordsType;


  @Override
  public String toString() {
    return "QuickReplyRequest{" +
            "content='" + content + '\'' +
            ", icdCodes='" + icdCodes + '\'' +
            ", id='" + id + '\'' +
            ", useNum='" + useNum + '\'' +
            ", userId='" + userId + '\'' +
            ", wordsType=" + wordsType +
            '}';
  }
}
