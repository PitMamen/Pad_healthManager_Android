package com.bitvalue.health.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.PhotoDialog;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.picture.imageEngine.impl.GlideEngine;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/11
 */
public class MoreDataDetailChildImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Context context;
    private PhotoDialog dialog;
    private List<String> urllist;

    public MoreDataDetailChildImageAdapter(int layoutResId, @Nullable List<String> data, Context mContext) {
        super(layoutResId, data);
        this.context = mContext;
        if (data == null) {
            urllist = new ArrayList<>();
        } else {
            urllist = data;
        }
    }


    @Override
    public void setNewData(@Nullable List<String> data) {
        super.setNewData(data);
        urllist = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        if (EmptyUtil.isEmpty(item)) {
            return;
        }
        ImageView imageView = holder.getView(R.id.iv_pic);
        int position = holder.getAdapterPosition();
        Picasso.with(Application.instance()).load(item.trim()).into(imageView);
        imageView.setOnClickListener(v -> enlargeImageDialog(context, urllist, position));

    }



    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };



    //点击图片放大
    private void enlargeImageDialog(Context context, List<String> stringList, int position) {
        if (dialog == null) {
            dialog = new PhotoDialog(context, stringList);
            dialog.onClickPosition(position);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
            dialog.setOnCancelListener(dialog1 -> {
                dialog.cancel();
                dialog = null;
            });
        }
    }
}
