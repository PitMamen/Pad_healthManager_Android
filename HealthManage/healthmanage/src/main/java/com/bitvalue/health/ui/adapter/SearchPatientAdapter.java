package com.bitvalue.health.ui.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.PatientResultBean;
import com.bitvalue.health.base.AppAdapter;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchPatientAdapter extends AppAdapter<PatientResultBean> {

    private PatientResultBean patientResultBean;
    private Context mContext;

    public SearchPatientAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_patient_log, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends AppAdapter<PatientResultBean>.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_describe)
        TextView tv_describe;
        @BindView(R.id.tv_sex_age)
        TextView tv_sex_age;
        @BindView(R.id.tv_disease)
        TextView tv_disease;
        @BindView(R.id.tv_chat_type)
        TextView tv_chat_type;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.img_head)
        ImageView img_head;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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

            if (null != patientResultBean.headUrl && !patientResultBean.headUrl.isEmpty()) {
                Glide.with(img_head)
//                        .load("http://img.duoziwang.com/2021/03/1623076080632524.jpg")
                        .load(patientResultBean.headUrl)
                        .transform(new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                20, Application.instance().getResources().getDisplayMetrics())))
                        .into(img_head);
            } else {
                img_head.setImageDrawable(patientResultBean.userInfo.userSex.equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
            }
        }
    }
}
