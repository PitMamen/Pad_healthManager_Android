package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.TASKDETAIL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.eventbusbean.NotifyactionObj;
import com.bitvalue.health.api.eventbusbean.RefreshViewUseApply;
import com.bitvalue.health.api.requestbean.DocListBean;
import com.bitvalue.health.api.requestbean.FinshMidRequestBean;
import com.bitvalue.health.api.requestbean.filemodel.SystemRemindObj;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.MyRightBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.QueryRightsRecordBean;
import com.bitvalue.health.api.responsebean.TaskDeatailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.healthmanagercontract.RightApplyUseContract;
import com.bitvalue.health.presenter.healthmanager.RightApplyUsePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.QueryRightsRecordAdapter;
import com.bitvalue.health.ui.adapter.RightUseTimeRecordAdapter;
import com.bitvalue.health.util.ClickUtils;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.GsonUtils;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.customview.dialog.UseEquityDialog;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 02/18
 * 个案管理师权益申请处理界面
 */
public class InterestsUseApplyFragment extends BaseFragment<RightApplyUsePresenter> implements RightApplyUseContract.View {

    @BindView(R.id.iv_patient_icon)
    ImageView iv_icon;
    @BindView(R.id.tv_patient_name)
    TextView tv_name;
    @BindView(R.id.tv_patient_sex)
    TextView tv_sex;
    @BindView(R.id.tv_patient_age)
    TextView tv_age;
    @BindView(R.id.tv_patient_phone)
    TextView tv_phoneNumber;
    @BindView(R.id.tv_gotodetail)
    TextView tv_detail;
    @BindView(R.id.tv_tcmc)
    TextView tv_tcmc; //套餐名称
    @BindView(R.id.tv_sername)
    TextView tv_serName; //服务名称
    @BindView(R.id.tv_validtime)
    TextView tv_validTime; // 有效期时间

    @BindView(R.id.ll_right_record)
    LinearLayout ll_right_record;

    @BindView(R.id.ll_hasdata)
    LinearLayout ll_hasRightsData;
    @BindView(R.id.rl_default_view)
    RelativeLayout rl_default_view;
    @BindView(R.id.rl_back)
    RelativeLayout back;


    @BindView(R.id.tv_complete)
    TextView btn_processing_complete; //处理完成按钮
    @BindView(R.id.tv_gochat)
    TextView tv_goChat;
    @BindView(R.id.tv_data_review)
    TextView tv_data_review;  //资料审核
    @BindView(R.id.tv_reset)
    TextView tv_reset;//重新设置


    @BindView(R.id.list_right_times)
    WrapRecyclerView list_righttimes;


    @BindView(R.id.list_technological_process)
    WrapRecyclerView list_technological_process;


    private HomeActivity homeActivity;
    private TaskDeatailBean taskDeatailBean;
    private List<MyRightBean> rightBeanList = new ArrayList<>();
    private List<MyRightBean.UserGoodsAttr> userGoodsAttrs = new ArrayList<>();
    private List<QueryRightsRecordBean.RowsDTO> useRecordList = new ArrayList<>();
    private RightUseTimeRecordAdapter rightUseTimeRecordAdapter;
    private QueryRightsRecordAdapter queryRightsRecordAdapter;
    private UseEquityDialog useEquityDialog;
    private int useRecordId;
    private boolean flashRecordRigth = false;
    private LoginBean loginBean;
    private boolean isDistributable = false;  //是否可 分配任务
    private String rigthDepatCode; //权益中科室代码

