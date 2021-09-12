package com.bitvalue.healthmanage.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppAdapter;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.PatientResultBean;
import com.bitvalue.healthmanage.util.TimeUtils;

public class SearchPatientAdapter extends AppAdapter<PatientResultBean> {

    private PatientResultBean patientResultBean;

    public SearchPatientAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<PatientResultBean>.ViewHolder {

        private final TextView tv_name, tv_describe, tv_sex_age, tv_disease, tv_chat_type, tv_time;

        private ViewHolder() {
            super(R.layout.item_patient_log);
            tv_name = findViewById(R.id.tv_name);
            tv_describe = findViewById(R.id.tv_describe);
            tv_sex_age = findViewById(R.id.tv_describe);
            tv_disease = findViewById(R.id.tv_describe);
            tv_chat_type = findViewById(R.id.tv_describe);
            tv_time = findViewById(R.id.tv_describe);
        }

        @Override
        public void onBindView(int position) {
            patientResultBean = getItem(position);
            tv_name.setText(patientResultBean.userInfo.userName);
            tv_describe.setText(patientResultBean.suggestion);
            tv_sex_age.setText(patientResultBean.userInfo.userSex + "（" + patientResultBean.userInfo.userAge + "岁）/");
            tv_disease.setText(patientResultBean.diagnosis);
            tv_chat_type.setText(patientResultBean.typeName);
            tv_time.setText(TimeUtils.getTime(patientResultBean.beginTime, TimeUtils.YY_MM_DD_FORMAT_3));
        }
    }
}
