package com.bitvalue.health.model.healthfilesmodel;



/**
 * 病历详情请求体封装类
 */
public class MRDetailReqestBody {
    private String token;
    private String indices;
    private String documentId;
    private String certificateUserName;
    private String deviceType;
    private String applyUserId;
    private String providerId;

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public MRDetailReqestBody(){
//        this.token = MyApplication.getInstance().getToken();
//        this.certificateUserName =MyApplication.getInstance().getUserName()+ SharedPreferenceUtil.getStringSP("SP_CERT_PASSWD","");
        this.deviceType = "PAD";
        this.applyUserId = "1";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getIndices() {
        return indices;
    }

    public void setIndices(String indices) {
        this.indices = indices;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCertificateUserName() {
        return certificateUserName;
    }

    public void setCertificateUserName(String certificateUserName) {
        this.certificateUserName = certificateUserName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public String toString() {
        return "MRDetailReqestBody{" +
                "token='" + token + '\'' +
                ", indices='" + indices + '\'' +
                ", documentId='" + documentId + '\'' +
                ", certificateUserName='" + certificateUserName + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", applyUserId='" + applyUserId + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }
}
