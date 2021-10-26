package com.bitvalue.healthmanage.ui.adapter;


import android.view.View;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.callback.OnItemDeleteCallback;
import com.bitvalue.healthmanage.http.bean.VideoResultBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 *
 * 语音列表
 */

public class VideoQuickAdapter extends BaseQuickAdapter<VideoResultBean.ListDTO, BaseViewHolder> {

    private OnItemDeleteCallback onItemDeleteCallback;
    private boolean isNoDelete;

    public VideoQuickAdapter(@LayoutRes int layoutResId, @Nullable List<VideoResultBean.ListDTO> data) {
        super(layoutResId, data);
    }

    public void setIsNoDelete(boolean isNoDelete){
        this.isNoDelete = isNoDelete;
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoResultBean.ListDTO listDTO) {
        if (null == listDTO) {
            return;
        }
        holder.setText(R.id.tv_name, listDTO.title);
        ImageView img_delete = holder.getView(R.id.img_delete);
        if (isNoDelete){
            img_delete.setVisibility(View.GONE);
        }
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (onItemDeleteCallback != null){
                    onItemDeleteCallback.onItemDelete(position);
                }
            }
        });
    }

    public OnItemDeleteCallback getOnItemDelete() {
        return onItemDeleteCallback;
    }

    public void setOnItemDelete(OnItemDeleteCallback onItemDeleteCallback) {
        this.onItemDeleteCallback = onItemDeleteCallback;
    }

}
