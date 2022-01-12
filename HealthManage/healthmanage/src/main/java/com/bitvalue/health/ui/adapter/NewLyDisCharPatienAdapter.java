package com.bitvalue.health.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 11/29
 */
public class NewLyDisCharPatienAdapter extends BaseQuickAdapter<NewLeaveBean.RowsDTO, BaseViewHolder> {
    private List<NewLeaveBean.RowsDTO> newlyDisChaegedPaBeanList;


    public NewLyDisCharPatienAdapter(int layoutResId, @Nullable List<NewLeaveBean.RowsDTO> data) {
        super(layoutResId, data);
        this.newlyDisChaegedPaBeanList = data;
    }


    @Override
    protected void convert(BaseViewHolder holder, NewLeaveBean.RowsDTO item) {

        if (null == item) {
            return;
        }
        ImageView img_head = holder.getView(R.id.img_head);
        img_head.setImageDrawable(item.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
        holder.setText(R.id.tv_patient_name, item.getUserName());
        holder.setText(R.id.tv_patient_sex, item.getSex());
        String curen = TimeUtils.getCurrenTime();
        int finatime = Integer.valueOf(curen) - Integer.valueOf((item.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
        holder.setText(R.id.tv_patient_age, String.valueOf(finatime) + "岁");
        holder.setText(R.id.tv_dis_time, String.valueOf(item.getDiagDate()));
//        holder.setText(R.id.tv_isdistribution, EmptyUtil.isEmpty(item.getUserId()) ? "待分配" : "待分配");
        holder.setText(R.id.tv_isregister, EmptyUtil.isEmpty(item.getUserId()) ? "未注册" : "已注册");


    }
}
