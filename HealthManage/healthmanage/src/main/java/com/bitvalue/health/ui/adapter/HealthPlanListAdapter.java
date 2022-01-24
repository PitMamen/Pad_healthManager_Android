package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
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
    private List<NewLeaveBean.RowsDTO> sfjhBeanList = new ArrayList<>(); //随访计划 数据
    private PhoneFollowupCliclistener phoneFollowupCliclistener;
    private Context homeActivity;

    public HealthPlanListAdapter(Context context) {
        super(R.layout.item_sfjh_layout);
        homeActivity = context;
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
        WrapRecyclerView childItemLRecycleView = helper.getView(R.id.plan_list);
        childItemLRecycleView.setLayoutManager(new LinearLayoutManager(homeActivity));
        PlanItemChildAdapter childAdapter = new PlanItemChildAdapter(sfjhBean.getPlanInfo());
        childItemLRecycleView.setAdapter(childAdapter);
        childAdapter.setNewData(sfjhBean.getPlanInfo());
        childAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                NewLeaveBean.RowsDTO.PlanInfoDetailDTO planInfo = (NewLeaveBean.RowsDTO.PlanInfoDetailDTO) adapter.getItem(position);

//                List<NewLeaveBean.RowsDTO.PlanInfoDetailDTO> data = adapter.getData();
//                for (int i = 0; i < data.size(); i++) {
//                    if (position==i){
//                        data.get(i).setChecked(true);
//                    }else {
//                        data.get(i).setChecked(false);
//                    }
//                }
//
//                adapter.setNewData(data);

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
            }
        });

    }

    private OnPlanTaskItemClickListener onPlanTaskItemClickListener;

    public interface OnPlanTaskItemClickListener {

        void onSendMsgItemClick(String planId,NewLeaveBean.RowsDTO planInfoDetailDTO);
        void onCkeckPlanItemClick(String planId,NewLeaveBean.RowsDTO planInfoDetailDTO);
    }

    public void setOnPlanTaskItemClickListener(OnPlanTaskItemClickListener listener) {
        onPlanTaskItemClickListener = listener;
    }

}
