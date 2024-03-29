package com.bitvalue.sdk.collab.modules.group.member;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.base.BaseFragment;
import com.bitvalue.sdk.collab.modules.group.info.GroupInfo;
import com.bitvalue.sdk.collab.utils.TUIKitConstants;


public class GroupMemberDeleteFragment extends BaseFragment {

    private GroupMemberDeleteLayout mMemberDelLayout;
    private View mBaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_fragment_del_members, container, false);
        mMemberDelLayout = mBaseView.findViewById(R.id.group_member_del_layout);
        init();
        return mBaseView;
    }

    private void init() {
        mMemberDelLayout.setDataSource((GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO));
        mMemberDelLayout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
    }
}
