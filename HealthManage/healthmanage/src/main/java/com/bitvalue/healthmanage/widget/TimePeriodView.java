package com.bitvalue.healthmanage.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimePeriodView extends LinearLayout {

    @BindView(R.id.wv_num)
    WheelView wv_num;

    @BindView(R.id.wv_unit)
    WheelView wv_unit;

    private Context homeActivity;
    private int mNum = 0;
    private String mUnit = "天";
    private GetTimeCallBack getTimeCallBack;

    public TimePeriodView(Context context) {
        super(context);
        initView(context);
    }

    public TimePeriodView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TimePeriodView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TimePeriodView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        homeActivity = (HomeActivity) context;
        View.inflate(context, R.layout.layout_time_period, this);
        ButterKnife.bind(this);

        initNumWheel();
        initUnitWheel();
    }

    private void initNumWheel() {
        wv_num.setCyclic(false);

        final List<String> mOptionsItems = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            mOptionsItems.add(i + "");
        }

        wv_num.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        wv_num.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mNum = Integer.parseInt(mOptionsItems.get(index));
            }
        });
    }

    @OnClick({R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                if (null != getTimeCallBack) {
                    getTimeCallBack.onGetTime(countDay() + "", mNum + mUnit + "后");
                }
                break;
        }
    }

    private int countDay() {
        switch (mUnit) {
            case "天":
                return mNum;
            case "周":
                return mNum * 7;
            case "月":
                return mNum * 30;
        }
        return 0;
    }

    public interface GetTimeCallBack {
        void onGetTime(String day, String s);
    }

    public void setGetTimeCallBack(GetTimeCallBack getTimeCallBack) {
        this.getTimeCallBack = getTimeCallBack;
    }

    private void initUnitWheel() {
        wv_unit.setCyclic(false);

        final List<String> mOptionsItems = new ArrayList<>();

        mOptionsItems.add("天");
        mOptionsItems.add("周");
        mOptionsItems.add("月");

        wv_unit.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        wv_unit.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mUnit = mOptionsItems.get(index);
            }
        });
    }

}
