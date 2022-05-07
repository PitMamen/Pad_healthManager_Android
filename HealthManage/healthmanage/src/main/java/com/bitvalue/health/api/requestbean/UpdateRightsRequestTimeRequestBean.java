package com.bitvalue.health.api.requestbean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 05/07
 * 更新权益时间 请求实体
 */
public class UpdateRightsRequestTimeRequestBean implements Serializable {
  public String id;
  public String userId;
  public String tradeId;
  public String execTime;


  @Override
  public String toString() {
    return "UpdateRightsRequestTimeRequestBean{" +
            "id='" + id + '\'' +
            ", userId='" + userId + '\'' +
            ", tradeId='" + tradeId + '\'' +
            ", execTime='" + execTime + '\'' +
            '}';
  }
}
