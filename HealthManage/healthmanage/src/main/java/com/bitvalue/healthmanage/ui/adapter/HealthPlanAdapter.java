package com.bitvalue.healthmanage.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppAdapter;
import com.bitvalue.healthmanage.http.response.PlanBean;
import com.bitvalue.healthmanage.http.response.PlanListBean;

import org.jetbrains.annotations.NotNull;

public class HealthPlanAdapter extends AppAdapter<PlanListBean> {

    private PlanListBean planBean;

    public HealthPlanAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<PlanListBean>.ViewHolder {

        private final TextView tv_use_status, tv_name, tv_use_no;

        private ViewHolder() {
            super(R.layout.item_plan);
            tv_use_status = findViewById(R.id.tv_use_status);
            tv_name = findViewById(R.id.tv_name);
            tv_use_no = findViewById(R.id.tv_use_no);
        }

        @Override
        public void onBindView(int position) {
            planBean = getItem(position);
            tv_use_status.setText(planBean.status.equals("1") ? "启用" : "停用");
            tv_name.setText(planBean.templateName);
            tv_use_no.setText(planBean.userNum + "人使用中");
        }
    }
}
