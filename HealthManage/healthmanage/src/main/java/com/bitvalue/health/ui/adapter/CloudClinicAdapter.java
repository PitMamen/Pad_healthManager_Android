package com.bitvalue.health.ui.adapter;

import android.util.TypedValue;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.callback.OnItemClick;
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
public class CloudClinicAdapter extends BaseQuickAdapter<NewLeaveBean.RowsDTO, BaseViewHolder> {
    private OnItemClick itemClick;
    private List<NewLeaveBean.RowsDTO> currentData;


    public void updateList(List<NewLeaveBean.RowsDTO> sourceData) {
        currentData.clear();
        this.currentData = sourceData;
    }


    public CloudClinicAdapter(int layoutResId, @Nullable List<NewLeaveBean.RowsDTO> data, OnItemClick onItemClickCallback) {
        super(layoutResId, data);
        this.itemClick = onItemClickCallback;
        if (null == data) {
            currentData = new ArrayList<>();
        } else {
            currentData = data;
        }
    }


    public void hasCheck(NewLeaveBean.RowsDTO bean) {
        //将点击的那个置成已点击的状态
        if (currentData != null && currentData.size() > 0) {
            for (int i = 0; i < currentData.size(); i++) {
                currentData.get(i).isChecked = false;
                if (currentData.get(i).getUserId() == bean.getUserId()) {
                    currentData.get(i).isChecked = true;
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
    protected void convert(BaseViewHolder holder, NewLeaveBean.RowsDTO videoClientsResultBean) {
        if (null == videoClientsResultBean) {
            return;
        }
        ImageView img_head = holder.getView(R.id.img_head);
//        if (null != videoClientsResultBean.headUrl && !videoClientsResultBean.headUrl.isEmpty()) {
//            Glide.with(img_head)
//                    .load(videoClientsResultBean.headUrl)
//                    .transform(new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                            20, Application.instance().getResources().getDisplayMetrics())))
//                    .into(img_head);
//        } else {
//        }
        img_head.setImageDrawable(videoClientsResultBean.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));

        holder.setText(R.id.tv_name, videoClientsResultBean.getUserName());
        holder.setText(R.id.tv_patient_sex, videoClientsResultBean.getSex());
        String curen = TimeUtils.getCurrenTime();
        int finatime = Integer.valueOf(curen) - Integer.valueOf((videoClientsResultBean.getAge().substring(0, 4)));
        holder.setText(R.id.tv_patient_age, finatime + "岁");

        holder.itemView.setOnClickListener(view -> {
            hasCheck(videoClientsResultBean);
            itemClick.onItemClick(videoClientsResultBean, videoClientsResultBean.isChecked);
        });
        holder.setBackgroundColor(R.id.layout_item, videoClientsResultBean.isChecked ? mContext.getResources().getColor(R.color.bg_gray_light) : mContext.getResources().getColor(R.color.white));


        //就诊状态（1：待就诊 2：就诊中 3：已完成）
//        switch (videoClientsResultBean.get) {
//            case 1:
//                holder.setText(R.id.tv_patient_status, "待就诊");
//                holder.setTextColor(R.id.tv_patient_status, mContext.getResources().getColor(R.color.text_desc_dark));
//                holder.setBackgroundRes(R.id.tv_patient_status, 0);
//                break;
//            case 2:
//                holder.setText(R.id.tv_patient_status, "已就绪");
//                holder.setTextColor(R.id.tv_patient_status, mContext.getResources().getColor(R.color.main_blue));
//                holder.setBackgroundRes(R.id.tv_patient_status, R.drawable.shape_bg_blue_small);
//                break;
//            case 3:
//                holder.setText(R.id.tv_patient_status, "就诊中");
//                holder.setTextColor(R.id.tv_patient_status, mContext.getResources().getColor(R.color.main_blue));
//                holder.setBackgroundRes(R.id.tv_patient_status, R.drawable.shape_bg_blue_small);
//                break;
//            case 4:
//                holder.setText(R.id.tv_patient_status, "已完成");
//                holder.setTextColor(R.id.tv_patient_status, mContext.getResources().getColor(R.color.text_desc_dark));
//                holder.setBackgroundRes(R.id.tv_patient_status, 0);
//                break;
//        }

    }


}
