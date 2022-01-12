package com.bitvalue.health.ui.adapter;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.InpatientAreaBean;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 01/10
 */
public class InPatientAreaAdapter extends BaseQuickAdapter<InpatientAreaBean, BaseViewHolder> {
    public InPatientAreaAdapter(int layoutResId, @Nullable List<InpatientAreaBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InpatientAreaBean item) {
         helper.setText(R.id.name,item.getName());
    }
}
