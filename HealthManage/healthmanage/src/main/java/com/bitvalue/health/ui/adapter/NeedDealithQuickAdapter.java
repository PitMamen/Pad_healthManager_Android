package com.bitvalue.health.ui.adapter;


import android.text.format.DateUtils;
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 待办列表 adapter
 */

public class NeedDealithQuickAdapter extends BaseQuickAdapter<TaskDeatailBean, BaseViewHolder> {

    private OnRightClickCallBack onRightClickCallBack;
    private boolean isCasemanagerAcount = true; // 是否是 个案师账号

    public NeedDealithQuickAdapter(@LayoutRes int layoutResId, @Nullable List<TaskDeatailBean> data, boolean isisCasemanagerAcount, OnRightClickCallBack callBack) {
        super(layoutResId, data);
        this.onRightClickCallBack = callBack;
        this.isCasemanagerAcount = isisCasemanagerAcount;
    }

    @Override
    protected void convert(BaseViewHolder holder, TaskDeatailBean taskdeatailBean) {
        if (null == taskdeatailBean || null == taskdeatailBean.getTaskDetail() || taskdeatailBean.getTaskDetail().getUserInfo() == null) {
            return;
        }
        ImageView img_head = holder.getView(R.id.img_head);
        if (!EmptyUtil.isEmpty((taskdeatailBean.getTaskDetail().getUserInfo()))) {
            img_head.setImageDrawable(taskdeatailBean.getTaskDetail().getUserInfo().getUserSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
        }

        if (!EmptyUtil.isEmpty(taskdeatailBean.getTaskDetail().getUserInfo())) {
            holder.setText(R.id.tv_name, taskdeatailBean.getTaskDetail().getUserInfo().getUserName());
        }
        holder.setText(R.id.tv_video_visit, taskdeatailBean.getTaskDetail().getRightsName());
        holder.setText(R.id.tv_patient_sex, taskdeatailBean.getTaskDetail().getUserInfo().getUserSex());
        holder.setText(R.id.tv_patient_age, taskdeatailBean.getTaskDetail().getUserInfo().getUserAge() + "岁");
        //如果是个案师账号 界面 正常显示
        if (isCasemanagerAcount) {
            holder.setText(R.id.tv_equity_use, taskdeatailBean.getTaskName());
            holder.setText(R.id.tv_time, TimeUtils.getTime_(taskdeatailBean.getExecTime()));
        } else {
            //医生账号 需显示 服务时效 以及 服务状态  如果当前时间  大于 服务执行时间  则是就诊中
//            Log.e(TAG, "权益执行时间: " + TimeUtils.getTime_tosecond(taskdeatailBean.getExecTime()) + " 是否确认: " + taskdeatailBean.getTaskDetail().getExecFlag() + " 姓名： " + taskdeatailBean.getTaskDetail().getUserInfo().getUserName() + " 服务时效：" + taskdeatailBean.getTaskDetail().getUserGoodsAttrInfo().getServiceExpire());
            if (System.currentTimeMillis() - taskdeatailBean.getExecTime() > 0 && taskdeatailBean.getTaskDetail().getExecFlag() == 2) {
                holder.setText(R.id.tv_equity_use, "就诊中");
                if (taskdeatailBean.getTaskDetail() != null
                        && !EmptyUtil.isEmpty(taskdeatailBean.getTaskDetail().getUserGoodsAttrInfo())
                        && !EmptyUtil.isEmpty(taskdeatailBean.getTaskDetail().getUserGoodsAttrInfo().getServiceExpire())) {  //如果没有服务时效  就不显示
                    int serviceContinueTime = taskdeatailBean.getTaskDetail().getUserGoodsAttrInfo().getServiceExpire();
                    long excetime = taskdeatailBean.getExecTime();  //执行时间

                    String addAfterTime = TimeUtils.AddDataMinut(TimeUtils.getTime_tosecond(excetime), serviceContinueTime);
//                    Log.e(TAG, "convert111 最后确认时间: " + addAfterTime);
                    float resultTime = TimeUtils.dateDiff(addAfterTime); //剩余时间=申请时间+服务时效-当前时间
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String time = decimalFormat.format(resultTime);
                    try {
                        if (Float.parseFloat(time) <= 0) {
                            time = "0.0";
                        }
                    } catch (Exception e) {
                        time = "0.0";
                        Log.e(TAG, "Float.parseFloat error-> ");
                    }
                    holder.setText(R.id.tv_time, String.format("服务时效:剩余 %s 小时", time));
//                    Log.e(TAG, "添加后时间: " + addAfterTime + " 需添加时间：" + serviceContinueTime + "  添加前时间：" + TimeUtils.getTime_tosecond(excetime) + " 姓名：" + taskdeatailBean.getTaskDetail().getUserInfo().getUserName());
                    holder.getView(R.id.tv_time).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.tv_time).setVisibility(View.GONE);
                }
                holder.getView(R.id.tv_remind).setVisibility(View.GONE);  //隐藏 提醒上线通知
            } else {
                holder.getView(R.id.tv_remind).setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_equity_use, "待就诊");
                if (taskdeatailBean.getTaskDetail().getExecFlag() == 0) {
                    holder.getView(R.id.tv_time).setVisibility(View.GONE);
                } else {
                    holder.setText(R.id.tv_time, "预约时间:" + TimeUtils.getTime_tosecond(taskdeatailBean.getExecTime()));
                }
                //发送上线提醒
                holder.getView(R.id.tv_remind).setOnClickListener(v -> {
                    if (onRightClickCallBack != null) {
                        onRightClickCallBack.OnRemindClick(taskdeatailBean);
                    }
                });

            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (onRightClickCallBack != null) {
                onRightClickCallBack.OnItemClick(taskdeatailBean);
            }
        });
    }
}
