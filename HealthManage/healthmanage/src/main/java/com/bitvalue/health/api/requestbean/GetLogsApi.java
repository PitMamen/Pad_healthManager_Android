package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

public class GetLogsApi implements IRequestApi {
    public String timeHorizon = "36";
    public String recordType;//门诊 menzhen   住院 zhuyuan  全部 all
    public String userId;
    public String idNumber;

    //不做分页了
    @Override
    public String getApi() {
        return Constants.API_HEALTH +  "/patient/qryPatientMedicalRecords";
    }

    public static class LogBean{
        private String lsh;
        private String zzysxm;
        private String docId;
        private String yljgdm;
        private String sj;
        private String zdmc;
        private String hospitalName;
        private String type;
        private String ksmc;

        public String getLsh() {
            return lsh;
        }

        public void setLsh(String lsh) {
            this.lsh = lsh;
        }

        public String getZzysxm() {
            return zzysxm;
        }

        public void setZzysxm(String zzysxm) {
            this.zzysxm = zzysxm;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public String getYljgdm() {
            return yljgdm;
        }

        public void setYljgdm(String yljgdm) {
            this.yljgdm = yljgdm;
        }

        public String getSj() {
            return sj;
        }

        public void setSj(String sj) {
            this.sj = sj;
        }

        public String getZdmc() {
            return zdmc;
        }

        public void setZdmc(String zdmc) {
            this.zdmc = zdmc;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getKsmc() {
            return ksmc;
        }

        public void setKsmc(String ksmc) {
            this.ksmc = ksmc;
        }


//        public String recordType;
//        public String hospitalCode;
//        public String docId;
//        public String doctorName;
//        public String hospitalName;
//        public String serialNumber;
//        public String hospitalDepartment;
//        public String departmentDiagnosis;
//        public String happenedTime;
    }
}
