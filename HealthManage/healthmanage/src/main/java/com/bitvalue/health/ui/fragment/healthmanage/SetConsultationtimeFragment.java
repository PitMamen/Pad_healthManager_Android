package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.PATIENT_EXPECTTIME;
import static com.bitvalue.health.util.Constants.PATIENT_TRADEID;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.chatUtil.CustomHealthPlanMessage;
import com.bitvalue.health.util.customview.dialog.ConfirmTimeDialog;
import com.bitvalue.health.util.customview.dialog.SetConsultionTimeDialog;
import com.bitvalue.healthmanage.R;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 05/04
 * <p>
 * 电话预约设置咨询时间界面
 */
public class SetConsultationtimeFragment extends BaseFragment {
    @BindView(R.id.tv_expecttime)
    TextView tv_expecttime;
    @BindView(R.id.re_settime_one)
    RelativeLayout re_settime_one;  //时间段1
    @BindView(R.id.re_settime_two)
    RelativeLayout re_settime_two;  //时间段2
    @BindView(R.id.re_settime_three)
    RelativeLayout re_settime_three;  //时间段3
    @BindView(R.id.back_btn)
    ImageView btn_back;

    @BindView(R.id.time_one)
    TextView tv_timeone;

    @BindView(R.id.time_two)
    TextView tv_timetwo;

    @BindView(R.id.time_three)
    TextView tv_timethree;

    @BindView(R.id.tv_btnconfirm)
    TextView btn_comfirm;//确认

    @BindView(R.id.tv_doc_confirmtime)
    TextView tv_docconfirmtime;
    @BindView(R.id.tv_doc_changetime)
    TextView getTv_docconfirmtime;

    private String finalTimes = "";


    private HomeActivity homeActivity;
    private String tradeid;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_consultationtime_layout;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        if (getArguments() == null) {
            return;
        }
        String patientTime = getArguments().getString(PATIENT_EXPECTTIME, "00:00-24:00");
        tradeid = getArguments().getString(PATIENT_TRADEID, "");
        tv_expecttime.setText(patientTime);

    }

    @OnClick({R.id.back_btn, R.id.re_settime_one, R.id.re_settime_two, R.id.re_settime_three, R.id.tv_btnconfirm})
    public void onClickbutton(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
            //时间段1
            case R.id.re_settime_one:
                showSetTimeDialog(tv_timeone);
                break;
            //时间段2
            case R.id.re_settime_two:
                showSetTimeDialog(tv_timetwo);
                break;
            //时间段3
            case R.id.re_settime_three:
                showSetTimeDialog(tv_timethree);
                break;
            //确认时间  如果医生选中了三个 则跳出弹框 让气选中一个  小于三个 直接提交
            case R.id.tv_btnconfirm:
//                if (showSetConfirmDialog()) return;
                // TODO: 2022/5/4 医生只选中了一个或两个时间段 则直接提交
                if (!EmptyUtil.isEmpty(finalTimes)) {
                    CustomHealthPlanMessage customHealthPlanMessage = new CustomHealthPlanMessage();
                    customHealthPlanMessage.tradeId = tradeid;
                    customHealthPlanMessage.time = finalTimes;
                    customHealthPlanMessage.title = "预约时间";
                    customHealthPlanMessage.type = "CustomAppointmentTimeMessage";
                    customHealthPlanMessage.description = "请选择时间";
                    EventBus.getDefault().post(customHealthPlanMessage);
                    if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {   //退出当前界面
                        homeActivity.getSupportFragmentManager().popBackStack();
                    }
                } else {
                    ToastUtils.show("请至少选择一个时间段!");
                }

                break;
        }

    }

    private boolean showSetConfirmDialog() {
        if (tv_timeone.getText().toString().contains("-") && tv_timetwo.getText().toString().contains("-") && tv_timethree.getText().toString().contains("-")) {
            // TODO: 2022/5/4 弹框 选一个
            if (getTv_docconfirmtime.getVisibility() == View.GONE && tv_docconfirmtime.getVisibility() == View.GONE) {
                List<String> sorcrListTime = new ArrayList<>();
                sorcrListTime.add(tv_timeone.getText().toString());
                sorcrListTime.add(tv_timetwo.getText().toString());
                sorcrListTime.add(tv_timethree.getText().toString());
                ConfirmTimeDialog confirmTimeDialog = new ConfirmTimeDialog(homeActivity, sorcrListTime);
                confirmTimeDialog.setOnClickListener(() -> {
                    tv_docconfirmtime.setVisibility(View.VISIBLE);
                    getTv_docconfirmtime.setVisibility(View.VISIBLE);
                    String confirmedTime = confirmTimeDialog.getconfirmedTime();
                    getTv_docconfirmtime.setText(confirmedTime);
                    confirmTimeDialog.dismiss();
                    // TODO: 2022/5/4 提交

                }).show();
                return true;
            }
        }
        return false;
    }

    private void showSetTimeDialog(TextView textView) {
        SetConsultionTimeDialog setConsultionTimeDialog = new SetConsultionTimeDialog(homeActivity);
        setConsultionTimeDialog.setOnClickListener(() -> {
            String selectedTime = setConsultionTimeDialog.getselectedTime();
            textView.setText(selectedTime);
            setConsultionTimeDialog.dismiss();
            finalTimes += selectedTime + ",";
        }).show();
    }


}
