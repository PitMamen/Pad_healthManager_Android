package com.bitvalue.health.ui.adapter;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.DiseaseSpeciesBean;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 01/10
 */
public class DiseaseSpeciesAdapter extends BaseQuickAdapter<DiseaseSpeciesBean, BaseViewHolder> {
    public DiseaseSpeciesAdapter(int layoutResId, @Nullable List<DiseaseSpeciesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiseaseSpeciesBean item) {

         helper.setText(R.id.name,item.getDiseasespeciesName());

    }
}
