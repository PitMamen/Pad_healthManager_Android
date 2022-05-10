package com.bitvalue.health.api.responsebean;


import com.bitvalue.health.api.requestbean.filemodel.HisBothRecord;
import com.bitvalue.health.api.requestbean.filemodel.TbCisMain;

public class MRDetailResult {
//    public String privateCloudId;
//    public String privateCloudName;
    public String hospitalName;
    public MedicalRecord medicalRecord;
    public static class MedicalRecord{
        public String id;//ex 202126753_2_3
        public String index;//ex zhuyuan

        public HisBothRecord source;
        public TbCisMain medicalMain;

    }


    @Override
    public String toString() {
        return "MRDetailResult{" +
                "hospitalName='" + hospitalName + '\'' +
                ", medicalRecord=" + medicalRecord +
                '}';
    }
}
