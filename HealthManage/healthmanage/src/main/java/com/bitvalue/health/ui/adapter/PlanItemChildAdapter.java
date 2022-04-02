package com.bitvalue.health.ui.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 01/21
 * 计划列表 子item adapter
 */
public class PlanItemChildAdapter extends BaseQuickAdapter<NewLeaveBean.RowsDTO.PlanInfoDetailDTO, BaseViewHolder> {

    public PlanItemChildAdapter(@Nullable List<NewLeaveBean.RowsDTO.PlanInfoDetailDTO> data) {
        super(R.layout.childitem_planlist_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, NewLeaveBean.RowsDTO.PlanInfoDetailDTO item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.tv_plan_title, item.getPlanName() != null ? item.getPlanName() : item.getGoodsName());
//        holder.addOnClickListener(R.id.iv_send_msg).addOnClickListener(R.id.iv_check_plan);

    }
}
