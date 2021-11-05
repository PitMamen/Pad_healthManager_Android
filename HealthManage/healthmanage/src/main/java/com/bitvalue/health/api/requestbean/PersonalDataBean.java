package com.bitvalue.health.api.requestbean;


public final class PersonalDataBean {

    public int accountId;
    public int userId;
    public String phone;
    public String userName;
    public String userSex;
    public String userType;
    public String identificationType;
    public String identificationNo;
    public String professionalTitle;
    public int departmentId;
    public String departmentCode;
    public String departmentName;
    public String hospitalCode;
    public String hospitalName;
    public String doctorBrief;
    public String avatarUrl;
    public int userAge;
    public String userSig;
    public String expertInDisease;
    public String expertInDiseaseWord;

    @Override
    public String toString() {
        return "PersonalDataBean{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", phone='" + phone + '\'' +
                ", userName='" + userName + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userType='" + userType + '\'' +
                ", identificationType='" + identificationType + '\'' +
                ", identificationNo='" + identificationNo + '\'' +
                ", professionalTitle='" + professionalTitle + '\'' +
                ", departmentId=" + departmentId +
                ", departmentCode='" + departmentCode + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", hospitalCode='" + hospitalCode + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", doctorBrief='" + doctorBrief + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", userAge=" + userAge +
                ", userSig='" + userSig + '\'' +
                ", expertInDisease='" + expertInDisease + '\'' +
                ", expertInDiseaseWord='" + expertInDiseaseWord + '\'' +
                '}';
    }
}