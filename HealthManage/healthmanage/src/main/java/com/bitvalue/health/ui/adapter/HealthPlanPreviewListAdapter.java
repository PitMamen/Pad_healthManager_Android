package com.bitvalue.health.ui.adapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.TaskInfoDTO;
import com.bitvalue.health.callback.PhoneFollowupCliclistener;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.TypeConstants;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxq.pq.BeveLabelView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HealthPlanPreviewListAdapter extends BaseQuickAdapter<HealthPlanTaskListBean, BaseViewHolder> {

    public static final String TAG = HealthPlanPreviewListAdapter.class.getSimpleName();


    public HealthPlanPreviewListAdapter() {
        super(R.layout.item_plan_preview_list);
    }



    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull HealthPlanTaskListBean item) {

        String time = TimeUtils.formatTime(item.getExec_time() + "");

        helper.setText(R.id.tv_plan_title,item.getTask_describe())
                 .setText(R.id.tv_plan_time,TimeUtils.formatTime(item.getExec_time()+""));


        BeveLabelView beveLabelView=   helper.getView(R.id.bl_tv);

        List<TaskInfoDTO> taskInfoDTOList = item.getTaskInfo();

        for (TaskInfoDTO task:taskInfoDTOList) {
            switch (task.getPlanType()){
                case TypeConstants.Knowledge:

                    helper.setText(R.id.tv_knowledge,"文章阅读："+task.getPlanDescribe());
                    break;
                case TypeConstants.Quest:
                helper.setText(R.id.tv_question,"问卷调查："+task.getPlanDescribe());
                    break;
                case TypeConstants.Check:
                    helper.setText(R.id.tv_check,"检查提醒："+task.getPlanDescribe());
                    break;
                case TypeConstants.Exam:
                    helper.setText(R.id.tv_exam,"检验提醒："+task.getPlanDescribe());
                    break;
                case TypeConstants.Remind:
                    helper.setText(R.id.tv_remind,"健康提醒："+task.getPlanDescribe());
                    break;
            }
        }

    }

}
