package com.bitvalue.sdk.collab.helper;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.bitvalue.sdk.collab.modules.chat.base.OfflineMessageBean;
import com.tencent.imsdk.v2.V2TIMMessage;

public interface IBaseLiveListener {
    void handleOfflinePushCall(Intent intent);
    void handleOfflinePushCall(OfflineMessageBean bean);
    void redirectCall(OfflineMessageBean bean);
    Fragment getSceneFragment();
    void refreshUserInfo();
    boolean isDialingMessage(V2TIMMessage message);
    Intent putCallExtra(Intent intent, String key, V2TIMMessage message);

}
