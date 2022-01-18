package com.bitvalue.health.ui.adapter;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.PlanListBean;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 11/29
 */
public class PlansAdapter extends BaseQuickAdapter<PlanListBean, BaseViewHolder> {
    private List<PlanListBean> newlyDisChaegedPaBeanList;


    public PlansAdapter(int layoutResId, @Nullable List<PlanListBean> data) {
        super(layoutResId, data);
        this.newlyDisChaegedPaBeanList = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, PlanListBean item) {

        if (null == item) {
            return;
        }
        holder.setText(R.id.tv_plan_name, item.templateName);
        holder.setText(R.id.tv_bone_number, item.userNum + "");
    }
}
