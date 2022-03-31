package com.bitvalue.health.api.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 03/25
 * 医生端获取 预诊答卷 列表响应实体
 */
public class AnswerResultBean implements Serializable {
    private List<RecordsDTO> records;
    private int total;
    private int size;
    private int current;
    private List<?> orders;
    private boolean optimizeCountSql;
    private boolean searchCount;
    private Object countId;
    private Object maxLimit;
    private int pages;

    public List<RecordsDTO> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsDTO> records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public List<?> getOrders() {
        return orders;
    }

    public void setOrders(List<?> orders) {
        this.orders = orders;
    }

    public boolean isOptimizeCountSql() {
        return optimizeCountSql;
    }

    public void setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public Object getCountId() {
        return countId;
    }

    public void setCountId(Object countId) {
        this.countId = countId;
    }

    public Object getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Object maxLimit) {
        this.maxLimit = maxLimit;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public static class RecordsDTO {
        private String createTime;
        private String updateTime;
        private int id;
        private String projectKey;
        private int serialNumber;
        private ProcessDataDTO processData;
        private SubmitUaDTO submitUa;
        private String submitOs;
        private String submitBrowser;
        private String submitRequestIp;
        private String submitAddress;
        private int completeTime;
        private Object wxOpenId;
        private WxUserInfoDTO wxUserInfo;
        private int userId;
        private String keyName;


        public String getName() {
            return keyName;
        }

        public void setName(String name) {
            this.keyName = name;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProjectKey() {
            return projectKey;
        }

        public void setProjectKey(String projectKey) {
            this.projectKey = projectKey;
        }

        public int getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(int serialNumber) {
            this.serialNumber = serialNumber;
        }

        public ProcessDataDTO getProcessData() {
            return processData;
        }

        public void setProcessData(ProcessDataDTO processData) {
            this.processData = processData;
        }

        public SubmitUaDTO getSubmitUa() {
            return submitUa;
        }

        public void setSubmitUa(SubmitUaDTO submitUa) {
            this.submitUa = submitUa;
        }

        public String getSubmitOs() {
            return submitOs;
        }

        public void setSubmitOs(String submitOs) {
            this.submitOs = submitOs;
        }

        public String getSubmitBrowser() {
            return submitBrowser;
        }

        public void setSubmitBrowser(String submitBrowser) {
            this.submitBrowser = submitBrowser;
        }

        public String getSubmitRequestIp() {
            return submitRequestIp;
        }

        public void setSubmitRequestIp(String submitRequestIp) {
            this.submitRequestIp = submitRequestIp;
        }

        public String getSubmitAddress() {
            return submitAddress;
        }

        public void setSubmitAddress(String submitAddress) {
            this.submitAddress = submitAddress;
        }

        public int getCompleteTime() {
            return completeTime;
        }

        public void setCompleteTime(int completeTime) {
            this.completeTime = completeTime;
        }

        public Object getWxOpenId() {
            return wxOpenId;
        }

        public void setWxOpenId(Object wxOpenId) {
            this.wxOpenId = wxOpenId;
        }

        public WxUserInfoDTO getWxUserInfo() {
            return wxUserInfo;
        }

        public void setWxUserInfo(WxUserInfoDTO wxUserInfo) {
            this.wxUserInfo = wxUserInfo;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public static class ProcessDataDTO {
        }

        public static class SubmitUaDTO {
            private OsDTO os;
            private String ua;
            private CpuDTO cpu;
            private DeviceDTO device;
            private EngineDTO engine;
            private BrowserDTO browser;

            public OsDTO getOs() {
                return os;
            }

            public void setOs(OsDTO os) {
                this.os = os;
            }

            public String getUa() {
                return ua;
            }

            public void setUa(String ua) {
                this.ua = ua;
            }

            public CpuDTO getCpu() {
                return cpu;
            }

            public void setCpu(CpuDTO cpu) {
                this.cpu = cpu;
            }

            public DeviceDTO getDevice() {
                return device;
            }

            public void setDevice(DeviceDTO device) {
                this.device = device;
            }

            public EngineDTO getEngine() {
                return engine;
            }

            public void setEngine(EngineDTO engine) {
                this.engine = engine;
            }

            public BrowserDTO getBrowser() {
                return browser;
            }

            public void setBrowser(BrowserDTO browser) {
                this.browser = browser;
            }

            public static class OsDTO {
                private String name;
                private String version;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }
            }

            public static class CpuDTO {
                private String architecture;

                public String getArchitecture() {
                    return architecture;
                }

                public void setArchitecture(String architecture) {
                    this.architecture = architecture;
                }
            }

            public static class DeviceDTO {
            }

            public static class EngineDTO {
                private String name;
                private String version;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }
            }

            public static class BrowserDTO {
                private String name;
                private String major;
                private String version;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getMajor() {
                    return major;
                }

                public void setMajor(String major) {
                    this.major = major;
                }

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }
            }
        }

        public static class WxUserInfoDTO {
        }
    }
}
