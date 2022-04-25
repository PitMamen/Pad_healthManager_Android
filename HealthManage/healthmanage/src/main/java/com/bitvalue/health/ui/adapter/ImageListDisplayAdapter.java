package com.bitvalue.health.ui.adapter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import static com.tencent.liteav.demo.beauty.utils.ResourceUtils.getResources;

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

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.util.PhotoDialog;
import com.bitvalue.health.util.photopreview.PhotoView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.picture.imageEngine.impl.GlideEngine;
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
public class ImageListDisplayAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;
    private List<Bitmap> bitmaplist = new ArrayList<>();
    private PhotoDialog dialog;

    public ImageListDisplayAdapter(int layoutResId, @Nullable List<String> data, Context context) {
        super(layoutResId, data);
        mContext = context;
        dialog = new PhotoDialog(context, bitmaplist);
    }


    @Override
    protected void convert(BaseViewHolder holder, String item) {
        if (item == null) {
            return;
        }

        ImageView imageView = holder.getView(R.id.iv_pic);
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

        //点击查看大图 事件
        imageView.setOnClickListener(v -> {
            if (bitmaplist.size() > 0)
                enlargeImageDialog(mContext, bitmaplist, position);
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


}
