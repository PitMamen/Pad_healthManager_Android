package com.bitvalue.health.api.responsebean;



import java.io.Serializable;

/**
 * @author fengyongge
 * @Description
 */
public class ImageModel implements Serializable {
    private String url;
    private Boolean isNet;
    private String fileName;
    private String fileId;  //图片文件ID
    public ImageModel() {
        super();
    }

    public ImageModel(String localUrl) {
        this.localUrl = localUrl;
    }

    public String localUrl;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public ImageModel(String url, Boolean isNet) {
        super();
        this.url = url;
        this.isNet = isNet;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Boolean getIsNet() {
        return isNet;
    }
    public void setIsNet(Boolean isNet) {
        this.isNet = isNet;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
