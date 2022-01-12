package com.bitvalue.health.ui.adapter;

import static com.bitvalue.health.util.DataUtil.isNumeric;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.callback.PhoneFollowupCliclistener;
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
public class SFJH_HZZX_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = SFJH_HZZX_Adapter.class.getSimpleName();
    private static Context mcontext;
    private List<NewLeaveBean.RowsDTO> sfjhBeanList = new ArrayList<>(); //随访计划 数据
    private PhoneFollowupCliclistener phoneFollowupCliclistener;

    public SFJH_HZZX_Adapter(List<NewLeaveBean.RowsDTO> sfjhBeanList, Context context) {
        this.sfjhBeanList = sfjhBeanList;
        this.mcontext = context;
    }


    public void setOnItemPhoneNumCallback(PhoneFollowupCliclistener callback) {
        this.phoneFollowupCliclistener = callback;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_sfjh_layout , parent, false);
        return  new SFJHViewHodler(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (sfjhBeanList != null) {
                ((SFJHViewHodler) holder).bingData(sfjhBeanList.get(position));
                ((SFJHViewHodler) holder).tv_phone.setOnClickListener(v -> {
                    if (null != phoneFollowupCliclistener) {
                        phoneFollowupCliclistener.phoneNumberCallback(sfjhBeanList.get(position).getInfoDetail().getDhhm());
                    }
                });
        }


    }

    @Override
    public int getItemCount() {
        return sfjhBeanList.size() > 0 ? sfjhBeanList.size() : 0;
    }


    public void updateSFJHList(List<NewLeaveBean.RowsDTO> sfjhBeanList) {
        this.sfjhBeanList = sfjhBeanList;
        notifyDataSetChanged();
    }






    public static class SFJHViewHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_phone_visitor)
        TextView tv_phone;
        @BindView(R.id.img_head)
        ImageView iv_head;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_age)
        TextView tv_age;
        @BindView(R.id.tv_sex)
        TextView tv_sex;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_type_one)
        TextView tv_type_one;
        @BindView(R.id.tv_type_two)
        TextView tv_type_two;
        @BindView(R.id.tv_type_three)
        TextView tv_type_three;
        @BindView(R.id.tv_type_status)
        TextView tv_type_status;
        @BindView(R.id.tv_sf_type)
        TextView tv_PackageType;  //套餐类型


        public SFJHViewHodler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bingData(NewLeaveBean.RowsDTO sfjhBean) {
            tv_name.setText(sfjhBean.getUserName());
            String curen = TimeUtils.getCurrenTime();
            int finatime = Integer.valueOf(curen) - Integer.valueOf((sfjhBean.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
            tv_age.setText(finatime + "岁");
            tv_sex.setText(sfjhBean.getSex());
            tv_time.setText(sfjhBean.getDiagDate());
            tv_PackageType.setText(sfjhBean.getDiagnosis());
            tv_phone.setVisibility(isNumeric(sfjhBean.getInfoDetail().getDhhm()) && !EmptyUtil.isEmpty(sfjhBean.getInfoDetail().getDhhm()) ? View.VISIBLE : View.GONE);
            iv_head.setImageDrawable(sfjhBean.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
//            tv_type_two.setText(sfjhBean.type2);
//            tv_type_three.setText(sfjhBean.type3);


            //就诊状态（1：待就诊 2：就诊中 3：已完成）
            switch (sfjhBean.getInfoDetail().getHzlx()) {
                case "1":
                    tv_type_status.setText("进行中");
                    tv_type_status.setTextColor(mcontext.getResources().getColor(R.color.main_blue));
                    tv_type_status.setBackground(mcontext.getResources().getDrawable(R.drawable.shape_bg_blue_small));
                    break;
                case "2":
                    tv_type_status.setText("待接收");
                    tv_type_status.setTextColor(mcontext.getResources().getColor(R.color.orange_text));
                    tv_type_status.setBackground(mcontext.getResources().getDrawable(R.drawable.shape_orange_small));
                    break;
                case "3":
                    tv_type_status.setText("医生拒绝");
                    tv_type_status.setTextColor(mcontext.getResources().getColor(R.color.orange_deep_text));
                    tv_type_status.setBackground(mcontext.getResources().getDrawable(R.drawable.shape_orange_small));
                    break;
            }
        }

    }




}
