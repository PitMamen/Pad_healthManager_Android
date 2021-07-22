package com.bitvalue.healthmanage.ui.contacts.bean;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class ContactsGroupBean extends ExpandableGroup<ContactBean> {
    private List<ContactBean> mList = new ArrayList<>();
    private String groupName;

    public ContactsGroupBean(String title, List<ContactBean> items) {
        super(title, items);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ContactBean> getChildList() {
        return mList;
    }

    public void setChildList(List<ContactBean> mList) {
        this.mList = mList;
    }

}
