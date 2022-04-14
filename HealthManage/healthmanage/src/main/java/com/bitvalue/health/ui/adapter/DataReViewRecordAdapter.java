package com.bitvalue.health.ui.adapter;

import android.util.Log;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.QueryRightsRecordBean;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 04/13
 */
public class DataReViewRecordAdapter extends BaseQuickAdapter<DataReViewRecordResponse, BaseViewHolder> {

    public DataReViewRecordAdapter(int layoutResId, @Nullable List<DataReViewRecordResponse> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, DataReViewRecordResponse item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.tv_time, TimeUtils.getTime_tosecond(Long.parseLong(item.getCreateTime())));
        holder.setText(R.id.tv_describe, item.getDealDetail());
    }
}
