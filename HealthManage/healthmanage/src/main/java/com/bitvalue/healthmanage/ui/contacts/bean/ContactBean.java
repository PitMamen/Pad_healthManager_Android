package com.bitvalue.healthmanage.ui.contacts.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactBean implements Parcelable {
    private String name;

    public ContactBean() {
    }

    protected ContactBean(Parcel in) {
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactBean> CREATOR = new Creator<ContactBean>() {
        @Override
        public ContactBean createFromParcel(Parcel in) {
            return new ContactBean(in);
        }

        @Override
        public ContactBean[] newArray(int size) {
            return new ContactBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContactBean(String name) {
        this.name = name;
    }
}
