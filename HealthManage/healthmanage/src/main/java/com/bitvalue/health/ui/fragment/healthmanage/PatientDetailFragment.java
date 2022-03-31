package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.FRAGMENT_DETAIL;
import static com.bitvalue.health.util.Constants.IDCARD_NUMBER;
import static com.bitvalue.health.util.Constants.USER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PatientBaseInfoBean;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.VisitPlanDetailContract;
import com.bitvalue.health.presenter.healthmanager.VisitPlanDetailPresenter;
import com.bitvalue.health.ui.activity.HealthFilesActivity;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.ImageListDisplayAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.hjq.toast.ToastUtils;

import java.util.EventListener;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 01/14
 * 患者详情界面
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
    @BindView(R.id.tv_more_data)
    TextView tv_moredata;//更多数据


    //    @BindView(R.id.rl_sendmessage)
//    RelativeLayout sendMessage;
//    @BindView(R.id.rl_plan)
//    RelativeLayout createPlan;
    @BindView(R.id.rl_back)
    RelativeLayout back;

    @BindView(R.id.tv_heigth)
    TextView tv_heigth;
    @BindView(R.id.tv_weight)
    TextView tv_weight;

    @BindView(R.id.tv_specialdisease)
    TextView tv_specialdisease;

    @BindView(R.id.list_visitload)
    WrapRecyclerView list_visitlod;
    @BindView(R.id.ll_image)
    LinearLayout ll_image_default;
    @BindView(R.id.iv_patient_icon)
    ImageView iv_head;
    @BindView(R.id.tv_bingli_detail)
    TextView tv_bingli_detail;

    private NewLeaveBean.RowsDTO itemPosition;
    private ImageListDisplayAdapter displayAdapter;
    private HomeActivity homeActivity;
    private int userID = 0;

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
        itemPosition = (NewLeaveBean.RowsDTO) getArguments().getSerializable(FRAGMENT_DETAIL);
        userID = getArguments().getInt(USER_ID);
        if (itemPosition != null) {
            if (!EmptyUtil.isEmpty(itemPosition.getSex())) {
                iv_head.setImageDrawable(itemPosition.getSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
            }
            tv_sendMessage.setBackground(EmptyUtil.isEmpty(itemPosition.getUserId()) ? homeActivity.getDrawable(R.drawable.shape_bg_gray_) : homeActivity.getDrawable(R.drawable.shape_bg_blue_dark));
            tv_distributionplan.setBackground((EmptyUtil.isEmpty(itemPosition.getUserId()) || (itemPosition.getPlanInfo().size() == 0)) ? homeActivity.getDrawable(R.drawable.shape_bg_gray_) : homeActivity.getDrawable(R.drawable.shape_bg_blue_dark));
            tv_name.setText(itemPosition.getUserName());
            tv_sex.setText(itemPosition.getSex());
            tv_depatment.setText(itemPosition.getKsmc());
            tv_inpatient_area.setText(itemPosition.getBqmc());
            tv_diseasename.setText(itemPosition.getCyzd());
            tv_specialdisease.setText(itemPosition.getBqmc());
            String curen = TimeUtils.getCurrenTime();
            if (!EmptyUtil.isEmpty(itemPosition.getAge())) {
                int finatime = Integer.valueOf(curen) - Integer.valueOf((itemPosition.getAge().substring(0, 4)));  //后台给的是出生日期 需要前端换算
                tv_age.setText(finatime + "岁");
            }
            if (itemPosition.getInfoDetail() != null && null != itemPosition.getInfoDetail().getSjhm()) {
                if (itemPosition.getInfoDetail().getSjhm().equals("******")) {
                    tv_phone.setText(itemPosition.getInfoDetail().getDhhm());
                } else {
                    tv_phone.setText(itemPosition.getInfoDetail().getSjhm());
                }
//                tv_address.setText(itemPosition.getInfoDetail().getGzdwdz());
            } else {
                tv_phone.setVisibility(View.GONE);
            }
            if (!EmptyUtil.isEmpty(itemPosition.getSex()))
                tv_heigth.setText(itemPosition.getSex().equals("女") ? "162cm" : "175cm");

            //根据id 查询患者的详情
        } else if (!EmptyUtil.isEmpty(userID)) {
            mPresenter.getPatientBaseInfo(userID);
        }
    }

    @Override
    public void initData() {
        super.initData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        list_visitlod.setLayoutManager(layoutManager);
        displayAdapter = new ImageListDisplayAdapter(R.layout.item_imagevisitlod_layout, null);
        list_visitlod.setAdapter(displayAdapter);
        UserLocalVisitBean bean = new UserLocalVisitBean();
        if (itemPosition != null) {
            if (!EmptyUtil.isEmpty(itemPosition.getUserId())) {
                bean.userId = itemPosition.getUserId();
            } else {
                ToastUtils.show("患者未注册!");
                return;
            }
        } else {
            bean.userId = String.valueOf(userID);
        }
        showLoading();
        mPresenter.qryUserLocalVisit(bean);


    }


    @OnClick({R.id.tv_sendmessage, R.id.tv_logout, R.id.tv_more_data, R.id.tv_bingli_detail})
    public void OnClick(View view) {
        switch (view.getId()) {
            //发送消息
            case R.id.tv_sendmessage:
                if (EmptyUtil.isEmpty(itemPosition.getUserId())) {
                    ToastUtils.show("该患者未注册!");
                    return;
                }

                NewLeaveBean.RowsDTO info = new NewLeaveBean.RowsDTO();
                info.setUserName(itemPosition.getUserName());
                info.setUserId(itemPosition.getUserId());
                info.setKsmc(itemPosition.getKsmc());
                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, info);
                break;

//          随访计划
            case R.id.tv_logout:
                if (itemPosition.getPlanInfo().size() == 0) {
                    ToastUtils.show("该患者暂未分配计划!");
                    return;
                }

                String planID = itemPosition.getPlanInfo().get(0).getPlanId();
                itemPosition.planId = planID;
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN_PREVIEW, itemPosition);
                break;

            //更多数据
            case R.id.tv_more_data:
                if (EmptyUtil.isEmpty(itemPosition)) {
                    NewLeaveBean.RowsDTO createItem = new NewLeaveBean.RowsDTO();
                    createItem.setUserId(String.valueOf(userID));
                    homeActivity.switchSecondFragment(Constants.FRAGMENT_MORE_DATA, createItem);
                } else {
                    homeActivity.switchSecondFragment(Constants.FRAGMENT_MORE_DATA, itemPosition);
                }
                break;

