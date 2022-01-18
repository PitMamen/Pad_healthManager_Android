package com.bitvalue.health.ui.adapter;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.QuickReplyBean;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 10/28
 */
public class QuickReplyAdapter extends BaseQuickAdapter<QuickReplyBean, BaseViewHolder> {
    private OnItemClickCallback itemClick;
    private List<QuickReplyBean> currentData;


    public void updateList(List<QuickReplyBean> sourceData) {
        currentData.clear();
        this.currentData = sourceData;
    }

    public void setOnClicListner(OnItemClickCallback callback) {
        this.itemClick = callback;
    }


    public QuickReplyAdapter(int layoutResId, @Nullable List<QuickReplyBean> data) {
        super(layoutResId, data);
        if (null == data) {
            currentData = new ArrayList<>();
        } else {
            currentData = data;
        }
    }


    @Override
    protected void convert(BaseViewHolder holder, QuickReplyBean replyBean) {
        if (null == replyBean) {
            return;
        }
        holder.setText(R.id.tv_quick_reply, replyBean.getReplyString());
        holder.itemView.setOnClickListener(v -> {
            if (null != itemClick) {
                itemClick.onItemClick(replyBean.getReplyString());
            }
        });
    }
}
