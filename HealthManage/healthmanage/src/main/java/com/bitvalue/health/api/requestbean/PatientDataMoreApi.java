package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

public final class PatientDataMoreApi implements IRequestApi {

    public String userId;

    @Override
    public String getApi() {
        return Constants.API_ACCOUNT + "/userInfo/getBaseInfoForDoctor";
    }
    
    public static class PatientDataMoreResponse{

        public String birthday;
        public String userSex;
        public String userAge;
        public String phone;
        public String address;
        public String rh;
        public String identificationNo;
        public int weight;
        public String userName;
        public String bloodType;
        public int height;
    }

}