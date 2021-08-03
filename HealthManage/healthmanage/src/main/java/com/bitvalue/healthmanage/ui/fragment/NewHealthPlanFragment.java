package com.bitvalue.healthmanage.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.aop.SingleClick;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.util.InputMethodUtils;
import com.bitvalue.healthmanage.widget.SwitchButton;
import com.hjq.toast.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewHealthPlanFragment extends AppFragment {

    private TextView tv_base_time;
    private TimePickerView pvTime;
    private SwitchButton switch_button;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_health_plan;
    }

    @Override
    protected void initView() {
        tv_base_time = getView().findViewById(R.id.tv_base_time);
        switch_button = getView().findViewById(R.id.switch_button);
        setOnClickListener(R.id.layout_base_time);

        initTimePick();
        initSwitchButton();
    }

    private void initSwitchButton() {
        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
                ToastUtils.show("是否开启--" + isChecked);
            }
        });
    }

    private void initTimePick() {
        //初始化时间选择器
        pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.format(date);
                tv_base_time.setText(simpleDateFormat.format(date).substring(0, 11));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
//                .setRangDate(new GregorianCalendar(1900, 1, 1), Calendar.getInstance())
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_base_time:
                pvTime.show();
                InputMethodUtils.hideSoftInput(getActivity());
                break;
        }
    }
}
