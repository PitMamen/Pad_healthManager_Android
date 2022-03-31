package com.bitvalue.health.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.callback.OnItemClick;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author created by bitvalue
 * @data : 11/29
 */
public class NewLyDisCharPatienAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = NewLyDisCharPatienAdapter.class.getSimpleName();
    private List<NewLeaveBean.RowsDTO> PatientList;
    private OnItemClick itemClickCallback;


    public NewLyDisCharPatienAdapter(List<NewLeaveBean.RowsDTO> sfjhBeanList, OnItemClick callback) {
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
            PatientList.get(i).setShowCheck(true);
            PatientList.get(i).setChecked(false);
//            if (!EmptyUtil.isEmpty(PatientList.get(i).getUserId())) {
//                PatientList.get(i).setShowCheck(true);
//                PatientList.get(i).setChecked(false);
//            } else {
//                PatientList.get(i).setShowCheck(false);
//                PatientList.get(i).setChecked(false);
//            }
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
//            if (!EmptyUtil.isEmpty(PatientList.get(i).getUserId())) {
//                PatientList.get(i).setShowCheck(true);
//                PatientList.get(i).setChecked(true);
//            } else {
//                PatientList.get(i).setShowCheck(false);
//                PatientList.get(i).setChecked(false);
//            }
            PatientList.get(i).setShowCheck(true);
            PatientList.get(i).setChecked(true);
            Log.e(TAG, "AllChecked: "+  PatientList.get(i).isChecked());
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newly_dispatient_layout, parent, false);
        return new NewLyDisCharPatienAdapter.NewlyDisviewHodler(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (null != PatientList) {
            ((NewLyDisCharPatienAdapter.NewlyDisviewHodler) holder).bindData(PatientList.get(position));
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


    public class NewlyDisviewHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.img_head)
        ImageView im_head;
        @BindView(R.id.tv_patient_name)
        TextView tv_name;
        @BindView(R.id.tv_patient_sex)
        TextView tv_sex;
        @BindView(R.id.tv_patient_age)
        TextView tv_age;
        @BindView(R.id.ck_selct)
        CheckBox checkBox;
        @BindView(R.id.tv_isregister)
        TextView tv_isRegister;
        @BindView(R.id.tv_dis_time)
        TextView time;

        public NewlyDisviewHodler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(NewLeaveBean.RowsDTO bean) {
            tv_isRegister.setText(!EmptyUtil.isEmpty(bean.getUserId()) ? "已注册" : "未注册");
            tv_name.setText(bean.getUserName());
            tv_sex.setText(bean.getSex());
            String curen = TimeUtils.getCurrenTime();
            if(!EmptyUtil.isEmpty(bean.getAge())){
                int finatime = Integer.valueOf(curen) - Integer.valueOf((bean.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
                tv_age.setText(finatime+"岁");

            }
            checkBox.setVisibility(bean.isShowCheck() ? View.VISIBLE : View.GONE);
            checkBox.setChecked(bean.isChecked());
            if (!EmptyUtil.isEmpty(bean.getSex())){
                im_head.setImageDrawable(bean.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
            }

        }


    }

}
