package com.bitvalue.healthmanage.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppAdapter;
import com.bitvalue.healthmanage.http.response.PlanDetailResult;
import com.bitvalue.healthmanage.util.TimeUtils;

public class HealthPlanDetailAdapter extends AppAdapter<PlanDetailResult.UserPlanDetailsDTO> {

    private PlanDetailResult.UserPlanDetailsDTO planDetailsDTO;

    public HealthPlanDetailAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<PlanDetailResult.UserPlanDetailsDTO>.ViewHolder {

        private final TextView tv_step_name, tv_step_desc, tv_step_time, tv_health_report;
        private final LinearLayout layout_health_btn;
        private final ImageView img_boll, img_btn;

        private ViewHolder() {
            super(R.layout.item_plan_detail);
            tv_step_name = findViewById(R.id.tv_step_name);
            tv_step_desc = findViewById(R.id.tv_step_desc);
            tv_health_report = findViewById(R.id.tv_health_report);
            tv_step_time = findViewById(R.id.tv_step_time);
            layout_health_btn = findViewById(R.id.layout_health_btn);
            img_boll = findViewById(R.id.img_boll);
            img_btn = findViewById(R.id.img_btn);
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindView(int position) {
            planDetailsDTO = getItem(position);
            if (planDetailsDTO.planType.equals("Quest")) {//planType   Quest   Remind  Knowledge   DrugGuide
                tv_step_name.setText("健康问卷");
                tv_health_report.setText("健康问卷");
            } else if (planDetailsDTO.planType.equals("Remind")) {
                tv_step_name.setText("健康提醒");
                tv_health_report.setText("健康提醒");
            } else if (planDetailsDTO.planType.equals("Knowledge")) {
                tv_step_name.setText("科普文章");
                tv_health_report.setText("科普文章");
            } else if (planDetailsDTO.planType.equals("Evaluate")) {
                tv_step_name.setText("健康评估");
                tv_health_report.setText("健康评估");
            } else if (planDetailsDTO.planType.equals("DrugGuide")) {
                tv_step_name.setText("用药提醒");
                tv_health_report.setText("用药提醒");
            } else if (planDetailsDTO.planType.equals("OutsideInformation")) {
                tv_step_name.setText("患者上传资料");
                tv_health_report.setText("患者上传资料");
            }
            tv_step_desc.setText(planDetailsDTO.planDescribe);
            tv_step_time.setText(planDetailsDTO.execTime);
            int dateCount = TimeUtils.getDateCount(System.currentTimeMillis(), TimeUtils.parseDate(planDetailsDTO.execTime, TimeUtils.YY_MM_DD_FORMAT_3).getTime());
            if (planDetailsDTO.execFlag == 1) {//已执行
                layout_health_btn.setBackground(getItemView().getContext().getResources().getDrawable(R.drawable.shape_bg_green));
                img_boll.setImageDrawable(getItemView().getContext().getResources().getDrawable(R.drawable.shape_boll_green));
                img_btn.setImageDrawable(getItemView().getContext().getResources().getDrawable(R.drawable.icon_lian_b));
                tv_health_report.setTextColor(getItemView().getContext().getResources().getColor(R.color.text_green));
            } else if (planDetailsDTO.execFlag == 0 && TimeUtils.getDateCount(System.currentTimeMillis()
                    , TimeUtils.parseDate(planDetailsDTO.execTime, TimeUtils.YY_MM_DD_FORMAT_3).getTime()) < 0) {//未执行已超期
                layout_health_btn.setBackground(getItemView().getContext().getResources().getDrawable(R.drawable.shape_bg_oran_small));
                img_boll.setImageDrawable(getItemView().getContext().getResources().getDrawable(R.drawable.shape_boll_orange));
                img_btn.setImageDrawable(getItemView().getContext().getResources().getDrawable(R.drawable.icon_lian_or));
                tv_health_report.setTextColor(getItemView().getContext().getResources().getColor(R.color.orange));
            } else {//未执行未超期；
                layout_health_btn.setBackground(getItemView().getContext().getResources().getDrawable(R.drawable.shape_bg_blue_small));
                img_boll.setImageDrawable(getItemView().getContext().getResources().getDrawable(R.drawable.shape_boll_blue));
                img_btn.setImageDrawable(getItemView().getContext().getResources().getDrawable(R.drawable.icon_lian_l));
                tv_health_report.setTextColor(getItemView().getContext().getResources().getColor(R.color.main_blue));
            }

//            //颜色  目前的项目
//            if (planDetailsDTO.isCurrent) {
//                layout_health_btn.setBackground(getItemView().getContext().getResources().getDrawable(R.drawable.shape_bg_green));
//                img_boll.setImageDrawable(getItemView().getContext().getResources().getDrawable(R.drawable.shape_boll_green));
//                img_btn.setImageDrawable(getItemView().getContext().getResources().getDrawable(R.drawable.icon_lian_b));
//                tv_health_report.setTextColor(getItemView().getContext().getResources().getColor(R.color.text_green));
//            } else {
//                layout_health_btn.setBackground(getItemView().getContext().getResources().getDrawable(R.drawable.shape_bg_blue_small));
//                img_boll.setImageDrawable(getItemView().getContext().getResources().getDrawable(R.drawable.shape_boll_blue));
//                img_btn.setImageDrawable(getItemView().getContext().getResources().getDrawable(R.drawable.icon_lian_l));
//                tv_health_report.setTextColor(getItemView().getContext().getResources().getColor(R.color.main_blue));
//            }
        }
    }
}
