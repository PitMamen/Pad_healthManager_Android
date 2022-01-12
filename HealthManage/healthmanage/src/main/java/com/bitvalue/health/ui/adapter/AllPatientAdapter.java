package com.bitvalue.health.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.callback.OnItemClick;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author created by bitvalue
 * @data :
 */
public class AllPatientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = AllPatientAdapter.class.getSimpleName();
    private List<NewLeaveBean.RowsDTO> PatientList;
    private OnItemClick itemClickCallback;


    public AllPatientAdapter(List<NewLeaveBean.RowsDTO> sfjhBeanList, OnItemClick callback) {
        this.PatientList = sfjhBeanList;
        this.itemClickCallback = callback;
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
        }
        notifyDataSetChanged();
    }


    public void unAllCheckShowcheck(boolean isShowCheck) {
        for (int i = 0; i < PatientList.size(); i++) {
            if (!EmptyUtil.isEmpty(PatientList.get(i).getUserId())) {
                PatientList.get(i).setShowCheck(isShowCheck);
                PatientList.get(i).setChecked(false);
            } else {
                PatientList.get(i).setShowCheck(false);
                PatientList.get(i).setChecked(false);
            }
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_patient, parent, false);
        return new SFJFViewHodler(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (null != PatientList) {
            ((SFJFViewHodler) holder).bindData(PatientList.get(position));
            holder.itemView.setOnClickListener(v -> {
                if (null != itemClickCallback) {
                    if (!EmptyUtil.isEmpty(PatientList.get(position))) {
                        if (PatientList.get(position).isChecked) {
                            PatientList.get(position).isChecked = false;
                        } else {
                            PatientList.get(position).isChecked = true;
                        }
                    }
                    notifyItemChanged(position);
                    itemClickCallback.onItemClick(PatientList.get(position), PatientList.get(position).isChecked);

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return null != PatientList && PatientList.size() > 0 ? PatientList.size() : 0;
    }


    public class SFJFViewHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.img_head)
        ImageView im_head;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_patient_sex)
        TextView tv_sex;
        @BindView(R.id.tv_patient_age)
        TextView tv_age;
        @BindView(R.id.tv_type_one_pa)
        TextView tv_type_one_pa;
        @BindView(R.id.tv_type_two_pa)
        TextView tv_type_two_pa;
        @BindView(R.id.tv_type_three_pa)
        TextView tv_type_three_pa;
        @BindView(R.id.img_boll)
        ImageView im_boll;
//        @BindView(R.id.ck_selct)
//        CheckBox checkBox;
        @BindView(R.id.tv_chat_type)
        TextView tv_isRegister;

        public SFJFViewHodler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(NewLeaveBean.RowsDTO bean) {
            tv_isRegister.setText(!EmptyUtil.isEmpty(bean.getUserId()) ? "已注册" : "未注册");
            tv_name.setText(bean.getUserName());
            tv_sex.setText(bean.getSex());
            String curen = TimeUtils.getCurrenTime();
            int finatime = Integer.valueOf(curen) - Integer.valueOf((bean.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
            tv_age.setText(String.valueOf(finatime)+"岁");
            tv_type_one_pa.setText(bean.getDiagnosis());
//            tv_type_two_pa.setText(bean.getGoodsName());
//            tv_type_three_pa.setText(bean.getGoodsName());
            Log.e(TAG, "userName: " + bean.getUserName() + "  useriD:" + bean.getUserId() + " isShow?" + bean.isShowCheck());
            im_boll.setVisibility(View.GONE);
//            checkBox.setVisibility(bean.isShowCheck() && !EmptyUtil.isEmpty(bean.getUserId()) ? View.VISIBLE : View.GONE);
//            checkBox.setChecked(bean.isChecked);
            im_head.setImageDrawable(bean.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));

        }


    }


}
