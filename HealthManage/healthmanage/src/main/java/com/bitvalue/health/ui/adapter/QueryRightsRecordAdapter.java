package com.bitvalue.health.ui.adapter;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.QueryRightsRecordBean;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/21
 * 权益流转列表记录 Adapter
 */
public class QueryRightsRecordAdapter extends BaseQuickAdapter<QueryRightsRecordBean.RowsDTO, BaseViewHolder> {
    public QueryRightsRecordAdapter(int layoutResId, @Nullable List<QueryRightsRecordBean.RowsDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, QueryRightsRecordBean.RowsDTO item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.tv_time, item.getExecTime());
        holder.setText(R.id.tv_describe, item.getStatusDescribe());


    }
}
