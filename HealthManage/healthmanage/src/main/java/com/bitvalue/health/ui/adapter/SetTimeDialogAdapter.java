package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.toast.ToastUtils;
import com.tencent.liteav.meeting.ui.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 05/04
 */
public class SetTimeDialogAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private List<String> sourceData;
    private Context mContext;

    public SetTimeDialogAdapter(int layoutResId, @Nullable List<String> data, Context context) {
        super(layoutResId, data);
        if (data == null) {
            sourceData = new ArrayList<>();
        } else {
            sourceData = data;
        }
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        if (item == null) {
            return;
        }
        String[] time_paragraph = item.split("-"); // 07:00-08:00
        String currentTime = TimeUtils.getCurrentTimeMinute();
        if (currentTime.compareTo(time_paragraph[1]) > 0) {
            holder.setTextColor(R.id.tv_time, mContext.getColor(R.color.beauty_color_black));
            holder.setBackgroundRes(R.id.tv_time, R.drawable.shape_not_press);
        }
        holder.setText(R.id.tv_time, item);

    }
}
