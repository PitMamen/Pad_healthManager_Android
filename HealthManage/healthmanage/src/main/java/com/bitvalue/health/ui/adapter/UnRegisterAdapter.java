package com.bitvalue.health.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.callback.OnItemClick;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author created by bitvalue
 * @data : 01/18
 */
public class UnRegisterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final String TAG = NewLyDisCharPatienAdapter.class.getSimpleName();
    private List<NewLeaveBean.RowsDTO> PatientList;
    private OnItemClick itemClickCallback;
    private OnItemClickCallback onItemClickCallback;


    public UnRegisterAdapter(List<NewLeaveBean.RowsDTO> sfjhBeanList, OnItemClick callback, OnItemClickCallback onItemClickCallback) {
        this.PatientList = sfjhBeanList;
        this.itemClickCallback = callback;
        this.onItemClickCallback = onItemClickCallback;
    }

    public void updateList(List<NewLeaveBean.RowsDTO> list) {
        if (PatientList == null) {
            PatientList = new ArrayList<>();
        } else {
            PatientList = list;
        }
        notifyDataSetChanged();
    }


    public void showCheckBox() {
        for (int i = 0; i < PatientList.size(); i++) {
            PatientList.get(i).setShowCheck(true);
            PatientList.get(i).setChecked(false);
            if (!EmptyUtil.isEmpty(PatientList.get(i).getUserId())) {
                PatientList.get(i).setShowCheck(true);
                PatientList.get(i).setChecked(false);
            } else {
                PatientList.get(i).setShowCheck(false);
                PatientList.get(i).setChecked(false);
            }
        }
        notifyDataSetChanged();
    }


    public void showCheckBox1(boolean isChecked) {
        for (int i = 0; i < PatientList.size(); i++) {
            if (!EmptyUtil.isEmpty(PatientList.get(i).getUserId())) {
                PatientList.get(i).setShowCheck(isChecked);
                PatientList.get(i).setChecked(isChecked);
            } else {
                PatientList.get(i).setShowCheck(true);
                PatientList.get(i).setChecked(false);
            }

        }
        notifyDataSetChanged();
    }


    public void AllChecked() {
        // TODO: 2021/12/7  没有userid 群发不可选 需要过滤
        for (int i = 0; i < PatientList.size(); i++) {
            if (!EmptyUtil.isEmpty(PatientList.get(i).getUserId())) {
                PatientList.get(i).setShowCheck(true);
                PatientList.get(i).setChecked(true);
            } else {
                PatientList.get(i).setShowCheck(false);
                PatientList.get(i).setChecked(false);
            }
            PatientList.get(i).setShowCheck(true);
            PatientList.get(i).setChecked(true);
            Log.e(TAG, "AllChecked: "+  PatientList.get(i).isChecked());
        }
        notifyDataSetChanged();
    }


    public void unAllCheck() {
        for (int i = 0; i < PatientList.size(); i++) {
            if (!EmptyUtil.isEmpty(PatientList.get(i).getUserId())) {
                PatientList.get(i).setShowCheck(true);
                PatientList.get(i).setChecked(false);
            } else {
                PatientList.get(i).setShowCheck(true);
                PatientList.get(i).setChecked(false);
            }
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unregister_layout, parent, false);
        return new UnRegisterAdapter.PatientViewHodler(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (null != PatientList) {
            ((UnRegisterAdapter.PatientViewHodler) holder).bindData(PatientList.get(position));

            ((PatientViewHodler) holder).im_head.setOnClickListener(v -> {
               if (null!=onItemClickCallback){
                   onItemClickCallback.onItemClick(PatientList.get(position));
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return null != PatientList && PatientList.size() > 0 ? PatientList.size() : 0;
    }


    public class PatientViewHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.img_head)
        ImageView im_head;
        @BindView(R.id.tv_patient_name)
        TextView tv_name;
        @BindView(R.id.tv_patient_sex)
        TextView tv_sex;
        @BindView(R.id.tv_patient_age)
        TextView tv_age;
        @BindView(R.id.tv_dis_time)
        TextView time;
        @BindView(R.id.tv_department)
        TextView tv_department;  //科室
        @BindView(R.id.tv_zhuanb)
        TextView tv_zhuanbing;  //专病
        @BindView(R.id.tv_bingqu)
        TextView tv_bingqu; //病区
        @BindView(R.id.tv_diagnosis)
        TextView tv_disa;//诊断


        @BindView(R.id.rl_pay_patient)
        RelativeLayout pay_patient; //付费患者


        public PatientViewHodler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(NewLeaveBean.RowsDTO bean) {
            tv_name.setText(bean.getUserName());
            tv_sex.setText(bean.getSex());
            tv_disa.setText(bean.getCyzd());
            tv_department.setText(bean.getKsmc());
            tv_zhuanbing.setText(bean.getBqmc());
            tv_bingqu.setText(bean.getBqmc());
            time.setText(bean.getCysj());
            String curen = TimeUtils.getCurrenTime();
            if (!EmptyUtil.isEmpty(bean.getAge())){
                int finatime = Integer.valueOf(curen) - Integer.valueOf((bean.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
                tv_age.setText(finatime+"岁");
            }
            if (!EmptyUtil.isEmpty(bean.getSex())){
                im_head.setImageDrawable(bean.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
            }

        }
    }
}
