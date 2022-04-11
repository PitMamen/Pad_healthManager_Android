package com.bitvalue.health.ui.adapter;


import android.util.Log;
import android.view.View;
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

/**
 * 待办列表 adapter
 */

public class NeedDealithQuickAdapter extends BaseQuickAdapter<TaskDeatailBean, BaseViewHolder> {

    private OnRightClickCallBack onRightClickCallBack;

    public NeedDealithQuickAdapter(@LayoutRes int layoutResId, @Nullable List<TaskDeatailBean> data, OnRightClickCallBack callBack) {
        super(layoutResId, data);
        this.onRightClickCallBack = callBack;
    }



    @Override
    protected void convert(BaseViewHolder holder, TaskDeatailBean taskdeatailBean) {
        if (null == taskdeatailBean||null==taskdeatailBean.getTaskDetail()) {
            return;
        }
        ImageView img_head = holder.getView(R.id.img_head);
        if (!EmptyUtil.isEmpty((taskdeatailBean.getTaskDetail().getUserInfo()))){
            img_head.setImageDrawable(taskdeatailBean.getTaskDetail().getUserInfo().getUserSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
        }

        holder.setText(R.id.tv_name, taskdeatailBean.getTaskDetail().getUserInfo().getUserName());
        holder.setText(R.id.tv_video_visit, taskdeatailBean.getTaskDetail().getRightsType().equals("videoNum")?"视频问诊":"图文咨询");
        holder.setText(R.id.tv_patient_sex, taskdeatailBean.getTaskDetail().getUserInfo().getUserSex());
        holder.setText(R.id.tv_patient_age, taskdeatailBean.getTaskDetail().getUserInfo().getUserAge() + "岁");
        holder.setText(R.id.tv_equity_use, taskdeatailBean.getTaskName());
        holder.setText(R.id.tv_time, TimeUtils.getTime_(taskdeatailBean.getExecTime()));


        holder.itemView.setOnClickListener(v -> {
            if (onRightClickCallBack != null) {
                onRightClickCallBack.OnItemClick(taskdeatailBean);
            }
        });

    }

}
