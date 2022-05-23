package com.bitvalue.health.util.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bitvalue.health.ui.adapter.SetTimeDialogAdapter;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.hjq.toast.ToastUtils;
import com.tencent.liteav.meeting.ui.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 05/04
 */
public class SetConsultionTimeDialog extends Dialog {
    private WrapRecyclerView wrapRecyclerView_time;
    private Context mContext;
    private List<String> time_list = new ArrayList<>();
    private SetTimeDialogAdapter setTimeDialogAdapter;
    private String selectTime = ""; //选择的时间
    private OnItemClickListener itemClickListener;

    public SetConsultionTimeDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        setCancelable(true);
    }

    public SetConsultionTimeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SetConsultionTimeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settime_consultion);
        setCanceledOnTouchOutside(true);
        initData();
        initView();
    }


    private void initData() {
        time_list.add("06:00-07:00");
        time_list.add("07:00-08:00");
        time_list.add("08:00-09:00");
        time_list.add("09:00-10:00");
        time_list.add("10:00-11:00");
        time_list.add("11:00-12:00");
        time_list.add("12:00-13:00");
        time_list.add("13:00-14:00");
        time_list.add("14:00-15:00");
        time_list.add("15:00-16:00");
        time_list.add("16:00-17:00");
        time_list.add("17:00-18:00");
        time_list.add("18:00-19:00");
        time_list.add("19:00-20:00");
        time_list.add("20:00-21:00");
        time_list.add("21:00-22:00");
        time_list.add("22:00-23:00");
        time_list.add("23:00-23:59");
    }


    private void initView() {
        wrapRecyclerView_time = findViewById(R.id.list_time);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        setTimeDialogAdapter = new SetTimeDialogAdapter(R.layout.item_settime_dialog_layout, time_list, mContext);
        wrapRecyclerView_time.setLayoutManager(layoutManager);
        wrapRecyclerView_time.setAdapter(setTimeDialogAdapter);
        setTimeDialogAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (itemClickListener != null) {
                selectTime = time_list.get(position);
                if (!EmptyUtil.isEmpty(selectTime) && selectTime.contains("-")) {
                    String[] time_paragraph = selectTime.split("-"); // 07:00-08:00
                    String currentTime = TimeUtils.getCurrentTimeMinute();
                    if (currentTime.compareTo(time_paragraph[1]) > 0) {
                        ToastUtils.show("该时间段不可选");
                        return;
                    }
                    itemClickListener.onPositiveClick();
                }

            }

        });
    }


    //医生选中的时间
    public String getselectedTime() {
        return selectTime;
    }


    public SetConsultionTimeDialog setOnClickListener(OnItemClickListener onclickListener) {
        this.itemClickListener = onclickListener;
        return this;
    }


    public interface OnItemClickListener {
        /**
         * 点击确定按钮事件
         */
        void onPositiveClick();

    }
}
