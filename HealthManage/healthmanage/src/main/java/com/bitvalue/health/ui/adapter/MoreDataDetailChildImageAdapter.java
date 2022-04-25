package com.bitvalue.health.ui.adapter;

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
public class MoreDataDetailChildImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Context context;
    private PhotoDialog dialog;
    private List<Bitmap> bitmaplist = new ArrayList<>();
    private Bitmap bitmap;
    private ImageView imageView;

    public MoreDataDetailChildImageAdapter(int layoutResId, @Nullable List<String> data, Context mContext) {
        super(layoutResId, data);
        this.context = mContext;
        dialog = new PhotoDialog(context, bitmaplist);
    }



    @Override
    public void setNewData(@Nullable List<String> data) {
        super.setNewData(data);
//        urllist = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        if (EmptyUtil.isEmpty(item)) {
            return;
        }
        imageView = holder.getView(R.id.iv_pic);
        int position = holder.getAdapterPosition();
        Picasso.with(mContext).load(item.trim()).error(R.drawable.image_error_bg).into(imageView);
        Observable.just(0).subscribeOn(Schedulers.io()).subscribe(r -> {
            try {
                Bitmap bitmap = Picasso.with(mContext).load(item.trim()).error(R.drawable.image_error_bg).get();
                if (bitmap != null) {
                    bitmaplist.add(bitmap);
                    if (dialog != null) {
                        dialog.updateData(bitmaplist);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "加载图片异常: " + e.getMessage());
            }
        });
        imageView.setOnClickListener(v -> {
            if (bitmaplist.size() > 0)
                enlargeImageDialog(context, bitmaplist, position);
        });

    }


    //点击图片放大
    private void enlargeImageDialog(Context context, List<Bitmap> imageList, int position) {
        dialog.onClickPosition(position);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                bitmaplist.clear();
//            }
//        });
    }



    private class LoadImageThred extends Thread{
        @Override
        public void run() {
            super.run();
        }
    }


}
