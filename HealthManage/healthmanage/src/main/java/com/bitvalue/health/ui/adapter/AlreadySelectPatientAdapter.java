package com.bitvalue.health.ui.adapter;

import android.view.View;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 01/18
 * 计划列表界面 已选中的患者adapter
 */
public class AlreadySelectPatientAdapter extends BaseQuickAdapter<NewLeaveBean.RowsDTO, BaseViewHolder> {
    private OnDeleteCallBack callBack;

    public AlreadySelectPatientAdapter(int layoutResId, @Nullable List<NewLeaveBean.RowsDTO> data) {
        super(layoutResId, data);
    }

    public void setOnItemDeleteClickLisenler(OnDeleteCallBack callBack){
        this.callBack = callBack;
    }


    @Override
    protected void convert(BaseViewHolder helper, NewLeaveBean.RowsDTO item) {
         if (item!=null){
                helper.getView(R.id.tv_delete).setOnClickListener(v -> {
                    if (callBack!=null){
                         callBack.onItemDelete(item);
                    }
                });
             helper.setText(R.id.tv_pat_name,item.getUserName());
         }
    }


    public interface OnDeleteCallBack{
        void onItemDelete(NewLeaveBean.RowsDTO item);
    }

}
