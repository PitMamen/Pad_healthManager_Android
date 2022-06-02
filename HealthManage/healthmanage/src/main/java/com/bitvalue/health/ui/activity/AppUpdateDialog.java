package com.bitvalue.health.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bitvalue.healthmanage.R;

/**
 * @author created by bitvalue
 * @data : 05/31
 */
public class AppUpdateDialog extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);
    }
}
