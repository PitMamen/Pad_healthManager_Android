package com.bitvalue.health.ui.adapter;


import android.view.View;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.callback.OnItemDelete;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 *
 * 语音列表
 */

public class QuestionQuickAdapter extends BaseQuickAdapter<QuestionResultBean.ListDTO, BaseViewHolder> {

    private boolean isNoDelete;
    private OnItemDelete onItemDelete;

    public QuestionQuickAdapter(@LayoutRes int layoutResId, @Nullable List<QuestionResultBean.ListDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, QuestionResultBean.ListDTO paperBean) {
        if (null == paperBean) {
            return;
        }
//        holder.setText(R.id.tv_duration, uploadFileApi.duration + "&quot;");
        holder.setText(R.id.tv_name, paperBean.name);
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
