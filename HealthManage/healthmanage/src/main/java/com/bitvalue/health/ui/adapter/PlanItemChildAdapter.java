package com.bitvalue.health.ui.adapter;

import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
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


//        holder.setBackgroundColor(R.id.ll_bg, item.isChecked()?Application.instance().getResources().getColor(R.color.blue_one):Application.instance().getResources().getColor(R.color.white));


       int position= holder.getAdapterPosition()+1;
        holder.setText(R.id.tv_plan_num, "第" + position + "次");
        holder.setText(R.id.tv_plan_title, item.getPlanName());
        holder.addOnClickListener(R.id.iv_send_msg)
        .addOnClickListener(R.id.iv_check_plan);
    }
}
