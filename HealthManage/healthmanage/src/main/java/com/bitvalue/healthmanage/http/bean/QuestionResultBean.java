package com.bitvalue.healthmanage.http.bean;

import java.io.Serializable;
import java.util.List;

public class QuestionResultBean {

    public List<ListDTO> list;
    public int total;

    public static class ListDTO implements Serializable {
        public String describe;
        public String id;
        public String key;
        public String name;
        public String questUrl;
        public String sourceId;
        public String sourceType;
        public String status;
        public int type;
        public int userId;

        //新增的type字段，用以区分Eventbus消息的类型，用于不同的页面关注
        public String questionFor;

        //用于区分不同任务不同项目的传参
        public int TaskNo;
        public int MissionNo;
    }
}
