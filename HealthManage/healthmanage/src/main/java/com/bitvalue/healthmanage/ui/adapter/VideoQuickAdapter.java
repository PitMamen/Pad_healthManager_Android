package com.bitvalue.healthmanage.ui.adapter;


import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.VideoResultBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 *
 * 语音列表
 */

public class VideoQuickAdapter extends BaseQuickAdapter<VideoResultBean.ListDTO, BaseViewHolder> {

    public VideoQuickAdapter(@LayoutRes int layoutResId, @Nullable List<VideoResultBean.ListDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoResultBean.ListDTO listDTO) {
        if (null == listDTO) {
            return;
        }
//        holder.setText(R.id.tv_duration, uploadFileApi.duration + "&quot;");
        holder.setText(R.id.tv_name, listDTO.title);

//        if (null != couponInfo.isChoosed) {//后台获取时，isChoosed可能为null，所以加一个判断
//            holder.setChecked(R.id.checkbox_choose, couponInfo.isChoosed);
//        } else {
//            holder.setChecked(R.id.checkbox_choose, false);
//        }
////        holder.setText(R.id.tv_coupon_item_title, "优惠券");//5.0不显示  5.0之后显示活动或者优惠券
//        holder.setVisible(R.id.tv_coupon_item_title, false);
//
//        holder.setText(R.id.tv_coupon_type, CouponListActivity.getCouponTypeString(couponInfo.applicableTypes));//（236001:全场通用；236002：图书；236003：课程；236004：游戏）
//        holder.setText(R.id.tv_invalid_time, "限" + CouponListActivity.subDateString(couponInfo.invalidTime) + "前使用");
//        holder.setText(R.id.tv_coupon_amount, CouponListActivity.getAmountString(couponInfo));//int 转换成string
//        holder.setText(R.id.tv_applicable_threshold, "满" + couponInfo.applicableThreshold + "可用");
    }

}
