package com.bitvalue.health.ui.adapter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


import static com.bitvalue.health.util.Constants.PREVIEWTARGET_HEIGHT;
import static com.bitvalue.health.util.Constants.PREVIEWTARGET_WIDTH;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.util.PhotoDialog;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 01/07
 */
public class ImageListDisplayAdapter extends BaseQuickAdapter<TaskDetailBean.HealthImagesDTO, BaseViewHolder> {

    private Context mContext;
    private List<String> bitmaplist = new ArrayList<>();
    private PhotoDialog dialog;

    public ImageListDisplayAdapter(int layoutResId, @Nullable List<TaskDetailBean.HealthImagesDTO> data, Context context) {
        super(layoutResId, data);
        mContext = context;
//        dialog = new PhotoDialog(context, bitmaplist);
    }


    @Override
    public void setNewData(@Nullable List<TaskDetailBean.HealthImagesDTO> data) {
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                bitmaplist.add(data.get(i).getFileUrl());
            }
        }
        super.setNewData(data);

    }

    @Override
    protected void convert(BaseViewHolder holder, TaskDetailBean.HealthImagesDTO item) {
        if (item == null) {
            return;
        }
        ImageView imageView = holder.getView(R.id.iv_pic);
        int position = holder.getAdapterPosition();
        Picasso.with(mContext).load(item.getPreviewFileUrl()).error(R.drawable.image_error_bg).resize(PREVIEWTARGET_WIDTH,PREVIEWTARGET_HEIGHT).onlyScaleDown().into(imageView);

        //点击查看大图 事件
        imageView.setOnClickListener(v -> {
            if (bitmaplist.size() > 0)
                enlargeImageDialog(position);
        });
    }

    //点击图片放大
    private void enlargeImageDialog(int position) {
        dialog = new PhotoDialog(mContext, bitmaplist);
        dialog.onClickPosition(position);
        dialog.updateData(bitmaplist);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setOnCancelListener(dialog -> {
            dialog.dismiss();
            dialog = null;
        });
    }


}
