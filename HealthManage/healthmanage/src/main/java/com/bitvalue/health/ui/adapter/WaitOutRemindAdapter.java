//package com.bitvalue.health.ui.adapter;
//
//import static com.bitvalue.health.util.DataUtil.isNumeric;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bitvalue.health.Application;
//import com.bitvalue.health.api.responsebean.NewLeaveBean;
//import com.bitvalue.health.callback.PhoneFollowupCliclistener;
//import com.bitvalue.health.util.EmptyUtil;
//import com.bitvalue.health.util.TimeUtils;
//import com.bitvalue.healthmanage.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * @author created by bitvalue
// * @data : 01/10
// */
//public class WaitOutRemindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//    public static final String TAG = WaitOutRemindAdapter.class.getSimpleName();
//    private static Context mcontext;
//    private List<NewLeaveBean.RowsDTO> sfjhBeanList = new ArrayList<>(); //随访计划 数据
//    private PhoneFollowupCliclistener phoneFollowupCliclistener;
//
//    public WaitOutRemindAdapter(List<NewLeaveBean.RowsDTO> sfjhBeanList, Context context) {
//        this.sfjhBeanList = sfjhBeanList;
//        this.mcontext = context;
//    }
//
//
//    public void setOnItemPhoneNumCallback(PhoneFollowupCliclistener callback) {
//        this.phoneFollowupCliclistener = callback;
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_sfjh_layout , parent, false);
//        return  new WaitOutRemindAdapter.WaitoutViewHodler(view) ;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return sfjhBeanList.size() > 0 ? sfjhBeanList.size() : 0;
//    }
//
//
//    public void updateList(List<NewLeaveBean.RowsDTO> sfjhBeanList) {
//        this.sfjhBeanList = sfjhBeanList;
//        notifyDataSetChanged();
//    }
//
//
//
//
//
//
//    public static class WaitoutViewHodler extends RecyclerView.ViewHolder {
//
//        @BindView(R.id.img_head)
//        ImageView iv_head;
//        @BindView(R.id.tv_name)
//        TextView tv_name;
//        @BindView(R.id.tv_age)
//        TextView tv_age;
//        @BindView(R.id.tv_sex)
//        TextView tv_sex;
//        @BindView(R.id.tv_time)
//        TextView tv_time;
//
//
//
//        public WaitoutViewHodler(@NonNull View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//
//
//        public void bingData(NewLeaveBean.RowsDTO sfjhBean) {
//            tv_name.setText(sfjhBean.getUserName());
//            String curen = TimeUtils.getCurrenTime();
//            int finatime = Integer.valueOf(curen) - Integer.valueOf((sfjhBean.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
//            tv_age.setText(finatime + "岁");
//            tv_sex.setText(sfjhBean.getSex());
//            tv_time.setText(sfjhBean.getDiagDate());
//
////            tv_phone.setVisibility(isNumeric(sfjhBean.getInfoDetail().getDhhm()) && !EmptyUtil.isEmpty(sfjhBean.getInfoDetail().getDhhm()) ? View.VISIBLE : View.GONE);
//            iv_head.setImageDrawable(sfjhBean.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
//
//        }
//
//    }
//
//
//}
