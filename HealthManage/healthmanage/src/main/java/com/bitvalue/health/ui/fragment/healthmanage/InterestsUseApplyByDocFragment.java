package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.TASKDETAIL;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.eventbusbean.NotifyactionObj;
import com.bitvalue.health.api.eventbusbean.RefreshDataViewObj;
import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.api.requestbean.SummaryBean;
import com.bitvalue.health.api.requestbean.filemodel.SystemRemindObj;
import com.bitvalue.health.api.responsebean.CallResultBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.QueryRightsRecordBean;
import com.bitvalue.health.api.responsebean.TaskDeatailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.InterestsUseApplyByDocContract;
import com.bitvalue.health.presenter.healthmanager.InterestsUseApplyByDocPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.GsonUtils;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.healthmanage.R;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

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
    @BindView(R.id.tv_startwenzhen)
    TextView tv_startwenzhen;

    @BindView(R.id.tv_chat_record)
    TextView tv_chat_record; //问诊记录


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
        if (taskDeatailBean == null || taskDeatailBean.getTaskDetail() == null || taskDeatailBean.getTaskDetail().getUserInfo() == null) {
            return;
        }
        loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
        initCompView();
    }


    private void initCompView() {
        if (taskDeatailBean.getExecFlag() != 1
                && taskDeatailBean.getTaskDetail().getRightsType().equalsIgnoreCase(Constants.RIGTH_TYPE)) {
            iv_endConsultationButton.setVisibility(View.VISIBLE);
            tv_end_consultation.setVisibility(View.VISIBLE);
            tv_end_consultation.setText("结束本次会诊");
        }
        //如果执行完了的  不显示开始问诊
        if (taskDeatailBean.getExecFlag() == 1) {
            start_consultation.setVisibility(View.INVISIBLE);
        } else {
            //  如果是 ICU的 是线下会诊 不需要进入聊天接诊
            start_consultation.setVisibility(taskDeatailBean.getTaskDetail().getRightsType().equalsIgnoreCase(Constants.RIGTH_TYPE) ? View.INVISIBLE : View.VISIBLE);
//            tv_startwenzhen.setText(taskDeatailBean.getTaskDetail().getExecFlag() == 0&&(taskDeatailBean.getTaskDetail().getRightsType().equals("telNum")||taskDeatailBean.getTaskDetail().getRightsType().equals("videoNum"))? getString(R.string.confirmtime) : getString(R.string.startconsultation));
            tv_startwenzhen.setText(taskDeatailBean.getTaskDetail().getExecFlag() == 0? getString(R.string.confirmtime) : getString(R.string.startconsultation));
        }

        //如果已经执行完毕 是重症医学科下面的 ICU权益 并且是线上会诊的 不显示问诊记录按钮 其他的都显示
        if (taskDeatailBean.getExecFlag() == 1) {
            if (taskDeatailBean.getTaskDetail().getRightsType().equalsIgnoreCase(Constants.RIGTH_TYPE)) {
                tv_chat_record.setVisibility(View.GONE);
            } else {
                tv_chat_record.setVisibility(View.VISIBLE);
            }
        } else {
            tv_chat_record.setVisibility(View.GONE);
        }
        tv_visitNameByDoc.setText(loginBean.getUser().user.userName); //执行人  (医生自己)
        tv_department.setText(taskDeatailBean.getTaskDetail().getDeptName()); //科室名称
        tv_start_time.setText(TimeUtils.getTime_tosecond(taskDeatailBean.getTaskDetail().getExecTime())); //执行时间
        tv_patientName.setText(taskDeatailBean.getTaskDetail().getUserInfo().getUserName()); //就诊人
        tv_continue_time.setText(EmptyUtil.isEmpty(taskDeatailBean.getTaskDetail().getRemark()) ? "待确认" : taskDeatailBean.getTaskDetail().getRemark() + "分钟");  //持续时间
        tv_applyType.setText(taskDeatailBean.getTaskDetail().getRightsName() + "申请:");  //权益申请类型 (图文咨询,视频咨询,重症会诊)
    }


    @OnClick({R.id.iv_icon, R.id.tv_end_consultation, R.id.tv_clickgotodetail, R.id.rl_start_consultation, R.id.rl_back, R.id.tv_chat_record})
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

                if (tv_end_consultation.getVisibility() == View.VISIBLE && tv_end_consultation.getText().toString().equals(homeActivity.getString(R.string.has_ended))) {
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
                        Log.e(TAG, "结束问诊=== ");
                        mPresenter.saveRightsUseRecord(saveRightsUseBean);
                        /**
                         * 提醒通知 调用接口
                         */
                        if (EmptyUtil.isEmpty(taskDeatailBean.getTaskDetail())) {
                            Log.e(TAG, "结束问诊 taskDeatailBean.getTaskDetail == null");
                            return;
                        }
                        TaskDeatailBean.TaskDetailDTO taskDetail = taskDeatailBean.getTaskDetail();
                        String jsonString = GsonUtils.ModelToJson(taskDetail);
                        sendSystemRemind(jsonString);
                    }

                    @Override
                    public void onNegative() {

                    }
                });


                break;

            //进入患者详情界面
            case R.id.tv_clickgotodetail:
                //如果是重症科室并且有提交资料的 进入 资料审核界面 其他科室进入 患者详情
                if (taskDeatailBean.getTaskDetail().getUploadDocFlag() == 1) {
                    taskDeatailBean.isShowBottomBuntton = false;  //不显示底部  两个按钮
                    homeActivity.switchSecondFragment(Constants.DATA_REVIEW, taskDeatailBean);
                } else {
                    homeActivity.switchSecondFragment(Constants.DATA_REVIEW, String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));
                }
                break;

            //开始问诊 进入聊天界面
            case R.id.rl_start_consultation:
                //创建一个患者
                NewLeaveBean.RowsDTO item = new NewLeaveBean.RowsDTO();
                item.setUserId(String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));
                item.setUserName(taskDeatailBean.getTaskDetail().getUserInfo().getUserName());
                item.setKsmc(taskDeatailBean.getTaskDetail().getDeptName()); //传科室名称
                item.isConsultation = true;   //问诊
                item.taskDeatailBean = taskDeatailBean;
                item.rightsType = taskDeatailBean.getTaskDetail().getRightsType();
                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, item);
                break;

            //问诊记录
            case R.id.tv_chat_record:
                NewLeaveBean.RowsDTO item_recordchat = new NewLeaveBean.RowsDTO();
                item_recordchat.setUserId(String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));
                item_recordchat.setUserName(taskDeatailBean.getTaskDetail().getUserInfo().getUserName());
                homeActivity.switchSecondFragment(Constants.FRAGMENT_RECORD_CHAT, item_recordchat);
                break;

        }
    }


    private void sendSystemRemind(String jsonString) {
        SystemRemindObj systemRemindObj = new SystemRemindObj();
        systemRemindObj.remindType = "videoRemind";
        systemRemindObj.eventType = 6;//6  结束问诊
        systemRemindObj.infoDetail = jsonString;
        systemRemindObj.userId = String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId());
        EasyHttp.post(homeActivity).api(systemRemindObj).request(new OnHttpListener<ApiResult<String>>() {
            @Override
            public void onSucceed(ApiResult<String> result) {
//                Log.e(TAG, "通知请求: " + result.getMessage());
            }

            @Override
            public void onFail(Exception e) {

            }
        });
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
//        tv_end_consultation.setText(homeActivity.getString(R.string.has_ended));
        start_consultation.setVisibility(View.INVISIBLE);
        if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            homeActivity.getSupportFragmentManager().popBackStack();
        }
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
            EventBus.getDefault().post(new NotifyactionObj());
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
            ToastUtils.show(failMessage);
        });
    }


    /***
     * 以下两个回调不需要实现
     * @param reViewRecordResponse
     */

    //发送问诊小结 成功回调
    @Override
    public void sendsummary_resultSuucess(boolean reViewRecordResponse) {

    }

    //发送问诊小结 失败回调
    @Override
    public void sendsummary_resultFail(String messageFail) {

    }


    /**
     *以下两个回调 不需实现
     */

    /**
     * 获取问诊小结 成功回调
     *
     * @param reViewRecordResponse
     */
    @Override
    public void getSummaryListSuucess(List<SummaryBean> reViewRecordResponse) {

    }

    /**
     * 获取问诊小结 失败回调
     *
     * @param
     */
    @Override
    public void getSummaryListFail(String messageFail) {

    }

    @Override
    public void saveCaseCommonWordsSuccess(String quickReplyResult) {

    }

    @Override
    public void saveCaseCommonWordsFail(String failMessage) {

    }

    /**
     * 以下两个回调 这个界面不需要实现
     */

    /**
     * 拨打电话成功回调
     *
     * @param resultBean
     */
    @Override
    public void callSuccess(boolean resultBean) {

    }

    /**
     * 拨打电话失败回调
     *
     * @param
     */
    @Override
    public void callFail(String failMessage) {

    }



    /***
     * 查询权益使用记录 成功回调
     * @param queryRightsRecordBean
     */
    @Override
    public void queryRightsRecordSuccess(QueryRightsRecordBean queryRightsRecordBean) {

    }


    /***
     * 查询权益使用记录 失败回调
     * @param faiMessage
     */
    @Override
    public void queryRightsRecordFail(String faiMessage) {
        homeActivity.runOnUiThread(() -> {
//            ToastUtils.show(faiMessage);
        });
    }



    /**
     * 医生接诊 更新权益时间  回调
     * @param isSuccess
     */
    @Override
    public void updateRightsRequestTimeSuccess(boolean isSuccess) {

    }

    @Override
    public void updateRightsRequestTimeFail(String failMessage) {

    }


    /**
     * 查询 患者权益 条文限制 这里不需要实现
     * @param listData
     */
    @Override
    public void qryRightsUserLogSuccess(List<DataReViewRecordResponse> listData) {

    }

    @Override
    public void qryRightsUserLogFail(String failMessage) {

    }


}
