package com.bitvalue.healthmanage.ui.adapter;


import android.view.View;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.ui.adapter.interfaz.OnItemDelete;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 *
 * 语音列表
 */

public class VideoPatientQuickAdapter extends BaseQuickAdapter<ArticleBean, BaseViewHolder> {

    private OnItemDelete onItemDelete;
    private boolean isNoDelete;

    public VideoPatientQuickAdapter(@LayoutRes int layoutResId, @Nullable List<ArticleBean> data) {
        super(layoutResId, data);
    }

    public void setIsNoDelete(boolean isNoDelete){
        this.isNoDelete = isNoDelete;
    }

    @Override
    protected void convert(BaseViewHolder holder, ArticleBean paperBean) {
        if (null == paperBean) {
            return;
        }
        holder.setText(R.id.tv_name, paperBean.title);
        ImageView img_delete = holder.getView(R.id.img_delete);
        if (isNoDelete){
            img_delete.setVisibility(View.GONE);
        }
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (onItemDelete != null){
                    onItemDelete.onItemDelete(position);
                }
            }
        });
    }

    public OnItemDelete getOnItemDelete() {
        return onItemDelete;
    }

    public void setOnItemDelete(OnItemDelete onItemDelete) {
        this.onItemDelete = onItemDelete;
    }
}
