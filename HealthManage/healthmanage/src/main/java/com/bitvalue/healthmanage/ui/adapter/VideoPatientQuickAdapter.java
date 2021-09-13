package com.bitvalue.healthmanage.ui.adapter;


import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.glide.GlideApp;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.VideoClientsResultBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.interfaz.OnItemDelete;
import com.bitvalue.healthmanage.util.TimeUtils;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 语音列表
 */

public class VideoPatientQuickAdapter extends BaseQuickAdapter<VideoClientsResultBean, BaseViewHolder> {

    private HomeActivity homeActivity;
    private OnItemDelete onItemDelete;
    private boolean isNoDelete;

    public VideoPatientQuickAdapter(@LayoutRes int layoutResId, @Nullable List<VideoClientsResultBean> data) {
        super(layoutResId, data);
    }

    public void setIsNoDelete(boolean isNoDelete) {
        this.isNoDelete = isNoDelete;
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoClientsResultBean videoClientsResultBean) {
        if (null == videoClientsResultBean) {
            return;
        }

        ImageView img_head = holder.getView(R.id.img_head);

        if (null != videoClientsResultBean.headUrl && !videoClientsResultBean.headUrl.isEmpty()) {
            GlideApp.with(img_head)
//                        .load("http://img.duoziwang.com/2021/03/1623076080632524.jpg")
                    .load(videoClientsResultBean.headUrl)
                    .transform(new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            20, AppApplication.instance().getResources().getDisplayMetrics())))
                    .into(img_head);
        } else {
            img_head.setImageDrawable(AppApplication.instance().getResources().getDrawable(R.drawable.default_head_img));
        }

        holder.setText(R.id.tv_name, videoClientsResultBean.userInfo.userName);
        holder.setText(R.id.tv_sex, videoClientsResultBean.userInfo.userSex);
        holder.setText(R.id.tv_age, videoClientsResultBean.userInfo.userAge + "岁");
        holder.setText(R.id.tv_time, TimeUtils.getTime(videoClientsResultBean.appointmentTime,
                TimeUtils.YY_MM_DD_FORMAT_3).substring(0, 10) + " " + videoClientsResultBean.seeTime);

        if (videoClientsResultBean.isClicked) {
            holder.setBackgroundColor(R.id.layout_item, mContext.getResources().getColor(R.color.divider));
        } else {
            holder.setBackgroundColor(R.id.layout_item, mContext.getResources().getColor(R.color.white));
        }

        //就诊状态（1：待就诊 2：就诊中 3：已完成）
        switch (videoClientsResultBean.attendanceStatus) {
            case 1:
                holder.setText(R.id.tv_patient_status, "待就诊");
                holder.setTextColor(R.id.tv_patient_status, mContext.getResources().getColor(R.color.text_desc_dark));
                holder.setBackgroundRes(R.id.tv_patient_status, 0);
                break;
            case 2:
                holder.setText(R.id.tv_patient_status, "就诊中");
                holder.setTextColor(R.id.tv_patient_status, mContext.getResources().getColor(R.color.main_blue));
                holder.setBackgroundRes(R.id.tv_patient_status, R.drawable.shape_bg_blue_small);
                break;
            case 3:
                holder.setText(R.id.tv_patient_status, "已完成");
                holder.setTextColor(R.id.tv_patient_status, mContext.getResources().getColor(R.color.text_desc_dark));
                holder.setBackgroundRes(R.id.tv_patient_status, 0);
                break;
        }
    }

    public OnItemDelete getOnItemDelete() {
        return onItemDelete;
    }

    public void setOnItemDelete(OnItemDelete onItemDelete) {
        this.onItemDelete = onItemDelete;
    }
}
