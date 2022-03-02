package com.bitvalue.health.ui.adapter;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.MyRightBean;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/21
 *
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
        holder.setText(R.id.tv_video, item.getAttrName().equals("videoNum") ? "视频咨询" : "图文咨询");
        holder.setText(R.id.tv_surplustimes, Integer.valueOf(item.getAttrValue()) - Integer.valueOf(item.getUsedValue()) + "");
        holder.setText(R.id.tv_totaltimes, item.getAttrValue());
    }
}
