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
                showSetTimeDialog(tv_timeone, 1);
                break;
            //时间段2
            case R.id.re_settime_two:
                showSetTimeDialog(tv_timetwo, 2);
                break;
            //时间段3
            case R.id.re_settime_three:
                showSetTimeDialog(tv_timethree, 3);
                break;
            //确认时间  如果医生选中了三个 则跳出弹框 让气选中一个  小于三个 直接提交
            case R.id.tv_btnconfirm:
//                if (showSetConfirmDialog()) return;
                // TODO: 2022/5/4 医生只选中了一个或两个时间段 则直接提交
                if (!EmptyUtil.isEmpty(time1) || !EmptyUtil.isEmpty(time2) || !EmptyUtil.isEmpty(time3)) {
                    CustomHealthPlanMessage customHealthPlanMessage = new CustomHealthPlanMessage();
                    customHealthPlanMessage.tradeId = tradeid;
                    finalTimes = time1 + "," + time2 + "," + time3;
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



    private String time1 = "";
    private String time2 = "";
    private String time3 = "";

    private void showSetTimeDialog(TextView textView, int timeslot) {
        SetConsultionTimeDialog setConsultionTimeDialog = new SetConsultionTimeDialog(homeActivity);
        setConsultionTimeDialog.setOnClickListener(() -> {
            String selectedTime = setConsultionTimeDialog.getselectedTime();
            textView.setText(selectedTime);
            setConsultionTimeDialog.dismiss();
            switch (timeslot) {
                case 1:    //时间段1
                    time1 = selectedTime;
                    break;
                case 2:    //时间段2
                    time2 = selectedTime;
                    break;
                case 3:   //时间段3
                    time3 = selectedTime;
                    break;
            }
//            finalTimes += selectedTime + ",";
        }).show();
    }


}
