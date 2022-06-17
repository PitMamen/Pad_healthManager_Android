package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/21
 * 查询我的任务 响应实体
 */
public class TaskDeatailBean implements Serializable {


    private int id;
    private String userId;
    private int taskType;
    private String taskName;
    private String taskDescribe;
    private TaskDetailDTO taskDetail;
    private String contentId;
    private long execTime;
    private String finishRate;
    private String updateTime;
    private int execFlag;  // 1 = 已执行  其他 未执行     0 申请 待确认   2  已分配
    private int noticeFlag;
    public boolean isShowBottomBuntton = true; //自定义字段 用来区分底部是否显示按钮 标识

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescribe() {
        return taskDescribe;
    }

    public void setTaskDescribe(String taskDescribe) {
        this.taskDescribe = taskDescribe;
    }

    public TaskDetailDTO getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(TaskDetailDTO taskDetail) {
        this.taskDetail = taskDetail;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public long getExecTime() {
        return execTime;
    }

    public void setExecTime(long execTime) {
        this.execTime = execTime;
    }

    public String getFinishRate() {
        return finishRate;
    }

    public void setFinishRate(String finishRate) {
        this.finishRate = finishRate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getExecFlag() {
        return execFlag;
    }

    public void setExecFlag(int execFlag) {
        this.execFlag = execFlag;
    }

    public int getNoticeFlag() {
        return noticeFlag;
    }

    public void setNoticeFlag(int noticeFlag) {
        this.noticeFlag = noticeFlag;
    }

    public static class TaskDetailDTO implements Serializable{
        private String deptName;
        private String rightsName;
        private UserInfoDTO userInfo;
        private RightsUseLog userGoodsAttrInfo;
        private String rightsType;
        private int rightsId;
        private String statusDescribe;
        private long execTime;
        private String remark;
        private String execUser;
        private int id;  //权益ID
        private int execFlag;   //1 =  已结束   0 = 申请中  2 个案师(医生)完成分配任务  3  意外终止
        private String userId;
        private String tradeId;
        private String execName;
        private int uploadDocFlag; //是否上传资料  1 = 上传   其他 不上传


        public RightsUseLog getUserGoodsAttrInfo() {
            return userGoodsAttrInfo;
        }

        public void setUserGoodsAttrInfo(RightsUseLog userGoodsAttrInfo) {
            this.userGoodsAttrInfo = userGoodsAttrInfo;
        }

        public int getUploadDocFlag() {
            return uploadDocFlag;
        }

        public void setUploadDocFlag(int uploadDocFlag) {
            this.uploadDocFlag = uploadDocFlag;
        }

        public String getExecName() {
            return execName;
        }

        public void setExecName(String execName) {
            this.execName = execName;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getRightsName() {
            return rightsName;
        }

        public void setRightsName(String rightsName) {
            this.rightsName = rightsName;
        }

        public UserInfoDTO getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoDTO userInfo) {
            this.userInfo = userInfo;
        }

        public String getRightsType() {
            return rightsType;
        }

        public void setRightsType(String rightsType) {
            this.rightsType = rightsType;
        }

        public int getRightsId() {
            return rightsId;
        }

        public void setRightsId(int rightsId) {
            this.rightsId = rightsId;
        }

        public String getStatusDescribe() {
            return statusDescribe;
        }

        public void setStatusDescribe(String statusDescribe) {
            this.statusDescribe = statusDescribe;
        }

        public long getExecTime() {
            return execTime;
        }

        public void setExecTime(long execTime) {
            this.execTime = execTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getExecUser() {
            return execUser;
        }

        public void setExecUser(String execUser) {
            this.execUser = execUser;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getExecFlag() {
            return execFlag;
        }

        public void setExecFlag(int execFlag) {
            this.execFlag = execFlag;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTradeId() {
            return tradeId;
        }

        public void setTradeId(String tradeId) {
            this.tradeId = tradeId;
        }

        public static class UserInfoDTO implements Serializable{
            private String userSex;
            private boolean isDefault;
            private String phone;
            private String identificationNo;
            private String identificationType;
            private String userType;
            private String userName;
            private int userId;
            private int userAge;


            @Override
            public String toString() {
                return "UserInfoDTO{" +
                        "userSex='" + userSex + '\'' +
                        ", isDefault=" + isDefault +
                        ", phone='" + phone + '\'' +
                        ", identificationNo='" + identificationNo + '\'' +
                        ", identificationType='" + identificationType + '\'' +
                        ", userType='" + userType + '\'' +
                        ", userName='" + userName + '\'' +
                        ", userId=" + userId +
                        ", userAge=" + userAge +
                        '}';
            }

            public String getUserSex() {
                return userSex;
            }

            public void setUserSex(String userSex) {
                this.userSex = userSex;
            }

            public boolean isIsDefault() {
                return isDefault;
            }

            public void setIsDefault(boolean isDefault) {
                this.isDefault = isDefault;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getIdentificationNo() {
                return identificationNo;
            }

            public void setIdentificationNo(String identificationNo) {
                this.identificationNo = identificationNo;
            }

            public String getIdentificationType() {
                return identificationType;
            }

            public void setIdentificationType(String identificationType) {
                this.identificationType = identificationType;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getUserAge() {
                return userAge;
            }

            public void setUserAge(int userAge) {
                this.userAge = userAge;
            }
        }



        public static class RightsUseLog implements Serializable{
            public String timeLimit; //通话时长 (分钟)  15  20
            public int caseFlag;
            public String uploadDocFlag;
            public String docId;
            public int serviceExpire;  //时效 (小时)
            public String textNumLimit;  //条数
            public String whoDeal; //由谁处理 医生/护士


            public String getWhoDeal() {
                return whoDeal;
            }

            public void setWhoDeal(String whoDeal) {
                this.whoDeal = whoDeal;
            }

            public String getTimeLimit() {
                return timeLimit;
            }

            public void setTimeLimit(String timeLimit) {
                timeLimit = timeLimit;
            }

            public int getCaseFlag() {
                return caseFlag;
            }

            public void setCaseFlag(int caseFlag) {
                this.caseFlag = caseFlag;
            }

            public String getUploadDocFlag() {
                return uploadDocFlag;
            }

            public void setUploadDocFlag(String uploadDocFlag) {
                this.uploadDocFlag = uploadDocFlag;
            }

            public String getDocId() {
                return docId;
            }

            public void setDocId(String docId) {
                this.docId = docId;
            }

            public int getServiceExpire() {
                return serviceExpire;
            }

            public void setServiceExpire(int serviceExpire) {
                serviceExpire = serviceExpire;
            }

            public String getTextNumLimit() {
                return textNumLimit;
            }

            public void setTextNumLimit(String textNumLimit) {
                this.textNumLimit = textNumLimit;
            }
        }







        @Override
        public String toString() {
            return "TaskDetailDTO{" +
                    "deptName='" + deptName + '\'' +
                    ", rightsName='" + rightsName + '\'' +
                    ", userInfo=" + userInfo +
                    ", rightsType='" + rightsType + '\'' +
                    ", rightsId=" + rightsId +
                    ", statusDescribe='" + statusDescribe + '\'' +
                    ", execTime=" + execTime +
                    ", remark='" + remark + '\'' +
                    ", execUser='" + execUser + '\'' +
                    ", id=" + id +
                    ", execFlag=" + execFlag +
                    ", userId='" + userId + '\'' +
                    ", tradeId='" + tradeId + '\'' +
                    ",uploadDocFlag='"+uploadDocFlag+'\''+
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TaskDeatailBean{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", taskType=" + taskType +
                ", taskName='" + taskName + '\'' +
                ", taskDescribe='" + taskDescribe + '\'' +
                ", taskDetail=" + taskDetail +
                ", contentId='" + contentId + '\'' +
                ", execTime=" + execTime +
                ", finishRate='" + finishRate + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", execFlag=" + execFlag +
                ", noticeFlag=" + noticeFlag +
                '}';
    }
}
