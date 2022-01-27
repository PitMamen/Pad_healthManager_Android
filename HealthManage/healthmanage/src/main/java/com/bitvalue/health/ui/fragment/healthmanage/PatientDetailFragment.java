package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.FRAGMENT_DETAIL;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.VisitPlanDetailContract;
import com.bitvalue.health.presenter.healthmanager.VisitPlanDetailPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.ImageListDisplayAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.hjq.toast.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 01/14
 */
public class PatientDetailFragment extends BaseFragment<VisitPlanDetailPresenter> implements VisitPlanDetailContract.View {

    @BindView(R.id.tv_patient_name)
    TextView tv_name;
    @BindView(R.id.tv_patient_sex)
    TextView tv_sex;
    @BindView(R.id.tv_patient_age)
    TextView tv_age;
    @BindView(R.id.tv_patient_phone)
    TextView tv_phone;
    @BindView(R.id.tv_patient_address)
    TextView tv_address;
    @BindView(R.id.tv_sendmessage)
    TextView tv_sendMessage;
    @BindView(R.id.tv_logout)
    TextView tv_distributionplan;
    @BindView(R.id.tv_depatment)
    TextView tv_depatment;//科室名称
    @BindView(R.id.inpatient_area)
    TextView tv_inpatient_area;//病区名称
    @BindView(R.id.tv_diseasename)
    TextView tv_diseasename;//诊断

    //    @BindView(R.id.rl_sendmessage)
//    RelativeLayout sendMessage;
//    @BindView(R.id.rl_plan)
//    RelativeLayout createPlan;
    @BindView(R.id.rl_back)
    RelativeLayout back;

    @BindView(R.id.tv_heigth)
    TextView tv_heigth;

    @BindView(R.id.list_visitload)
    WrapRecyclerView list_visitlod;
    @BindView(R.id.ll_image)
    LinearLayout ll_image_default;

    private NewLeaveBean.RowsDTO itemPosition;
    private ImageListDisplayAdapter displayAdapter;
    private HomeActivity homeActivity;

    @Override
    protected VisitPlanDetailPresenter createPresenter() {
        return new VisitPlanDetailPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_patientdetail_layout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        back.setOnClickListener(v -> {
            if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0)
                homeActivity.getSupportFragmentManager().popBackStack();
        });
        Bundle bundle = getArguments();
        itemPosition = (NewLeaveBean.RowsDTO) bundle.getSerializable(FRAGMENT_DETAIL);
        if (itemPosition != null) {
            tv_sendMessage.setBackground(EmptyUtil.isEmpty(itemPosition.getUserId()) ? homeActivity.getDrawable(R.drawable.shape_bg_gray_) : homeActivity.getDrawable(R.drawable.shape_bg_blue_dark));
            tv_distributionplan.setBackground((EmptyUtil.isEmpty(itemPosition.getUserId()) || (itemPosition.getPlanInfo().size() == 0)) ? homeActivity.getDrawable(R.drawable.shape_bg_gray_) : homeActivity.getDrawable(R.drawable.shape_bg_blue_dark));
            tv_name.setText(itemPosition.getUserName());
            tv_sex.setText(itemPosition.getSex());
            tv_depatment.setText(itemPosition.getKsmc());
            tv_inpatient_area.setText(itemPosition.getBqmc());
            tv_diseasename.setText(itemPosition.getDiagnosis());
            String curen = TimeUtils.getCurrenTime();
            int finatime = Integer.valueOf(curen) - Integer.valueOf((itemPosition.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
            tv_age.setText(finatime + "岁");
            tv_phone.setText(itemPosition.getInfoDetail().getSjhm());
            tv_address.setText(itemPosition.getInfoDetail().getGzdwdz());
            tv_heigth.setText(itemPosition.getSex().equals("女") ? "162cm" : "175cm");

        }
    }

    @Override
    public void initData() {
        super.initData();
        if (!EmptyUtil.isEmpty(itemPosition)) {
            if (!EmptyUtil.isEmpty(itemPosition.getUserId())) {
                showLoading();
                LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity);
                layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                list_visitlod.setLayoutManager(layoutManager);
                displayAdapter = new ImageListDisplayAdapter(R.layout.item_imagevisitlod_layout, null);
                Log.e(TAG, "name: " + itemPosition.getUserName() + " userid: " + itemPosition.getUserId());
                UserLocalVisitBean bean = new UserLocalVisitBean();
                bean.userId = itemPosition.getUserId();
                mPresenter.qryUserLocalVisit(bean);
                list_visitlod.setAdapter(displayAdapter);
            } else {
//                ll_image_default.setVisibility(View.VISIBLE);
            }

        }
    }


    @OnClick({R.id.tv_sendmessage, R.id.tv_logout})
    public void OnClick(View view) {
        switch (view.getId()) {
            //发送消息
            case R.id.tv_sendmessage:
                if (EmptyUtil.isEmpty(itemPosition.getUserId())){
                    ToastUtils.show("该患者未注册!");
                    return;
                }

                NewLeaveBean.RowsDTO info = new NewLeaveBean.RowsDTO();
                info.setUserName(itemPosition.getUserName());
                info.setUserId(itemPosition.getUserId());
                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, info);
                break;

//          随访计划
            case R.id.tv_logout:
                  if (itemPosition.getPlanInfo().size()==0){
                      ToastUtils.show("该患者暂未分配计划!");
                      return;
                  }

                String planID = itemPosition.getPlanInfo().get(0).getPlanId();
                  itemPosition.planId = planID;
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN_PREVIEW,itemPosition);
                break;
        }
    }


    @Override
    public void qryUserVisitSuccess(List<String> listBean) {
        homeActivity.runOnUiThread(() -> {
            if (listBean!=null&&listBean.size()>0){
                hideDialog();
                displayAdapter.setNewData(listBean);
            }
        });
    }

    @Override
    public void qryUserVisitFail(String failMessage) {
        homeActivity.runOnUiThread(() -> {
           hideDialog();
            Log.e(TAG, "qryUserVisitFail: "+failMessage );
//           ToastUtils.show(failMessage);
        });
    }
}
