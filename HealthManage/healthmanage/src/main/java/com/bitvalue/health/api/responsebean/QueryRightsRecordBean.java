package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/21
 */
public class QueryRightsRecordBean implements Serializable {
    private int pageNo;
    private int pageSize;
    private int totalPage;
    private int totalRows;
    private List<RowsDTO> rows;
    private List<Integer> rainbow;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public List<RowsDTO> getRows() {
        return rows;
    }

    public void setRows(List<RowsDTO> rows) {
        this.rows = rows;
    }

    public List<Integer> getRainbow() {
        return rainbow;
    }

    public void setRainbow(List<Integer> rainbow) {
        this.rainbow = rainbow;
    }

    public static class RowsDTO {
        private int id;
        private String userId;
        private String tradeId;
        private int rightsId;
        private String rightsType;
        private String rightsName;
        private String statusDescribe;
        private String execTime;  //执行时间
        private String execUser;
        private int execFlag;
        private String remark;


        @Override
        public String toString() {
            return "RowsDTO{" +
                    "id=" + id +
                    ", userId='" + userId + '\'' +
                    ", tradeId='" + tradeId + '\'' +
                    ", rightsId=" + rightsId +
                    ", rightsType='" + rightsType + '\'' +
                    ", rightsName='" + rightsName + '\'' +
                    ", statusDescribe='" + statusDescribe + '\'' +
                    ", execTime='" + execTime + '\'' +
                    ", execUser='" + execUser + '\'' +
                    ", execFlag=" + execFlag +
                    ", remark='" + remark + '\'' +
                    '}';
        }

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

        public String getTradeId() {
            return tradeId;
        }

        public void setTradeId(String tradeId) {
            this.tradeId = tradeId;
        }

        public int getRightsId() {
            return rightsId;
        }

        public void setRightsId(int rightsId) {
            this.rightsId = rightsId;
        }

        public String getRightsType() {
            return rightsType;
        }

        public void setRightsType(String rightsType) {
            this.rightsType = rightsType;
        }

        public String getRightsName() {
            return rightsName;
        }

        public void setRightsName(String rightsName) {
            this.rightsName = rightsName;
        }

        public String getStatusDescribe() {
            return statusDescribe;
        }

        public void setStatusDescribe(String statusDescribe) {
            this.statusDescribe = statusDescribe;
        }

        public String getExecTime() {
            return execTime;
        }

        public void setExecTime(String execTime) {
            this.execTime = execTime;
        }

        public String getExecUser() {
            return execUser;
        }

        public void setExecUser(String execUser) {
            this.execUser = execUser;
        }

        public int getExecFlag() {
            return execFlag;
        }

        public void setExecFlag(int execFlag) {
            this.execFlag = execFlag;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