//               病历详情
            case R.id.tv_bingli_detail:
                Intent intent = new Intent(homeActivity, HealthFilesActivity.class);
                if (userID == 0) {
                    if (!EmptyUtil.isEmpty(itemPosition) && !EmptyUtil.isEmpty(itemPosition.getUserId()))
                        intent.putExtra(USER_ID, Integer.valueOf(itemPosition.getUserId()));
                } else {
                    intent.putExtra(USER_ID, userID);
                }
                homeActivity.startActivity(intent);
                break;
        }
    }


    /**
     * 获取患者上传资料成功回调
     *
     * @param listBean
     */
    @Override
    public void qryUserVisitSuccess(List<String> listBean) {
        homeActivity.runOnUiThread(() -> {
            hideDialog();
            if (listBean != null && listBean.size() > 0) {
                displayAdapter.setNewData(listBean);
            }
        });
    }

    /**
     * 获取患者上传资料失败回调
     *
     * @param
     */
    @Override
    public void qryUserVisitFail(String failMessage) {
        homeActivity.runOnUiThread(() -> {
            hideDialog();
        });
    }


    /**
     * 根据Userid 查询患者更多信息 成功回调
     *
     * @param patientBaseInfoBean
     */
    @Override
    public void getPatientBaseInfoSuccess(PatientBaseInfoBean patientBaseInfoBean) {
        homeActivity.runOnUiThread(() -> {
            if (patientBaseInfoBean != null) {
                if (!EmptyUtil.isEmpty(patientBaseInfoBean.getBaseInfo().getUserSex())) {
                    iv_head.setImageDrawable(patientBaseInfoBean.getBaseInfo().getUserSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
                }
                tv_name.setText(patientBaseInfoBean.getBaseInfo().getUserName());
                tv_sex.setText(patientBaseInfoBean.getBaseInfo().getUserSex());
                tv_depatment.setText("-");
                tv_inpatient_area.setText("-");
                tv_diseasename.setText("-");
                String curen = TimeUtils.getCurrenTime();
                if (!EmptyUtil.isEmpty(patientBaseInfoBean.getBaseInfo().getBirthday())) {
                    int finatime = Integer.valueOf(curen) - Integer.valueOf((patientBaseInfoBean.getBaseInfo().getBirthday().substring(0, 4)));  //后台给的是出生日期 需要前端换算
                    tv_age.setText(finatime + "岁");
                }
                tv_phone.setText(patientBaseInfoBean.getExternalInfo().getPhone());
                tv_address.setText(patientBaseInfoBean.getExternalInfo().getAddress());
                tv_heigth.setText(String.valueOf(patientBaseInfoBean.getExternalInfo().getHeight()));
                tv_weight.setText(String.valueOf(patientBaseInfoBean.getExternalInfo().getWeight()));
            }
        });

    }

    /**
     * 根据Userid 查询患者更多信息 失败回调
     *
     * @param messageFail
     */
    @Override
    public void getPatientBaseInfoFail(String messageFail) {
        homeActivity.runOnUiThread(() -> {
//            hideDialog();
            ToastUtils.show("获取详情失败:" + messageFail);
        });
    }
}
