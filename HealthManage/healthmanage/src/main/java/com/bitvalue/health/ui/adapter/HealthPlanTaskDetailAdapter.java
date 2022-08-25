package com.bitvalue.health.ui.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.QueryPlanDetailApi;
import com.bitvalue.health.api.responsebean.PlanTaskDetail;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.TypeConstants;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.toast.ToastUtils;

import org.jetbrains.annotations.NotNull;

public class HealthPlanTaskDetailAdapter extends BaseQuickAdapter<PlanTaskDetail.UserPlanDetailsDTO, BaseViewHolder> {

    public static final String TAG = HealthPlanTaskDetailAdapter.class.getSimpleName();


    public HealthPlanTaskDetailAdapter() {
        super(R.layout.item_plan_task_detail);
    }



    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull PlanTaskDetail.UserPlanDetailsDTO task) {
        TextView tv_state = helper.getView(R.id.tv_state);
        if (task.getExecFlag() == 1){
            helper.setText(R.id.tv_state,"已完成");

            tv_state.setTextColor(Application.instance().getResources().getColor(R.color.gray_999999));
        }else {
            helper.setText(R.id.tv_state,"待完成");

            tv_state.setTextColor(Application.instance().getResources().getColor(R.color.main_blue));
        }

        if (TextUtils.isEmpty(task.getPlanDescribe())){
            task.setPlanDescribe("暂无");
        }
            switch (task.getPlanType()){
                case TypeConstants.Knowledge:

                    helper.setText(R.id.tv_title,"文章阅读："+task.getPlanDescribe());
                    break;
                case TypeConstants.Quest:
                    helper.setText(R.id.tv_title,"问卷调查："+task.getPlanDescribe());
                    break;
                case TypeConstants.Check:
                    helper.setText(R.id.tv_title,"检查提醒："+task.getPlanDescribe());
                    break;
                case TypeConstants.Exam:
                    helper.setText(R.id.tv_title,"检验提醒："+task.getPlanDescribe());
                    break;
                case TypeConstants.Remind:
                    helper.setText(R.id.tv_title,"健康提醒："+task.getPlanDescribe());
                    break;
                case TypeConstants.Evaluate:
                    helper.setText(R.id.tv_title,"健康评估："+task.getPlanDescribe());
                    break;
            }


        //点击 查看计划详情内容 问卷 问诊 检查 检验 图片等
        helper.getView(R.id.rr_layout_title).setOnClickListener(v -> {
            QueryPlanDetailApi questbean = new QueryPlanDetailApi();
            questbean.contentId = task.contentId;
            questbean.planType = task.planType;;
            questbean.userId = task.contentInfo.userId;
            switch (task.getPlanType()){
                case TypeConstants.Quest:
                    if (task.execFlag==0){
                        ToastUtils.show("该计划未完成,不可查看");
                        return;
                    }
                    Application.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_QUESTION_DETAIL,questbean);
                    break;
                case TypeConstants.Knowledge:
                    Application.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL,questbean);
                    break;
                case TypeConstants.Exam:
                case TypeConstants.Check:
                    Application.instance().getHomeActivity().switchSecondFragment(Constants.FRAGMENT_MORE_DATA,questbean);
                    break;
            }
//            Log.e(TAG, "随访任务点击了: "+ task.getPlanType()+" contentID: "+task.contentId+"  planType: "+task.planType+" userID: "+task.getContentInfo().userId);
        });

//            if (task.getContentInfo()!=null && task.getContentInfo().getHealthImages()!=null && task.getContentInfo().getHealthImages().size()>0){
//
//                helper.setVisible(R.id.recyclerView,true);
//                RecyclerView recyclerView=helper.getView(R.id.recyclerView);
//                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
//                recyclerView.setLayoutManager(gridLayoutManager);
//                HealthTaskImageAdapter imageAdapter=new HealthTaskImageAdapter(task.getContentInfo().getHealthImages());
//                imageAdapter.setOnItemClickListener(new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//                        final List<String> imageList = new ArrayList<>();
//                        for (HealthImagesDTO image : task.getContentInfo().getHealthImages()) {
//
//                            imageList.add(image.getFileUrl());
//                        }
//                        ImagePreview
//                                .getInstance()
//                                // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好；
//                                .setContext(mContext)
//                                .setIndex(position)
//                                .setImageList( imageList)
//                                // 开启预览
//                                .start();
//                    }
//                });
//                recyclerView.setAdapter(imageAdapter);//设置数据
//            }else {
//                helper.setVisible(R.id.recyclerView,false);
//            }

    }




}
