package com.bitvalue.healthmanage.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppAdapter;
import com.bitvalue.healthmanage.http.response.PlanBean;

public class HealthPlanDetailAdapter extends AppAdapter<PlanBean> {

    private PlanBean planBean;

    public HealthPlanDetailAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<PlanBean>.ViewHolder {

        private final TextView  tv_step_name;

        private ViewHolder() {
            super(R.layout.item_plan_detail);
            tv_step_name = findViewById(R.id.tv_step_name);
        }

        @Override
        public void onBindView(int position) {
            planBean = getItem(position);
            tv_step_name.setText(planBean.name);
        }
    }
}