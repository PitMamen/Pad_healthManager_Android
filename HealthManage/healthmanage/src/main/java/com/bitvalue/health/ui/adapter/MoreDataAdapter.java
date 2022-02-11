package com.bitvalue.health.ui.adapter;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.picture.imageEngine.impl.GlideEngine;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by bitvalue
 * @data : 02/11
 */
public class MoreDataAdapter extends BaseQuickAdapter<TaskDetailBean, BaseViewHolder> {
    public MoreDataAdapter(int layoutResId, @Nullable List<TaskDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskDetailBean item) {
        helper.setText(R.id.tv_data_time, TimeUtils.getTime(item.getVisitTime()));
        String type = item.getVisitType();
        switch (type){
            case "Exam":
                type = "检验单";
                break;
            case "Check":
                type = "检查单";
                break;
            case "DailyRecord":
                type = "日常记录";
                break;
        }
        helper.setText(R.id.tv_data_title,type);
        WrapRecyclerView childItemLRecycleView = helper.getView(R.id.item_image_datalist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Application.instance());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        childItemLRecycleView.setLayoutManager(layoutManager);
        List<String> imageUrllist = new ArrayList<>();
        for (int i = 0; i < item.getHealthImages().size(); i++) {
            imageUrllist.add(item.getHealthImages().get(i).previewFileUrl);
        }
        MoreDataDetailChildImageAdapter moreDataDetailChildImageAdapter = new MoreDataDetailChildImageAdapter(R.layout.item_moredata_child_layout,imageUrllist);
        childItemLRecycleView.setAdapter(moreDataDetailChildImageAdapter);
    }
}
