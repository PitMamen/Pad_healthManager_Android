package com.bitvalue.health.ui.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.HealthImagesDTO;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.util.TypeConstants;
import com.bitvalue.healthmanage.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HealthTaskImageAdapter extends BaseQuickAdapter<HealthImagesDTO, BaseViewHolder> {

    public static final String TAG = HealthTaskImageAdapter.class.getSimpleName();


    public HealthTaskImageAdapter(List<HealthImagesDTO> list) {
        super(R.layout.item_task_image,list);
    }



    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull HealthImagesDTO item) {


        Glide.with(mContext).load(item.getPreviewFileUrl()).into((ImageView) helper.getView(R.id.img_pic));


    }




}
