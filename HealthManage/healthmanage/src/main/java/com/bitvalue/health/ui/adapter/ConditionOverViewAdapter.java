package com.bitvalue.health.ui.adapter;

import static com.bitvalue.health.util.Constants.PREVIEWTARGET_HEIGH1;
import static com.bitvalue.health.util.Constants.PREVIEWTARGET_HEIGHT;
import static com.bitvalue.health.util.Constants.PREVIEWTARGET_WIDTH;
import static com.bitvalue.health.util.Constants.PREVIEWTARGET_WIDTH1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.GlideApp;
import com.bitvalue.health.util.PhotoDialog;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.picture.imageEngine.impl.GlideEngine;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 05/06
 */
public class ConditionOverViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Context mContext;
    private List<String> bitmaplist = new ArrayList<>();
    private PhotoDialog dialog;


    public ConditionOverViewAdapter(int layoutResId, @Nullable List<String> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
        bitmaplist = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        if (EmptyUtil.isEmpty(item)) {
            return;
        }
        ImageView imageView = holder.getView(R.id.iv_pic);
        int position = holder.getAdapterPosition();
        Picasso.with(mContext).setLoggingEnabled(true);
        Picasso.with(mContext).load(item.trim()).config(Bitmap.Config.RGB_565).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).fit().error(R.drawable.image_error_bg).into(imageView);

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
