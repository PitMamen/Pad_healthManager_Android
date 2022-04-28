package com.bitvalue.health.api.requestbean;

import com.bitvalue.health.util.Constants;
import com.hjq.http.config.IRequestApi;

import java.io.Serializable;

public class GetLogsApi  implements IRequestApi, Serializable {
    public String dataOwnerId;
    public String dataUserId;
    public String hospitalCode;
    public String recordType;
    public String pastMonths = "48";

    //不做分页了
    @Override
    public String getApi() {
        return "/ehr/v1/list";
    }

    public static class LogBean {
        public String recordType;
        public String hospitalCode;
        public String docId;
        public String doctorName;
        public String hospitalName;
        public String serialNumber;  //记录流水号
        public String hospitalDepartment;
        public String departmentDiagnosis;
        public String happenedTime;

        public String getRecordType() {
            return recordType;
        }

        public void setRecordType(String recordType) {
            this.recordType = recordType;
        }

        public String getHospitalCode() {
            return hospitalCode;
        }

        public void setHospitalCode(String hospitalCode) {
            this.hospitalCode = hospitalCode;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getHospitalDepartment() {
            return hospitalDepartment;
        }

        public void setHospitalDepartment(String hospitalDepartment) {
            this.hospitalDepartment = hospitalDepartment;
        }

        public String getDepartmentDiagnosis() {
            return departmentDiagnosis;
        }

        public void setDepartmentDiagnosis(String departmentDiagnosis) {
            this.departmentDiagnosis = departmentDiagnosis;
        }

        public String getHappenedTime() {
            return happenedTime;
        }

        public void setHappenedTime(String happenedTime) {
            this.happenedTime = happenedTime;
        }


        @Override
        public String toString() {
            return "LogBean{" +
                    "recordType='" + recordType + '\'' +
                    ", hospitalCode='" + hospitalCode + '\'' +
                    ", docId='" + docId + '\'' +
                    ", doctorName='" + doctorName + '\'' +
                    ", hospitalName='" + hospitalName + '\'' +
                    ", serialNumber='" + serialNumber + '\'' +
                    ", hospitalDepartment='" + hospitalDepartment + '\'' +
                    ", departmentDiagnosis='" + departmentDiagnosis + '\'' +
                    ", happenedTime='" + happenedTime + '\'' +
                    '}';
        }
    }
}
