package com.bitvalue.health.ui.adapter;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.requestbean.SummaryBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 04/27
 */
public class MedicalRecordAdapter extends BaseQuickAdapter<SummaryBean, BaseViewHolder> {
    public MedicalRecordAdapter(int layoutResId, @Nullable List<SummaryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SummaryBean item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tv_time, TimeUtils.getTimeToDay(Long.parseLong(item.getCreateTime())));
        helper.setText(R.id.tv_right_name,item.getRightsName());
    }
}
