package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.api.requestbean.GetLogsApi;
import com.bitvalue.health.base.AppAdapter;
import com.bitvalue.healthmanage.R;

public class HealthLogsAdapter extends AppAdapter<GetLogsApi.LogBean> {

    private GetLogsApi.LogBean logBean;

    public HealthLogsAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<GetLogsApi.LogBean>.ViewHolder {

        private final TextView tv_time, tv_type, tv_result, tv_hospital, tv_office, tv_doctor;

        private ViewHolder() {
            super(R.layout.item_health_log);
            tv_time = findViewById(R.id.tv_time);
            tv_type = findViewById(R.id.tv_type);
            tv_result = findViewById(R.id.tv_result);
            tv_hospital = findViewById(R.id.tv_hospital);
            tv_office = findViewById(R.id.tv_office);
            tv_doctor = findViewById(R.id.tv_doctor);
        }

        @Override
        public void onBindView(int position) {
            logBean = getItem(position);
            tv_time.setText(logBean.happenedTime.substring(0, 10));
            tv_type.setText(logBean.recordType.equals("menzhen") ? "门诊" : "住院");
            tv_type.setTextColor(logBean.recordType.equals("menzhen") ?
                    getContext().getResources().getColor(R.color.main_blue) : getContext().getResources().getColor(R.color.green));
            tv_result.setText(logBean.departmentDiagnosis);
            tv_hospital.setText(logBean.hospitalName);
            tv_office.setText(logBean.hospitalDepartment);
            tv_doctor.setText(logBean.doctorName);
        }
    }
}
