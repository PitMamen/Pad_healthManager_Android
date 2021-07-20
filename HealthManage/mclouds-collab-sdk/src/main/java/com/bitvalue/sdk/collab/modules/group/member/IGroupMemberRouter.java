package com.bitvalue.sdk.collab.modules.group.member;

import com.bitvalue.sdk.collab.modules.group.info.GroupInfo;

public interface IGroupMemberRouter {

    void forwardListMember(GroupInfo info);

    void forwardAddMember(GroupInfo info);

    void forwardDeleteMember(GroupInfo info);
}
