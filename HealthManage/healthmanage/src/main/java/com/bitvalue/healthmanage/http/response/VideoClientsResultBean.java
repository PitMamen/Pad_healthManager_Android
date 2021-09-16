package com.bitvalue.healthmanage.http.response;

public class VideoClientsResultBean{

    public String appointmentPeriodTime;
    public long appointmentTime;
    public int appointmentType;
    public int attendanceStatus;//就诊状态（1：待就诊 2：就诊中 3：待书写病历 4:已完成）
    public int bookingStatus;
    public String createTime;
    public String departmentCode;
    public String doctorId;
    public String id;
    public int patientId;
    public String seeTime;
    public long updateTime;
    public UserInfoDTO userInfo;
    public String yljgdm;
    public boolean isClicked = false;
    public boolean hasNew = false;
    public String headUrl;

    public static class UserInfoDTO {

        public String userSex;
        public String identificationType;
        public String userName;
        public int userId;
        public String cardNo;
        public int userAge;
        public int accountId;
        public boolean isDefault;
        public String phone;
        public String identificationNo;
        public String userType;
        public String relationship;
        public String userSig;
    }
}
