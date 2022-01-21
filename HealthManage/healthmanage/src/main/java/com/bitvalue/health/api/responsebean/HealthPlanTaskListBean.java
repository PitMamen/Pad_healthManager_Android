package com.bitvalue.health.api.responsebean;

import java.util.List;

public class HealthPlanTaskListBean {

    private Long end_date;
    private Long create_time;
    private String user_id;
    private String task_describe;
    private Long exec_time;
    private int exec_flag;
    private Integer goods_id;
    private Integer task_id;
    private List<TaskInfoDTO> taskInfo;
    //为了计划显示组装的列表
    private List<TaskInfoDTO> formartTaskInfo;
    private Integer plan_id;
    private String plan_name;
    private Long start_date;

    public List<TaskInfoDTO> getFormartTaskInfo() {
        return formartTaskInfo;
    }

    public void setFormartTaskInfo(List<TaskInfoDTO> formartTaskInfo) {
        this.formartTaskInfo = formartTaskInfo;
    }

    public int getExec_flag() {
        return exec_flag;
    }

    public void setExec_flag(int exec_flag) {
        this.exec_flag = exec_flag;
    }

    public Long getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Long end_date) {
        this.end_date = end_date;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTask_describe() {
        return task_describe;
    }

    public void setTask_describe(String task_describe) {
        this.task_describe = task_describe;
    }

    public Long getExec_time() {
        return exec_time;
    }

    public void setExec_time(Long exec_time) {
        this.exec_time = exec_time;
    }

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public List<TaskInfoDTO> getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(List<TaskInfoDTO> taskInfo) {
        this.taskInfo = taskInfo;
    }

    public Integer getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(Integer plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public Long getStart_date() {
        return start_date;
    }

    public void setStart_date(Long start_date) {
        this.start_date = start_date;
    }
}
