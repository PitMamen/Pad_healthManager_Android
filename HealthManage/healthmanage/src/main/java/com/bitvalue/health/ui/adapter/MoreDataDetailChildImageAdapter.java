package com.bitvalue.health.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.picture.imageEngine.impl.GlideEngine;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/11
 */
public class MoreDataDetailChildImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MoreDataDetailChildImageAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        if (EmptyUtil.isEmpty(item)){
            return;
        }
        ImageView imageView = holder.getView(R.id.iv_pic);
        GlideEngine.loadImage(imageView,item);
    }
}
