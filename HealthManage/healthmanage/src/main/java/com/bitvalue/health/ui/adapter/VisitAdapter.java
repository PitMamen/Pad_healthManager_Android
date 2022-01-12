package com.bitvalue.health.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.api.responsebean.VisitBean;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 11/11
 */
public class VisitAdapter extends BaseQuickAdapter<VisitBean, BaseViewHolder> {
    private OnItemClickCallback itemClick;

    public VisitAdapter(int layoutResId, OnItemClickCallback onItemClickCallback,ArrayList<VisitBean> beanlist) {
        super(layoutResId,beanlist);
        this.itemClick = onItemClickCallback;
    }


    @Override
    protected void convert(BaseViewHolder holder, VisitBean item) {


        }

}
