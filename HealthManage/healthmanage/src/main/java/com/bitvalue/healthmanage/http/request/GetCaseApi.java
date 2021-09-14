package com.bitvalue.healthmanage.http.request;

import com.bitvalue.healthmanage.Constants;
import com.hjq.http.config.IRequestApi;

/**
 */
public final class GetCaseApi implements IRequestApi {
    public String appointmentId;
    public String chargeDocId;
    public String chargeDocName;
    public String checkDocId;
    public String checkDocName;
    public String checkTime;
    public String chiefComplaint;
    public String createTime;
    public String diagnosis;
    public String diagnosisCode;
    public String docId;
    public String docName;
    public String generalInspection;
    public int id;
    public String pastIllness;
    public String presentIllness;
    public String remark;
    public int status;
    public String suggestion;
    public String updateTime;
    public String userId;

    @Override
    public String getApi() {
        return Constants.API_HEALTH + "/medical/doctor/qryUserMedicalCase";
    }

}