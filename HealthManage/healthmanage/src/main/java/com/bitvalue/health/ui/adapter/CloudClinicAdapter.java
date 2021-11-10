package com.bitvalue.health.ui.adapter;

import android.util.TypedValue;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 10/28
 */
public class CloudClinicAdapter extends BaseQuickAdapter<VideoClientsResultBean, BaseViewHolder> {
    private OnItemClickCallback itemClick;
    private List<VideoClientsResultBean> currentData;


    public void updateList(List<VideoClientsResultBean> sourceData) {
        currentData.clear();
        this.currentData = sourceData;
    }


    public CloudClinicAdapter(int layoutResId, @Nullable List<VideoClientsResultBean> data, OnItemClickCallback onItemClickCallback) {
        super(layoutResId, data);
        this.itemClick = onItemClickCallback;
        if (null == data) {
            currentData = new ArrayList<>();
        } else {
            currentData = data;
        }
    }


    public void hasCheck(VideoClientsResultBean bean) {
        //将点击的那个置成已点击的状态
        if (currentData != null && currentData.size() > 0) {
            for (int i = 0; i < currentData.size(); i++) {
                currentData.get(i).isClicked = false;
                if (currentData.get(i).userInfo.userId == bean.userInfo.userId) {
                    currentData.get(i).isClicked = true;
                }
            }
        }

    }


    public int hasNewMessage(VideoClientsResultBean bean) {
        if (bean.hasNew) {
            bean.hasNew = false;
            return bean.newMsgNum;
        }
        return 0;
    }


    @Override
    protected void convert(BaseViewHolder holder, VideoClientsResultBean videoClientsResultBean) {
        if (null == videoClientsResultBean) {
            return;
        }
        ImageView img_head = holder.getView(R.id.img_head);
        if (null != videoClientsResultBean.headUrl && !videoClientsResultBean.headUrl.isEmpty()) {
            Glide.with(img_head)
                    .load(videoClientsResultBean.headUrl)
                    .transform(new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            20, Application.instance().getResources().getDisplayMetrics())))
                    .into(img_head);
        } else {
            img_head.setImageDrawable(videoClientsResultBean.userInfo.userSex.equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
        }

        holder.setText(R.id.tv_name, videoClientsResultBean.userInfo.userName);
        holder.setText(R.id.tv_sex, videoClientsResultBean.userInfo.userSex);
        holder.setText(R.id.tv_age, videoClientsResultBean.userInfo.userAge + "岁");
        holder.setText(R.id.tv_time, TimeUtils.getTime(videoClientsResultBean.appointmentTime,
                TimeUtils.YY_MM_DD_FORMAT_3).substring(0, 10) + " " + videoClientsResultBean.seeTime);

        holder.itemView.setOnClickListener(view -> {
            hasCheck(videoClientsResultBean);
            itemClick.onItemClick(videoClientsResultBean, hasNewMessage(videoClientsResultBean));
        });
        holder.setBackgroundColor(R.id.layout_item, videoClientsResultBean.isClicked ? mContext.getResources().getColor(R.color.bg_gray_light) : mContext.getResources().getColor(R.color.white));

        holder.setVisible(R.id.img_boll, videoClientsResultBean.hasNew);

        //就诊状态（1：待就诊 2：就诊中 3：已完成）
        switch (videoClientsResultBean.attendanceStatus) {
            case 1:
                holder.setText(R.id.tv_patient_status, "待就诊");
                holder.setTextColor(R.id.tv_patient_status, mContext.getResources().getColor(R.color.text_desc_dark));
                holder.setBackgroundRes(R.id.tv_patient_status, 0);
                break;
            case 2:
                holder.setText(R.id.tv_patient_status, "已就绪");
                holder.setTextColor(R.id.tv_patient_status, mContext.getResources().getColor(R.color.main_blue));
                holder.setBackgroundRes(R.id.tv_patient_status, R.drawable.shape_bg_blue_small);
                break;
            case 3:
                holder.setText(R.id.tv_patient_status, "就诊中");
                holder.setTextColor(R.id.tv_patient_status, mContext.getResources().getColor(R.color.main_blue));
                holder.setBackgroundRes(R.id.tv_patient_status, R.drawable.shape_bg_blue_small);
                break;
            case 4:
                holder.setText(R.id.tv_patient_status, "已完成");
                holder.setTextColor(R.id.tv_patient_status, mContext.getResources().getColor(R.color.text_desc_dark));
                holder.setBackgroundRes(R.id.tv_patient_status, 0);
                break;
        }

    }


}