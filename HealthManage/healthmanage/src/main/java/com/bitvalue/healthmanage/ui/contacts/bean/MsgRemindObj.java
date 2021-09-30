package com.bitvalue.healthmanage.ui.contacts.bean;

public class MsgRemindObj {
    public int type;//1 健康管理聊天消息；2 云看诊视频消息
    public int num;

    public MsgRemindObj() {
    }

    public MsgRemindObj(int type, int num) {
        this.type = type;
        this.num = num;
    }
}
