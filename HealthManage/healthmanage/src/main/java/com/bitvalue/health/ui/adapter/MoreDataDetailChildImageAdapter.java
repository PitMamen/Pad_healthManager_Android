package com.bitvalue.health.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.picture.imageEngine.impl.GlideEngine;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/11
 */
public class MoreDataDetailChildImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Context context;
    public MoreDataDetailChildImageAdapter(int layoutResId, @Nullable List<String> data,Context mContext) {
        super(layoutResId, data);
        this.context = mContext;
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        if (EmptyUtil.isEmpty(item)) {
            return;
        }
        ImageView imageView = holder.getView(R.id.iv_pic);
        Picasso.with(Application.instance()).load(item.trim()).into(imageView);
        imageView.setOnClickListener(v -> enlargeImageDialog(item.trim(), context));


    }

    //点击图片放大
    private void enlargeImageDialog(String url, Context context) {
        final Dialog dialog = new Dialog(context);
        ImageView image = new ImageView(context);
        Picasso.with(context).load(url).into(image);
        dialog.setCancelable(false);
        dialog.setContentView(image);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();


//        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.width = 800;
//        params.height = 1200;
//        dialog.getWindow().setAttributes(params);
        image.setOnClickListener(v1 -> {
            dialog.cancel();
        });
    }


    //点击图片放大
//    private void enlargeImageDialog(String url, Context context) {
//        final Dialog dialog = new Dialog(context);
//        ImageView image = new ImageView(context);
//        Picasso.with(context).load(url).into(image);
//        dialog.setContentView(image);
//        dialog.setCancelable(false);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.show();
//        image.setOnClickListener(v1 -> {
//            dialog.cancel();
//        });
//    }



}
