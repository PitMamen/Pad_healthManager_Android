package com.bitvalue.sdk.collab.modules.group.apply;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.modules.contact.FriendProfileLayout;
import com.bitvalue.sdk.collab.utils.TUIKitConstants;

public class GroupApplyMemberActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_apply_member_activity);
        FriendProfileLayout layout = findViewById(R.id.friend_profile);

        layout.initData(getIntent().getSerializableExtra(TUIKitConstants.ProfileType.CONTENT));
    }

}
