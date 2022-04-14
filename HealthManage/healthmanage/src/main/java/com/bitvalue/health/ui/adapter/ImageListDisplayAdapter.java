package com.bitvalue.health.ui.adapter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private Context mContext;


    public ImageListDisplayAdapter(int layoutResId, @Nullable List<String> data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder holder, String item) {
        if (item == null) {
            return;
        }
        ImageView imageView = holder.getView(R.id.iv_pic);
        Picasso.with(Application.instance()).load(item.trim()).error(R.drawable.image_error_bg).into(imageView);
        //点击查看大图 事件
        imageView.setOnClickListener(v -> {
            enlargeImageDialog(item.trim(), mContext);
        });
    }

    //点击图片放大
    private void enlargeImageDialog(String url, Context context) {
        final Dialog dialog = new Dialog(context);
        ImageView image = new ImageView(context);
        Picasso.with(context).load(url).into(image);
        dialog.setContentView(image);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        image.setOnClickListener(v1 -> {
            dialog.cancel();
        });
    }


}
