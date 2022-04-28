package com.bitvalue.health.api;

/**
 * @author created by bitvalue
 * @data : 04/28
 */
public class MRRecordResult {
  public int code;
  public String message;
  public String encryptedRecord;
  public String wrappedDEK;


  @Override
  public String toString() {
    return "MRRecordResult{" +
            "code=" + code +
            ", message='" + message + '\'' +
            ", encryptedRecord='" + encryptedRecord + '\'' +
            ", wrappedDEK='" + wrappedDEK + '\'' +
            '}';
  }
}
