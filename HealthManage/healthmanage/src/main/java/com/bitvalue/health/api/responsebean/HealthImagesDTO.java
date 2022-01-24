package com.bitvalue.health.api.responsebean;

public class HealthImagesDTO {
    private Integer id;
    private String userId;
    private String imageType;
    private String sourceId;
    private String fileId;
    private String fileUrl;
    private String previewFileId;
    private String previewFileUrl;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getPreviewFileId() {
        return previewFileId;
    }

    public void setPreviewFileId(String previewFileId) {
        this.previewFileId = previewFileId;
    }

    public String getPreviewFileUrl() {
        return previewFileUrl;
    }

    public void setPreviewFileUrl(String previewFileUrl) {
        this.previewFileUrl = previewFileUrl;
    }

}
