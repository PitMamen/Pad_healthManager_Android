package com.bitvalue.sdk.collab.modules.forward;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.base.BaseActvity;
import com.bitvalue.sdk.collab.utils.TUIKitLog;


public class ForwardSelectActivity extends BaseActvity {
    private static final String TAG = ForwardSelectActivity.class.getSimpleName();
    /*public static final String path= Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+"conversationInfo.dat";*/

    public static final String FORWARD_MODE = "forward_mode";//0,onebyone;  1,merge;
    public static final int FORWARD_MODE_ONE_BY_ONE = 0;
    public static final int FORWARD_MODE_MERGE = 1;
    private ForwardSelectFragment mForwardSelectFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forward_activity);

        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        TUIKitLog.i(TAG, "onNewIntent");
        super.onNewIntent(intent);;
    }

    @Override
    protected void onResume() {
        TUIKitLog.i(TAG, "onResume");
        super.onResume();
    }

    private void init() {
        mForwardSelectFragment = new ForwardSelectFragment();
        //mForwardSelectFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, mForwardSelectFragment).commitAllowingStateLoss();

        /*FragmentManager fragmentManager = getSupportFragmentManager();        // 开启一个事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.empty_view, mForwardSelectFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();*/
    }
}
