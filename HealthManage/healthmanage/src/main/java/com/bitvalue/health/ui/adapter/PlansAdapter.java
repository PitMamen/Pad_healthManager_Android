package com.bitvalue.health.ui.adapter;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.PlanListBean;
import com.bitvalue.health.callback.OnItemClick;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 11/29
 */
public class PlansAdapter extends BaseQuickAdapter<PlanListBean, BaseViewHolder> {


    private CheckBox checkBox;
    private int mSelectedPos = 0;   //实现单选，保存当前选中的position
    private List<PlanListBean> soureceList = new ArrayList<>();
    private OnItemClick onItemClick;

    public PlansAdapter(int layoutResId, @Nullable List<PlanListBean> data) {
        super(layoutResId, data);

    }

    public void updateList(List<PlanListBean> data){
        soureceList = data;
        for (int i = 0; i < soureceList.size(); i++) {
            if (soureceList.get(i).isChecked) {
                mSelectedPos = i;
            }
        }
    }




    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    @Override
    protected void convert(BaseViewHolder holder, PlanListBean item) {

        if (null == item) {
            return;
        }
        int position = holder.getAdapterPosition();
        checkBox = holder.getView(R.id.ck_selct);
        checkBox.setChecked(item.isChecked);
        checkBox.setClickable(false);
        holder.itemView.setOnClickListener(v -> {
            soureceList.get(mSelectedPos).isChecked = false;
            mSelectedPos  = position;
            soureceList.get(mSelectedPos).isChecked = true;
            notifyDataSetChanged();
            if (null!=onItemClick){
                onItemClick.onItemClick(item,true);
            }
        });
        holder.setText(R.id.tv_plan_name, item.templateName);
        holder.setText(R.id.tv_bone_number_des, item.templateName);
        holder.setText(R.id.tv_bone_number, item.userNum + "");
    }
}
