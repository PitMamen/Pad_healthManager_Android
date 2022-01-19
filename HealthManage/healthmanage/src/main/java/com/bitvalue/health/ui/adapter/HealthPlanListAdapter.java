package com.bitvalue.health.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.callback.PhoneFollowupCliclistener;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * @author created by bitvalue
 * @data :
 *  健康随访列表
 */
public class HealthPlanListAdapter extends BaseQuickAdapter<NewLeaveBean.RowsDTO, BaseViewHolder> {

    public static final String TAG = HealthPlanListAdapter.class.getSimpleName();
    private List<NewLeaveBean.RowsDTO> sfjhBeanList = new ArrayList<>(); //随访计划 数据
    private PhoneFollowupCliclistener phoneFollowupCliclistener;

    public HealthPlanListAdapter() {
        super(R.layout.item_sfjh_layout);
    }



    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NotNull BaseViewHolder helper, @NotNull NewLeaveBean.RowsDTO sfjhBean) {
        String curen = TimeUtils.getCurrenTime();
        int finatime = Integer.valueOf(curen) - Integer.valueOf((sfjhBean.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
        helper.setText(R.id.tv_name, sfjhBean.getUserName())
                .setText(R.id.tv_age,finatime + "岁")
                .setText(R.id.tv_sex,sfjhBean.getSex())
                .setText(R.id.tv_time,sfjhBean.getDiagDate())
                .setImageDrawable(R.id.img_head,sfjhBean.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));

    }








}
