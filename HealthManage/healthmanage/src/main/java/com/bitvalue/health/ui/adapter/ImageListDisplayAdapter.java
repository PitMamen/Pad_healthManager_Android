package com.bitvalue.health.ui.adapter;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.picture.imageEngine.impl.GlideEngine;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 01/07
 */
public class ImageListDisplayAdapter extends BaseQuickAdapter<String, BaseViewHolder> {



    public ImageListDisplayAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder holder, String item) {
       if (item==null){
           return;
       }
        Log.e(TAG, "URL图片: "+item );
        ImageView imageView = holder.getView(R.id.iv_pic);
        Picasso.with(Application.instance()).load(item.trim()).into(imageView);
//        GlideEngine.loadImage(imageView,item.trim());
    }
}
