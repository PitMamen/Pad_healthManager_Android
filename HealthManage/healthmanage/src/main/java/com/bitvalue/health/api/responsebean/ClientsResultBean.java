package com.bitvalue.health.api.responsebean;

import android.os.Parcel;
import android.os.Parcelable;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ClientsResultBean extends ExpandableGroup<ClientsResultBean.UserInfoDTO> {

    public List<UserInfoDTO> userInfo;
    public int num;
    public String group;
    public int newMsgNum = 0;

    public ClientsResultBean(String title, List<UserInfoDTO> items) {
        super(title, items);
    }

    public static class UserInfoDTO implements Parcelable {
        public String userSex;
        public int identificationType;
        public String userName;
        public String userId;
        public String planId;
        public String cardNo;
        public int userAge;
        public int accountId;
        public Boolean isDefault;
        public String phone;
        public String identificationNo;
        public String userType;
        public Long beginTime;
        public String relationship;
        public String goodsName;
        public String goodsId;
        public String groupID;

        //区别云门诊和健康管理的标记 100健康管理  101云门诊
        public int chatType;


        public boolean isChecked = false;
        public boolean isClicked = false;
        public boolean hasNew = false;
        public int newMsgNum;
        public boolean isShowCheck = false;
        public String headUrl;
        public boolean noInput;

        public UserInfoDTO() {
        }

        public UserInfoDTO(String userName) {
            this.userName = userName;
        }

        protected UserInfoDTO(Parcel in) {
            userSex = in.readString();
            identificationType = in.readInt();
            userName = in.readString();
            userId = in.readString();
            cardNo = in.readString();
            userAge = in.readInt();
            accountId = in.readInt();
            byte tmpIsDefault = in.readByte();
            isDefault = tmpIsDefault == 0 ? null : tmpIsDefault == 1;
            phone = in.readString();
            identificationNo = in.readString();
            userType = in.readString();
            if (in.readByte() == 0) {
                beginTime = null;
            } else {
                beginTime = in.readLong();
            }
            relationship = in.readString();
            goodsName = in.readString();
        }

        public static final Creator<UserInfoDTO> CREATOR = new Creator<UserInfoDTO>() {
            @Override
            public UserInfoDTO createFromParcel(Parcel in) {
                return new UserInfoDTO(in);
            }

            @Override
            public UserInfoDTO[] newArray(int size) {
                return new UserInfoDTO[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(userSex);
            dest.writeInt(identificationType);
            dest.writeString(userName);
            dest.writeString(userId);
            dest.writeString(cardNo);
            dest.writeInt(userAge);
            dest.writeInt(accountId);
            dest.writeByte((byte) (isDefault == null ? 0 : isDefault ? 1 : 2));
            dest.writeString(phone);
            dest.writeString(identificationNo);
            dest.writeString(userType);
            if (beginTime == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeLong(beginTime);
            }
            dest.writeString(relationship);
            dest.writeString(goodsName);
        }
    }
}
