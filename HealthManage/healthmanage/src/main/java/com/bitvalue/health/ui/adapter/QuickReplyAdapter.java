package com.bitvalue.health.ui.adapter;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.requestbean.QuickReplyRequest;
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
public class QuickReplyAdapter extends BaseQuickAdapter<QuickReplyRequest, BaseViewHolder> {
    private OnItemClickCallback itemClick;
    private List<QuickReplyRequest> currentData;



    public void setOnClicListner(OnItemClickCallback callback) {
        this.itemClick = callback;
    }


    public QuickReplyAdapter(int layoutResId, @Nullable List<QuickReplyRequest> data) {
        super(layoutResId, data);
        if (null == data) {
            currentData = new ArrayList<>();
        } else {
            currentData = data;
        }
    }


    @Override
    protected void convert(BaseViewHolder holder, QuickReplyRequest replyBean) {
        if (null == replyBean) {
            return;
        }
        holder.setText(R.id.tv_quick_reply,replyBean.content);
        holder.itemView.setOnClickListener(v -> {
            if (null != itemClick) {
                itemClick.onItemClick(replyBean);
            }
        });
    }
}
