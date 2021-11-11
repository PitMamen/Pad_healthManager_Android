package com.bitvalue.health.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.api.responsebean.VisitBean;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 11/11
 */
public class VisitAdapter extends BaseQuickAdapter<VisitBean, BaseViewHolder> {
    private OnItemClickCallback itemClick;

    public VisitAdapter(int layoutResId, OnItemClickCallback onItemClickCallback,ArrayList<VisitBean> beanlist) {
        super(layoutResId,beanlist);
        this.itemClick = onItemClickCallback;
    }


    @Override
    protected void convert(BaseViewHolder holder, VisitBean item) {
        if (null == item) {
            return;
        }
        ImageView imageView = holder.getView(R.id.img_head_icon);
        switch (item.getVisitStatus()) {
            case "云门诊":
                imageView.setImageDrawable(Application.instance().getDrawable(R.drawable.home_icon_ymz));
                break;
            case "院内就诊":
                imageView.setImageDrawable(Application.instance().getDrawable(R.drawable.home_icon_yljz));
                break;

            case "联合门诊":
                imageView.setImageDrawable(Application.instance().getDrawable(R.drawable.home_icon_lhmz));
                break;

            case "远程门诊":
                imageView.setImageDrawable(Application.instance().getDrawable(R.drawable.home_icon_ychz));
                break;
        }

        holder.setText(R.id.tv_visit_status, item.getVisitStatus());
        holder.setText(R.id.tv_hospitolby,item.getHospitolName());
        holder.setText(R.id.tv_time,item.getStartTime());
        holder.setText(R.id.tv_time_by_end,item.getEndTime());


    }
}
