package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.callback.PhoneFollowupCliclistener;
import com.bitvalue.health.ui.fragment.workbench.HealthPlanTaskDetailFragment;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * @author created by bitvalue
 * @data :
 * 健康随访列表
 */
public class HealthPlanListAdapter extends BaseQuickAdapter<NewLeaveBean.RowsDTO, BaseViewHolder> {

    public static final String TAG = HealthPlanListAdapter.class.getSimpleName();
    private Context homeActivity;
    private OnItemClickCallback onItemClickCallback;

    public HealthPlanListAdapter(Context context,OnItemClickCallback callback) {
        super(R.layout.item_sfjh_layout);
        homeActivity = context;
        this.onItemClickCallback = callback;
    }


    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull NewLeaveBean.RowsDTO sfjhBean) {
        String curen = TimeUtils.getCurrenTime();
        int finatime = Integer.valueOf(curen) - Integer.valueOf((sfjhBean.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
        helper.setText(R.id.tv_name, sfjhBean.getUserName())
                .setText(R.id.tv_age, finatime + "岁")
                .setText(R.id.tv_sex, sfjhBean.getSex())
                .setText(R.id.tv_time, sfjhBean.getDiagDate())
                .setImageDrawable(R.id.img_head, sfjhBean.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));

        helper.setOnClickListener(R.id.img_head, v -> {
            if (onItemClickCallback!=null){
                onItemClickCallback.onItemClick(sfjhBean);
            }
        });


        WrapRecyclerView childItemLRecycleView = helper.getView(R.id.plan_list);
        childItemLRecycleView.setLayoutManager(new LinearLayoutManager(homeActivity));
        PlanItemChildAdapter childAdapter = new PlanItemChildAdapter(sfjhBean.getPlanInfo());
        childItemLRecycleView.setAdapter(childAdapter);
        childAdapter.setNewData(sfjhBean.getPlanInfo());
        childAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            NewLeaveBean.RowsDTO.PlanInfoDetailDTO planInfo = (NewLeaveBean.RowsDTO.PlanInfoDetailDTO) adapter.getItem(position);

            switch (view.getId()){
                case R.id.iv_send_msg:
                    if (onPlanTaskItemClickListener!=null){
                        onPlanTaskItemClickListener.onSendMsgItemClick(planInfo.getPlanId(),  sfjhBean);
                    }

                    break;
                case R.id.iv_check_plan:
                    if (onPlanTaskItemClickListener!=null){
                        onPlanTaskItemClickListener.onCkeckPlanItemClick(planInfo.getPlanId(), sfjhBean);
                    }

                    break;


            }
        });

    }

    private OnPlanTaskItemClickListener onPlanTaskItemClickListener;

    public interface OnPlanTaskItemClickListener {

        void onSendMsgItemClick(String planId,NewLeaveBean.RowsDTO planInfoDetailDTO);
        void onCkeckPlanItemClick(String planId,NewLeaveBean.RowsDTO planInfoDetailDTO);
//        void onHeadItemClick(NewLeaveBean.RowsDTO rowsDTO);
    }

    public void setOnPlanTaskItemClickListener(OnPlanTaskItemClickListener listener) {
        onPlanTaskItemClickListener = listener;
    }

}
