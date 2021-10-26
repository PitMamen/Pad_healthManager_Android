package com.bitvalue.healthmanage.http.api;

import com.bitvalue.healthmanage.util.Constants;
import com.hjq.http.config.IRequestApi;

public final class PersonalDataApi implements IRequestApi {

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
    public String getApi() {
        return Constants.API_ACCOUNT + "/individualInfo/getDoctorInfo";
    }
    
    

}