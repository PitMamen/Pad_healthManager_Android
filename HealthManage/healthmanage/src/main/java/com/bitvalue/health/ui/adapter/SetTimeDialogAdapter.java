package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 05/04
 */
public class SetTimeDialogAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private List<String> sourceData;

    public SetTimeDialogAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
        if (data == null) {
            sourceData = new ArrayList<>();
        } else {
            sourceData = data;
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        if (item == null) {
            return;
        }
        holder.setText(R.id.tv_time, item);

    }
}