    @Override
    protected RightApplyUsePresenter createPresenter() {
        return new RightApplyUsePresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_interests_use_apply;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        back.setVisibility(View.GONE);
        EventBus.getDefault().register(this);
        taskDeatailBean = (TaskDeatailBean) getArguments().getSerializable(TASKDETAIL);
        if (taskDeatailBean == null || taskDeatailBean.getTaskDetail() == null || taskDeatailBean.getTaskDetail().getUserInfo() == null) {
            Log.e(TAG, "taskDeatailBean.getTaskDetail() == null");
            return;
        }
        //先获取一下审核记录
        mPresenter.getDataReviewRecord(taskDeatailBean.getTaskDetail().getTradeId(), taskDeatailBean.getTaskDetail().getUserInfo().getUserId() + "");
        Log.e(TAG, "initView: " + taskDeatailBean.getTaskDetail().getRightsType() + "  上否上传资料: " + taskDeatailBean.getTaskDetail().getUploadDocFlag());
        loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);

        initrightUseTimeRecord();
        initRightsRecord();
        initCompView();
    }


    @OnClick({R.id.tv_complete, R.id.tv_gochat, R.id.tv_data_review, R.id.tv_reset, R.id.tv_gotodetail, R.id.tv_patient_phone})
    public void onClickButtonLisenner(View view) {
        switch (view.getId()) {
            //任务分配
            case R.id.tv_complete:
                //这里要做一下区分 如果是 重症科室的  需要先完成资料审核才能进行 任务分派
                if (taskDeatailBean.getTaskDetail().getUploadDocFlag() == 1) {
                    if (isDistributable) {
                        showDialog(loginBean, false);
                    } else {
                        ToastUtils.show("请先审核资料!");
                    }
                    return;
                }
                showDialog(loginBean, false);
                break;
            //进入聊天
            case R.id.tv_gochat:
                    // TODO: 2022/3/25 进入聊天界面
                    NewLeaveBean.RowsDTO info = new NewLeaveBean.RowsDTO();
                    info.setUserName(taskDeatailBean.getTaskDetail().getUserInfo().getUserName());
                    info.setUserId(String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));
                    info.setKsmc(taskDeatailBean.getTaskDetail().getDeptName());
                    info.isShowSendRemind = false;  //进入咨询 不需要显示底部 发送提醒 按钮
                    homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, info);
                break;
            //资料审核
            case R.id.tv_data_review:
                homeActivity.switchSecondFragment(Constants.DATA_REVIEW, taskDeatailBean);
                break;

            //重新设置
            case R.id.tv_reset:
                //重新设置
                tv_reset.setOnClickListener(v -> {
                    showDialog(loginBean, true);  //重新设置 需要携带ID
                });
                break;
            //进入详情
            case R.id.tv_gotodetail:
                tv_detail.setOnClickListener(v -> {
                    homeActivity.switchSecondFragment(Constants.DATA_REVIEW, String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));
                });
                break;

            //拨号
            case R.id.tv_patient_phone:
                callPhone(tv_phoneNumber.getText().toString());
                break;
        }

    }


    //拨号
    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    private void showDialog(LoginBean loginBean, boolean carryuseRecordId) {
        useEquityDialog.setDepartment(taskDeatailBean.getTaskDetail().getDeptName()).setVisitName(taskDeatailBean.getTaskDetail().getUserInfo().getUserName()).setVisitType(taskDeatailBean.getTaskDetail().getRightsName()).setOnclickListener(new UseEquityDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                //防止连续点击
                if (!ClickUtils.isFastClick()) {
                    FinshMidRequestBean finshMidRequestBean = new FinshMidRequestBean();
                    String selectDoctor = useEquityDialog.getSelectDoc();
                    String selectContinueTime = useEquityDialog.getSelectContinueTime();
                    String selectTakeTime = useEquityDialog.getSelectTakeTime() + ":00";
                    int docUserId = useEquityDialog.getDocUserId();
//                    finshMidRequestBean.deptName = loginBean.getUser().user.departmentName;
//                    finshMidRequestBean.execDept = loginBean.getUser().user.departmentCode; //这里传科室代码
                    finshMidRequestBean.execDept = rigthDepatCode; //这里传权益中的科室代码
                    finshMidRequestBean.deptName = taskDeatailBean.getTaskDetail().getDeptName(); //这里传 该权益所属科室名称 之前是传个案师所在的科室
                    finshMidRequestBean.lastTime = Integer.valueOf(selectContinueTime);
                    finshMidRequestBean.execFlag = 2;
                    finshMidRequestBean.execTime = selectTakeTime;
                    finshMidRequestBean.execUser = String.valueOf(docUserId);    //这里要传医生ID
                    finshMidRequestBean.rightsId = taskDeatailBean.getTaskDetail().getRightsId();
                    finshMidRequestBean.rightsName = taskDeatailBean.getTaskDetail().getRightsName();
                    finshMidRequestBean.rightsType = taskDeatailBean.getTaskDetail().getRightsType();
                    finshMidRequestBean.execName = selectDoctor;
                    flashRecordRigth = carryuseRecordId;
                    if (carryuseRecordId) {
                        finshMidRequestBean.id = useRecordId;
                    }
                    if (selectDoctor.contains("请选择医生")) {
                        ToastUtils.show("请选择医生!");
                        return;
                    }
                    finshMidRequestBean.statusDescribe = "个案管理师已完成处理 分配给" + taskDeatailBean.getTaskDetail().getDeptName() + selectDoctor + "医生,开始时间:" + selectTakeTime + ",持续时间:" + selectContinueTime + "分钟";  //描述 添加上科室和医生
                    finshMidRequestBean.taskId = String.valueOf(taskDeatailBean.getId());
                    finshMidRequestBean.userId = String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId());
                    finshMidRequestBean.tradeId = taskDeatailBean.getTaskDetail().getTradeId();
                    finshMidRequestBean.remark = selectContinueTime;
                    mPresenter.finishMidRequest(finshMidRequestBean);
                    String jsonString = GsonUtils.ModelToJson(finshMidRequestBean);
                    Log.e(TAG, "jsonString1111: " + jsonString);
                    sendSystemRemind(jsonString);
                } else {
                    Log.e(TAG, "重复点击!!!");
                }

            }

            @Override
            public void onNegtiveClick() {
                useEquityDialog.dismiss();
            }
        }).show();
    }


    private void initCompView() {
        btn_processing_complete.setVisibility(taskDeatailBean.getExecFlag() == 1 ? View.GONE : View.VISIBLE); //如果是已办 则不显示处理完成按钮shape_cancel_
        btn_processing_complete.setBackground((taskDeatailBean.getTaskDetail().getUploadDocFlag() == 1) ? homeActivity.getDrawable(R.drawable.shape_cancel_) : homeActivity.getDrawable(R.drawable.shape_comfirm_sele)); //@drawable/shape_cancel_
        tv_goChat.setVisibility(taskDeatailBean.getExecFlag() == 1 ? View.INVISIBLE : View.VISIBLE); //如果是已办 则不显示进入聊天按钮
        iv_icon.setImageDrawable(taskDeatailBean.getTaskDetail().getUserInfo().getUserSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
        tv_sex.setText(taskDeatailBean.getTaskDetail().getUserInfo().getUserSex());
        tv_name.setText(taskDeatailBean.getTaskDetail().getUserInfo().getUserName());
//        tv_data_review.setVisibility((taskDeatailBean.getTaskDetail().getRightsType().equalsIgnoreCase(Constants.RIGTH_TYPE) && taskDeatailBean.getTaskDetail().getUploadDocFlag() == 1) ? View.VISIBLE : View.INVISIBLE); //如果是重症科，并且需要审核资料则 显示资料审核
        tv_data_review.setVisibility(taskDeatailBean.getTaskDetail().getUploadDocFlag() == 1 ? View.VISIBLE : View.INVISIBLE); //如果是需要审核资料则 显示资料审核
        tv_age.setText(taskDeatailBean.getTaskDetail().getUserInfo().getUserAge() + "岁");
        tv_phoneNumber.setText(taskDeatailBean.getTaskDetail().getUserInfo().getPhone());
        useEquityDialog = new UseEquityDialog(homeActivity);
    }


    //权益使用记录
    private void initrightUseTimeRecord() {
        list_righttimes.setLayoutManager(new LinearLayoutManager(homeActivity));
        rightUseTimeRecordAdapter = new RightUseTimeRecordAdapter(R.layout.item_right_record_layout, userGoodsAttrs);
        list_righttimes.setAdapter(rightUseTimeRecordAdapter);
    }

    //权益流转列表
    private void initRightsRecord() {
        list_technological_process.setLayoutManager(new LinearLayoutManager(homeActivity));
        queryRightsRecordAdapter = new QueryRightsRecordAdapter(R.layout.item_processing_flow_layout, useRecordList);
        list_technological_process.setAdapter(queryRightsRecordAdapter);
    }


    //发送消息提醒
    private void sendSystemRemind(String infoDetailJsonString) {
        SystemRemindObj systemRemindObj = new SystemRemindObj();
        systemRemindObj.remindType = "videoRemind";
        systemRemindObj.eventType = 4;
        systemRemindObj.infoDetail = infoDetailJsonString;
        systemRemindObj.userId = String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId());
        EasyHttp.post(homeActivity).api(systemRemindObj).request(new OnHttpListener<ApiResult<String>>() {
            @Override
            public void onSucceed(ApiResult<String> result) {
                Log.e(TAG, "通知请求: " + result.getMessage());
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }


    /**
     * 审核通过 更新 任务分派按钮 视图 可允许点击 及 变更背景
     *
     * @param updateView
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventHandler(RefreshViewUseApply updateView) {//不用区分类型，全部直接转换成json发送消息出去
        isDistributable = true;
        taskDeatailBean.isShowBottomBuntton = false;   //已经审核过了  重新再点进去界面 不需再显示下面的 按钮
        btn_processing_complete.setBackground(homeActivity.getDrawable(R.drawable.shape_button_select));
    }


    @Override
    public void initData() {
        super.initData();
        mPresenter.getMyRight(0, String.valueOf(taskDeatailBean.getTaskDetail().getRightsId()), String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));  //调试 userID用219  其他参数不传
        getRightsRecord();

    }


    private void getRightsRecord() {
        mPresenter.queryRightsRecord(1, 1000, taskDeatailBean.getTaskDetail().getRightsId(), String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    /**
     * 查询我的权益成功回调
     *
     * @param myRightBeanList
     */
    @Override
    public void getMyRightSuccess(List<MyRightBean> myRightBeanList) {
        homeActivity.runOnUiThread(() -> {
            if (myRightBeanList != null && myRightBeanList.size() > 0) {
                ll_hasRightsData.setVisibility(View.VISIBLE);
                rl_default_view.setVisibility(View.GONE);
                rightBeanList = myRightBeanList;
                MyRightBean myRightBean = rightBeanList.get(0);
                rigthDepatCode = myRightBean.getBelong();
                mPresenter.getDocList(myRightBean.getBelong()); //请求获取医生
                tv_tcmc.setText(myRightBean.getGoodsName());
                tv_serName.setText(myRightBean.getGoodsSpec());
                tv_validTime.setText(TimeUtils.getTime_(myRightBean.getEndTime()));
                if ((myRightBean.getUserGoodsAttr() == null || myRightBean.getUserGoodsAttr().size() == 0)) {
                    ll_right_record.setVisibility(View.GONE);
                } else {
                    ll_right_record.setVisibility(View.VISIBLE);
                    rightUseTimeRecordAdapter.setNewData(myRightBean.getUserGoodsAttr());
                }
            } else {
                ll_hasRightsData.setVisibility(View.GONE);
                rl_default_view.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 查询我的权益失败回调
     *
     * @param faiMessage
     */
    @Override
    public void getMyRightFail(String faiMessage) {
        homeActivity.runOnUiThread(() -> {
//            ToastUtils.show(faiMessage);
        });
    }


    /***
     * 查询权益使用记录 成功回调
     * @param queryRightsRecordBean
     */
    @Override
    public void queryRightsRecordSuccess(QueryRightsRecordBean queryRightsRecordBean) {
        homeActivity.runOnUiThread(() -> {
            if (queryRightsRecordBean != null && queryRightsRecordBean.getRows().size() > 0) {
                useRecordList = queryRightsRecordBean.getRows();
                if (useRecordList != null && useRecordList.size() > 0 && taskDeatailBean.getExecFlag() == 1 && useRecordList.get(0).getExecFlag() == 2) {
                    useRecordId = useRecordList.get(0).getId();
                    tv_reset.setVisibility(View.VISIBLE);
                }
                queryRightsRecordAdapter.setNewData(useRecordList);
            } else {
//                ll_hasRightsData.setVisibility(View.GONE);
//                rl_default_view.setVisibility(View.VISIBLE);
            }
        });
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
     * 个案管理师 完成申请处理 成功回调
     *
     * @param finshMidRequestBean
     */
    @Override
    public void finishMidRequestSuccess(FinshMidRequestBean finshMidRequestBean) {
        homeActivity.runOnUiThread(() -> {
            if (useEquityDialog != null) {
                useEquityDialog.dismiss();
            }
            if (flashRecordRigth) {   //如果是个案师重新设置的  设置提交成功后 需要更新一下权益记录列表 但不需要更新待办列表
                getRightsRecord();
            } else {
                EventBus.getDefault().post(new NotifyactionObj());
            }

            if (btn_processing_complete.getVisibility() == View.VISIBLE) {
                btn_processing_complete.setVisibility(View.GONE); //这里如果完成成功了 则隐藏 申请完成处理 按钮，不能让一直点
            }
            if (tv_goChat.getVisibility() == View.VISIBLE) {
                tv_goChat.setVisibility(View.GONE); //这里如果完成成功了 则隐藏 进入聊天 按钮，不能让一直点
            }

            if (tv_data_review.getVisibility() == View.VISIBLE) {  //任务分配完成了 隐藏资料审核按钮
                tv_data_review.setVisibility(View.GONE);
            }

            ToastUtils.show("处理成功!");
        });


    }


    /***
     * 个案管理师 完成申请处理 失败回调
     * @param messageFail
     */
    @Override
    public void finishMidRequestFail(String messageFail) {
        homeActivity.runOnUiThread(() -> ToastUtils.show("处理失败:" + messageFail));
    }

    /**
     * 根据科室ID 查询医生成功回调
     *
     * @param docListBeans
     */
    @Override
    public void getDocListSuccess(List<DocListBean> docListBeans) {
        if (useEquityDialog != null) {
            if (docListBeans != null && docListBeans.size() > 0) {
                useEquityDialog.setDocList(docListBeans);
            } else {
                useEquityDialog.setDocList(null);

            }
        }

    }


    /**
     * 根据科室ID 查询医生失败回调
     *
     * @param faileMessage
     */
    @Override
    public void getDocListFail(String faileMessage) {

    }


    /**
     * 获取审核记录
     *
     * @param responseList
     */

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void getDataReviewRecordSuccess(List<DataReViewRecordResponse> responseList) {
        homeActivity.runOnUiThread(() -> {
            if (responseList != null && responseList.size() > 0) {
                //如果最新的 审核记录 是通过 则任务分配按钮可点击
                if (!EmptyUtil.isEmpty(responseList.get(0).getDealResult())){
                    if (responseList.get(0).getDealResult().equals("审核通过")) {
                        isDistributable = true;
                        taskDeatailBean.isShowBottomBuntton = false;  //不显示底部 按钮
                        btn_processing_complete.setBackground(getActivity().getDrawable(R.drawable.shape_comfirm_sele));
                    }
                }

            }


        });
    }

    @Override
    public void getDataReviewRecordFail(String messageFail) {

    }
}
