package com.bitvalue.health.ui.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bitvalue.health.api.responsebean.HealthPlanTaskListBean;
import com.bitvalue.health.api.responsebean.TaskInfoDTO;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.TypeConstants;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxq.pq.BeveLabelView;

import org.jetbrains.annotations.NotNull;

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





         helper.setText(R.id.tv_plan_title,item.getTask_describe())
                 .setText(R.id.tv_plan_time,item.getExec_time()!=null?TimeUtils.getTime(item.getExec_time()):"");

            //等待开启
            helper.setVisible(R.id.bl_tv_ddkq,item.getExec_flag() == 0);
        //进行中
        helper.setVisible(R.id.bl_tv_jxz,item.getExec_flag() == 1);
        //已完成
        helper.setVisible(R.id.bl_tv_ywc,item.getExec_flag() == 2);

        List<TaskInfoDTO> taskInfoDTOList = item.getFormartTaskInfo();



        for (TaskInfoDTO task:taskInfoDTOList) {
            if (TextUtils.isEmpty(task.getFormartPlanDescribe())){
                task.setFormartPlanDescribe("暂无");
            }
            Log.e(TAG, "计划类型: "+task.getPlanType()+"---------"+task.getFormartPlanDescribe() );
            switch (task.getPlanType()){
                case TypeConstants.Knowledge:
                    helper.getView(R.id.tv_knowledge).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_knowledge,"文章阅读："+task.getFormartPlanDescribe());
                    break;
                case TypeConstants.Quest:
                    helper.getView(R.id.tv_question).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_question,"问卷调查："+task.getFormartPlanDescribe());
                    break;
                case TypeConstants.Check:
                    helper.getView(R.id.tv_check).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_check,"检查提醒："+task.getFormartPlanDescribe());
                    break;
                case TypeConstants.Exam:
                    helper.getView(R.id.tv_exam).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_exam,"检验提醒："+task.getFormartPlanDescribe());
                    break;

            }
        }

    }

}
