package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.util.Log;
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
            tv_time.setText(logBean.getHappenedTime().substring(0, 10));  //就诊时间
            if (logBean.getRecordType()!=null){
                tv_type.setText(logBean.getRecordType().equals("menzhen") ? "门诊" : "住院");
                tv_type.setTextColor(logBean.getRecordType().equals("menzhen") ?getContext().getResources().getColor(R.color.main_blue) : getContext().getResources().getColor(R.color.green));//类型
            }
            tv_result.setText(logBean.getDepartmentDiagnosis());  //诊断
            tv_hospital.setText(logBean.getHospitalName()); //医院
            tv_office.setText(logBean.getHospitalDepartment()); //科室
            tv_doctor.setText(logBean.getDoctorName()); //医生
        }
    }
}
