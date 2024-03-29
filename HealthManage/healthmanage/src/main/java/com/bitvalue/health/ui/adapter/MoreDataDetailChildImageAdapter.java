package com.bitvalue.health.ui.adapter;

import static com.bitvalue.health.util.Constants.PREVIEWTARGET_HEIGHT;
import static com.bitvalue.health.util.Constants.PREVIEWTARGET_WIDTH;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.PhotoDialog;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.picture.imageEngine.impl.GlideEngine;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * @author created by bitvalue
 * @data : 02/11
 */
public class MoreDataDetailChildImageAdapter extends BaseQuickAdapter<TaskDetailBean.HealthImagesDTO, BaseViewHolder> {
    private Context context;
    private PhotoDialog dialog;
    private List<String> bitmaplist = new ArrayList<>();
    private Bitmap bitmap;
    private ImageView imageView;

    public MoreDataDetailChildImageAdapter(int layoutResId, @Nullable List<TaskDetailBean.HealthImagesDTO> data, Context mContext) {
        super(layoutResId, data);
        this.context = mContext;
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
        if (EmptyUtil.isEmpty(item)) {
            return;
        }
        imageView = holder.getView(R.id.iv_pic);
        int position = holder.getAdapterPosition();
        Picasso.with(mContext).load(item.getPreviewFileUrl()).error(R.drawable.image_error_bg).resize(PREVIEWTARGET_WIDTH,PREVIEWTARGET_HEIGHT).onlyScaleDown().into(imageView);
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
