package com.bitvalue.health.api.responsebean;

import java.io.Serializable;

/**
 * @author created by bitvalue
 * @data : 05/27
 */
public class CheckNewVersionBean implements Serializable {
  private int id;
  private String versionCode;
  private String versionNumber;
  private String versionDescription;
  private int state;
  private String downloadUrl;
  private Object fileHash;
  private Object fileSize;
  private String fileName;
  private long createdTime;
  private long updatedTime;
  private int createrId;
  private String createrName;
  private int platform;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(String versionCode) {
    this.versionCode = versionCode;
  }

  public String getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(String versionNumber) {
    this.versionNumber = versionNumber;
  }

  public String getVersionDescription() {
    return versionDescription;
  }

  public void setVersionDescription(String versionDescription) {
    this.versionDescription = versionDescription;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public Object getFileHash() {
    return fileHash;
  }

  public void setFileHash(Object fileHash) {
    this.fileHash = fileHash;
  }

  public Object getFileSize() {
    return fileSize;
  }

  public void setFileSize(Object fileSize) {
    this.fileSize = fileSize;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public long getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(long createdTime) {
    this.createdTime = createdTime;
  }

  public long getUpdatedTime() {
    return updatedTime;
  }

  public void setUpdatedTime(long updatedTime) {
    this.updatedTime = updatedTime;
  }

  public int getCreaterId() {
    return createrId;
  }

  public void setCreaterId(int createrId) {
    this.createrId = createrId;
  }

  public String getCreaterName() {
    return createrName;
  }

  public void setCreaterName(String createrName) {
    this.createrName = createrName;
  }

  public int getPlatform() {
    return platform;
  }

  public void setPlatform(int platform) {
    this.platform = platform;
  }

  @Override
  public String toString() {
    return "CheckNewVersionBean{" +
            "id=" + id +
            ", versionCode='" + versionCode + '\'' +
            ", versionNumber='" + versionNumber + '\'' +
            ", versionDescription='" + versionDescription + '\'' +
            ", state=" + state +
            ", downloadUrl='" + downloadUrl + '\'' +
            ", fileHash=" + fileHash +
            ", fileSize=" + fileSize +
            ", fileName='" + fileName + '\'' +
            ", createdTime=" + createdTime +
            ", updatedTime=" + updatedTime +
            ", createrId=" + createrId +
            ", createrName='" + createrName + '\'' +
            ", platform=" + platform +
            '}';
  }
}
