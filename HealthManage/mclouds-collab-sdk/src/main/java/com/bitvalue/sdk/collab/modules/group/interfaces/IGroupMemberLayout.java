package com.bitvalue.sdk.collab.modules.group.interfaces;


import com.bitvalue.sdk.collab.base.ILayout;
import com.bitvalue.sdk.collab.modules.group.info.GroupInfo;

public interface IGroupMemberLayout extends ILayout {

    void setDataSource(GroupInfo dataSource);

}
