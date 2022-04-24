package com.bitvalue.health.ui.adapter;

import android.util.Log;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.MyRightBean;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/21
 * <p>
 * 服务权益列表 adapter
 */
public class RightUseTimeRecordAdapter extends BaseQuickAdapter<MyRightBean.UserGoodsAttr, BaseViewHolder> {
    public RightUseTimeRecordAdapter(int layoutResId, @Nullable List<MyRightBean.UserGoodsAttr> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, MyRightBean.UserGoodsAttr item) {
        if (item == null) {
            return;
        }
        String attrName = "";
        switch (item.getAttrName()) {
            case "videoNum":
                attrName = "视频咨询";
                break;
            case "textNum":
                attrName = "图文咨询";
                break;
            case "appointBedNum":
                attrName = "床位预约";
                break;
            case "appointNum":
                attrName = "复诊预约";
                break;
            case "ICUConsultNum":
                attrName = "重症会诊";
                break;
            case "telNum":
                attrName = "电话随访追踪";
                break;
            case "eatEvaluateNum":
                attrName = "膳食点评";
                break;
            case "sportEvaluateNum":
                attrName = "身体活动情况点评";
                break;
            case "mailNum":
                attrName = "邮寄服务";
                break;
        }
        holder.setText(R.id.tv_video, attrName);
        holder.setText(R.id.tv_surplustimes, Integer.valueOf(item.getAttrValue()) - Integer.valueOf(item.getUsedValue()) + "");
        holder.setText(R.id.tv_totaltimes, item.getAttrValue());
    }
}
