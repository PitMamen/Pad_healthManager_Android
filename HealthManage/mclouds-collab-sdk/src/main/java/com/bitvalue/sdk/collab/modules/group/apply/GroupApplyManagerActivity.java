package com.bitvalue.sdk.collab.modules.group.apply;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.modules.group.info.GroupInfo;
import com.bitvalue.sdk.collab.utils.TUIKitConstants;

public class GroupApplyManagerActivity extends Activity {

    private GroupApplyManagerLayout mManagerLayout;
    private GroupInfo mGroupInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_apply_manager_activity);
        if (getIntent().getExtras() == null) {
            finish();
            return;
        }
        mManagerLayout = findViewById(R.id.group_apply_manager_layout);

        mGroupInfo = (GroupInfo) getIntent().getExtras().getSerializable(TUIKitConstants.Group.GROUP_INFO);
        mManagerLayout.setDataSource(mGroupInfo);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != TUIKitConstants.ActivityRequest.CODE_1) {
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        GroupApplyInfo info = (GroupApplyInfo) data.getSerializableExtra(TUIKitConstants.Group.MEMBER_APPLY);
        if (info == null) {
            return;
        }
        mManagerLayout.updateItemData(info);
    }

}
