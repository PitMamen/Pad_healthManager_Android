package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.TASKDETAIL;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.api.eventbusbean.NotifycationAlardyObj;
import com.bitvalue.health.api.eventbusbean.RefreshDataViewObj;
import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.TaskDeatailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.InterestsUseApplyByDocContract;
import com.bitvalue.health.presenter.healthmanager.InterestsUseApplyByDocPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 02/18
 * 医生账号 权益使用申请界面
 */
public class InterestsUseApplyByDocFragment extends BaseFragment<InterestsUseApplyByDocPresenter> implements InterestsUseApplyByDocContract.View {
    @BindView(R.id.rl_back)
    RelativeLayout back;
    @BindView(R.id.iv_icon)
    ImageView iv_endConsultationButton;
    @BindView(R.id.tv_end_consultation)
    TextView tv_end_consultation;
    @BindView(R.id.tv_name_by_doc)
    TextView tv_visitNameByDoc;
    @BindView(R.id.tv_department)
    TextView tv_department;
    @BindView(R.id.tv_starttime)
    TextView tv_start_time;
    @BindView(R.id.tv_continue_time)
    TextView tv_continue_time;
    @BindView(R.id.visit_type_apply)
    TextView tv_applyType;
    @BindView(R.id.tv_patient_name)
    TextView tv_patientName;
    @BindView(R.id.tv_clickgotodetail)
    TextView tv_clickGotoDeatail;
    @BindView(R.id.rl_start_consultation)
    RelativeLayout start_consultation;


    private HomeActivity homeActivity;
    private TaskDeatailBean taskDeatailBean;
    private LoginBean loginBean;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    protected InterestsUseApplyByDocPresenter createPresenter() {
        return new InterestsUseApplyByDocPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_applybydoc_layout;
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        EventBus.getDefault().register(this);
        taskDeatailBean = (TaskDeatailBean) getArguments().getSerializable(TASKDETAIL);
        if (taskDeatailBean == null) {
            return;
        }
        loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
        iv_endConsultationButton.setVisibility(taskDeatailBean.getExecFlag() == 1 ? View.GONE : View.VISIBLE); //如果是已办 则不显示处理完成按钮
        tv_end_consultation.setVisibility(taskDeatailBean.getExecFlag() == 1 ? View.GONE : View.VISIBLE); //如果是已办 则不显示处理完成按钮
        start_consultation.setVisibility(taskDeatailBean.getExecFlag() == 1 ? View.INVISIBLE : View.VISIBLE); //如果是已办 则不显示开始问诊按钮
        tv_visitNameByDoc.setText(loginBean.getUser().user.userName); //执行人  (医生自己)
        tv_department.setText(taskDeatailBean.getTaskDetail().getDeptName()); //科室名称
        tv_start_time.setText(TimeUtils.getTime_tosecond(taskDeatailBean.getTaskDetail().getExecTime())); //执行时间
        tv_patientName.setText(taskDeatailBean.getTaskDetail().getUserInfo().getUserName()); //就诊人
        tv_continue_time.setText(taskDeatailBean.getTaskDetail().getRemark()+"分钟");  //持续时间
        tv_applyType.setText(taskDeatailBean.getTaskDetail().getRightsName());  //权益申请类型 (图文咨询,视频咨询)

    }


    @OnClick({R.id.iv_icon, R.id.tv_end_consultation, R.id.tv_clickgotodetail, R.id.rl_start_consultation, R.id.rl_back})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回界面
            case R.id.rl_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;

            //结束问诊
            case R.id.iv_icon:
            case R.id.tv_end_consultation:

                if (tv_end_consultation.getText().toString().equals(homeActivity.getString(R.string.has_ended))){
                    ToastUtils.show("已结束问诊!无需再次结束");
                    return;
                }

                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定结束问诊吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        SaveRightsUseBean saveRightsUseBean = new SaveRightsUseBean();
                        saveRightsUseBean.deptName = loginBean.getUser().user.departmentName;
                        saveRightsUseBean.execDept = String.valueOf(loginBean.getUser().user.departmentId); //这里传科室代码
                        saveRightsUseBean.execFlag = 1;
                        saveRightsUseBean.execTime = TimeUtils.getTime_tosecond(taskDeatailBean.getExecTime());
                        saveRightsUseBean.execUser = String.valueOf(loginBean.getUser().user.userId);
                        saveRightsUseBean.id = taskDeatailBean.getTaskDetail().getId();
                        saveRightsUseBean.rightsId = taskDeatailBean.getTaskDetail().getRightsId();
                        saveRightsUseBean.rightsName = taskDeatailBean.getTaskDetail().getRightsName();
                        saveRightsUseBean.rightsType = taskDeatailBean.getTaskDetail().getRightsType();
                        saveRightsUseBean.statusDescribe = "结束问诊";
                        saveRightsUseBean.tradeId = taskDeatailBean.getTaskDetail().getTradeId();
                        saveRightsUseBean.userId = taskDeatailBean.getTaskDetail().getUserId();
                        saveRightsUseBean.taskId = String.valueOf(taskDeatailBean.getId());
                        mPresenter.saveRightsUseRecord(saveRightsUseBean);
                        Log.e(TAG, "结束问诊=== ");
                    }

                    @Override
                    public void onNegative() {

                    }
                });
                break;

            //进入患者详情界面
            case R.id.tv_clickgotodetail:
                homeActivity.switchSecondFragment(Constants.FRAGMENT_DETAIL, Integer.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));
                break;

            //开始问诊 进入聊天界面
            case R.id.rl_start_consultation:
                //创建一个患者
                NewLeaveBean.RowsDTO item = new NewLeaveBean.RowsDTO();
                item.setUserId(String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));
                item.setUserName(taskDeatailBean.getTaskDetail().getUserInfo().getUserName());
                item.isConsultation = true;   //问诊
                item.taskDeatailBean = taskDeatailBean;
                item.rightsName = taskDeatailBean.getTaskDetail().getRightsName();
                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, item);
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 更新视图
     */
    private void refreshView() {
        tv_end_consultation.setText(homeActivity.getString(R.string.has_ended));
        start_consultation.setVisibility(View.INVISIBLE);
    }


    /**
     * 更新已办数据 重新请求接口
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateEndVisitText(RefreshDataViewObj obj) {
        refreshView();
    }


    /**
     * 结束问诊 成功回调
     *
     * @param bean
     */
    @Override
    public void saveRightsUseRecordSuccess(SaveRightsUseBean bean) {
        homeActivity.runOnUiThread(() -> {
//            Log.e(TAG, "成功---------------" + bean.execFlag);
            refreshView();
            EventBus.getDefault().post(new NotifycationAlardyObj());
            ToastUtils.show("结束问诊处理成功!");
        });
    }

    /**
     * 结束问诊 失败回调
     *
     * @param failMessage
     */
    @Override
    public void saveRightsUseRecordFail(String failMessage) {
        homeActivity.runOnUiThread(() -> {
            ToastUtils.show("结束问诊处理失败!");
        });
    }
}
