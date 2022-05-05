package com.bitvalue.health.util.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bitvalue.healthmanage.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author created by bitvalue
 * @data : 05/04
 * 时间确认Dialog
 */
public class ConfirmTimeDialog extends Dialog implements View.OnClickListener {
    public TextView tv_time1, tv_time2, tv_time3,btn_confirmtime;
    public RadioButton radioButtonTime1, radioButtonTime2, radioButtonTime3;
    private List<String> sourceTime;
    private String confirmTime = "";
    private OnConfirmTimeClickListener onConfirmTimeClickListener;
    private String docconfirmTime = "";
    public ConfirmTimeDialog(@NonNull Context context, List<String> times) {
        super(context);
        setCancelable(true);
        if (times == null) {
            sourceTime = new ArrayList<>();
        } else {
            sourceTime = times;
        }
    }

    public ConfirmTimeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ConfirmTimeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirmtime_layout);
        setCanceledOnTouchOutside(true);
        initView();
        initData();
    }

    private void initView() {
        tv_time1 = findViewById(R.id.selecttime_one);
        tv_time2 = findViewById(R.id.selecttime_two);
        tv_time3 = findViewById(R.id.selecttime_three);
        radioButtonTime1 = findViewById(R.id.radio_time_one);
        radioButtonTime2 = findViewById(R.id.radio_time_two);
        radioButtonTime3 = findViewById(R.id.radio_time_three);
        btn_confirmtime = findViewById(R.id.tv_confirm_time);
        tv_time1.setOnClickListener(this);
        tv_time2.setOnClickListener(this);
        tv_time3.setOnClickListener(this);
        radioButtonTime1.setOnClickListener(this);
        radioButtonTime2.setOnClickListener(this);
        radioButtonTime3.setOnClickListener(this);
        btn_confirmtime.setOnClickListener(this);
    }

    private void initData() {
        if (sourceTime != null && sourceTime.size() > 0) {
            tv_time1.setText(sourceTime.get(0));
            tv_time2.setText(sourceTime.get(1));
            tv_time3.setText(sourceTime.get(2));
        }
    }



    //医生确认的时间
    public String getconfirmedTime() {
        return docconfirmTime;
    }


    public ConfirmTimeDialog setOnClickListener(ConfirmTimeDialog.OnConfirmTimeClickListener onclickListener) {
        this.onConfirmTimeClickListener = onclickListener;
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_time_one:
            case R.id.selecttime_one:
                radioButtonTime1.setChecked(true);
                radioButtonTime2.setChecked(false);
                radioButtonTime3.setChecked(false);
                confirmTime = tv_time1.getText().toString();
                break;
            case R.id.radio_time_two:
            case R.id.selecttime_two:
                radioButtonTime2.setChecked(true);
                radioButtonTime1.setChecked(false);
                radioButtonTime3.setChecked(false);
                confirmTime = tv_time2.getText().toString();
                break;
            case R.id.radio_time_three:
            case R.id.selecttime_three:
                radioButtonTime3.setChecked(true);
                radioButtonTime1.setChecked(false);
                radioButtonTime2.setChecked(false);
                confirmTime = tv_time3.getText().toString();
                break;

            //提交时间
            case R.id.tv_confirm_time:
                if (onConfirmTimeClickListener != null) {
                    docconfirmTime = confirmTime;
                    onConfirmTimeClickListener.onPositiveClick();
                }
                break;
        }
    }


    public interface OnConfirmTimeClickListener {
        /**
         * 点击确定按钮事件
         */
        void onPositiveClick();

    }

}
