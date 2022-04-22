package com.bitvalue.health.ui.adapter;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import static com.tencent.liteav.demo.beauty.utils.ResourceUtils.getResources;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * @author created by bitvalue
 * @data : 01/07
 */
public class ImageListDisplayAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;
    private List<String> list = new ArrayList<>();

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
        list.add(item);
        //点击查看大图 事件
        imageView.setOnClickListener(v -> {
            enlargeImageDialog( mContext,list);
        });
    }

    //点击图片放大
    private void enlargeImageDialog( Context context,List<String> stringList) {
        PhotoDialog dialog = new PhotoDialog(context,stringList);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();


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
    }


}
