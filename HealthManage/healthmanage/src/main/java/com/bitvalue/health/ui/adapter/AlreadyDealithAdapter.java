package com.bitvalue.health.ui.adapter;

/**
 * @author created by bitvalue
 * @data : 03/01
 */

import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.TaskDeatailBean;
import com.bitvalue.health.callback.OnRightClickCallBack;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class AlreadyDealithAdapter extends BaseQuickAdapter<TaskDeatailBean, BaseViewHolder> {

    private OnRightClickCallBack onRightClickCallBack;
    private int currentPosition = -1;

    public AlreadyDealithAdapter(@LayoutRes int layoutResId, @Nullable List<TaskDeatailBean> data, OnRightClickCallBack callBack) {
        super(layoutResId, data);
        this.onRightClickCallBack = callBack;
    }

    public void setPosition(int position) {
        this.currentPosition = position;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }


    @Override
    protected void convert(BaseViewHolder holder, TaskDeatailBean videoClientsResultBean) {
        if (null == videoClientsResultBean) {
            return;
        }

        ImageView img_head = holder.getView(R.id.img_head);
        if (!EmptyUtil.isEmpty((videoClientsResultBean.getTaskDetail().getUserInfo()))) {
            img_head.setImageDrawable(videoClientsResultBean.getTaskDetail().getUserInfo().getUserSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
        }
        String rightType = "";
        switch (videoClientsResultBean.getTaskDetail().getRightsType()) {
            case "videoNum":
                rightType = "视频问诊";
                break;
            case "textNum":
                rightType = "图文咨询";
                break;
            case "ICUConsultNum":
                rightType = "重症会诊";
                break;
        }
        holder.setText(R.id.tv_video_visit, rightType);
        holder.setText(R.id.tv_name, videoClientsResultBean.getTaskDetail().getUserInfo().getUserName());
        holder.setText(R.id.tv_patient_sex, videoClientsResultBean.getTaskDetail().getUserInfo().getUserSex());
        holder.setText(R.id.tv_patient_age, videoClientsResultBean.getTaskDetail().getUserInfo().getUserAge() + "岁");
        holder.setText(R.id.tv_equity_use, videoClientsResultBean.getTaskName());
        holder.setText(R.id.tv_time, TimeUtils.getTime_(videoClientsResultBean.getExecTime()));


        holder.itemView.setOnClickListener(v -> {
            if (onRightClickCallBack != null) {
                onRightClickCallBack.OnItemClick(videoClientsResultBean);
            }
        });

    }

}

