package com.tencent.liteav.basic;

import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserModelManager {
    private static final String TAG = "UserModelManager";

    private static final String PER_DATA = "per_profile_manager";
    private static final String PER_USER_MODEL = "per_user_model";
    private static final String PER_USER_DATE = "per_user_publish_video_date";
    private static final String PER_USER_SIG_TIMESTAMP = "per_user_sig_timestamp";

    private static UserModelManager sInstance;
    private UserModel mUserModel;
    private String mUserPubishVideoDate;
    private long mSigTimestamp = 0L;

    public static UserModelManager getInstance() {
        if (sInstance == null) {
            synchronized (UserModelManager.class) {
                if (sInstance == null) {
                    sInstance = new UserModelManager();
                }
            }
        }
        return sInstance;
    }

    public synchronized UserModel getUserModel() {
        if (mUserModel == null) {
            loadUserModel();
        }
        return mUserModel == null ? new UserModel() : mUserModel;
    }

    public synchronized void setUserModel(UserModel model) {
        mUserModel = model;
        try {
            SPUtils.getInstance(PER_DATA).put(PER_USER_MODEL, GsonUtils.toJson(mUserModel));
        } catch (Exception e) {
            Log.d(TAG, "");
        }
    }

    private void loadUserModel() {
        try {
            String json = SPUtils.getInstance(PER_DATA).getString(PER_USER_MODEL);
            mUserModel = GsonUtils.fromJson(json, UserModel.class);
        } catch (Exception e) {
        }
    }

    private String getUserPublishVideoDate() {
        if (mUserPubishVideoDate == null) {
            mUserPubishVideoDate = SPUtils.getInstance(PER_DATA).getString(PER_USER_DATE, "");
        }
        return mUserPubishVideoDate;
    }

    private void setUserPublishVideoDate(String date) {
        mUserPubishVideoDate = date;
        try {
            SPUtils.getInstance(PER_DATA).put(PER_USER_DATE, mUserPubishVideoDate);
        } catch (Exception e) {
        }
    }

    // 首次TRTC打开摄像头提示"Demo特别配置了无限期云端存储"
    public boolean needShowSecurityTips() {
        String profileDate = getUserPublishVideoDate();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String day = formatter.format(date);
        if (!day.equals(profileDate)) {
            setUserPublishVideoDate(day);
            return true;
        }
        return false;
    }

    public void onSignatureUpdated() {
        mSigTimestamp = System.currentTimeMillis();
        try {
            SPUtils.getInstance(PER_DATA).put(PER_USER_SIG_TIMESTAMP, mSigTimestamp);
        } catch (Exception e) {
        }
    }

    public boolean needUpdateSignature(int expireInSecs) {
        if (mSigTimestamp == 0L) {
            mSigTimestamp = SPUtils.getInstance(PER_DATA).getLong(PER_USER_SIG_TIMESTAMP, 0L);
        }
        return System.currentTimeMillis() - mSigTimestamp > expireInSecs * 1000L / 2L;
    }
}