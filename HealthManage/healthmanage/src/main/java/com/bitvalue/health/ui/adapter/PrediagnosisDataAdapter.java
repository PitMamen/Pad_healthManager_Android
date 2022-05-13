package com.bitvalue.health.ui.adapter;

import android.util.Log;

import androidx.annotation.Nullable;

import com.bitvalue.health.api.responsebean.AnswerResultBean;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author created by bitvalue
 * @data : 05/13
 * 预诊资料 adapter
 */
public class PrediagnosisDataAdapter extends BaseQuickAdapter<AnswerResultBean.RecordsDTO, BaseViewHolder> {
    public PrediagnosisDataAdapter(int layoutResId, @Nullable List<AnswerResultBean.RecordsDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AnswerResultBean.RecordsDTO item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tv_time, item.getCreateTime().length() > 10 ? item.getCreateTime().substring(0, 10) : item.getCreateTime());
        int position = helper.getPosition();
        if (item.getName().contains("-")) {
            String[] name = item.getName().split("-");
            helper.setText(R.id.tv_right_name, position == 0 ? name[1] + "(最新)" : name[1]);
        } else {
            helper.setText(R.id.tv_right_name, position == 0 ? item.getName() + "(最新)" : item.getName());
        }

    }
}
