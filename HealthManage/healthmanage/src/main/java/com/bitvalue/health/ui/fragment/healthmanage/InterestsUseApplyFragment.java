package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.TASKDETAIL;

import android.content.Context;
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
import com.bitvalue.health.api.requestbean.DocListBean;
import com.bitvalue.health.api.requestbean.FinshMidRequestBean;
import com.bitvalue.health.api.requestbean.filemodel.SystemRemindObj;
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
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.customview.UseEquityDialog;
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


    @Override
    protected RightApplyUsePresenter createPresenter() {
        return new RightApplyUsePresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_interests_use_apply;
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
        taskDeatailBean = (TaskDeatailBean) getArguments().getSerializable(TASKDETAIL);
        if (taskDeatailBean == null) {
            return;
        }

        /**
         * 点击查看详情
         */
        tv_detail.setOnClickListener(v -> {
            homeActivity.switchSecondFragment(Constants.FRAGMENT_DETAIL, taskDeatailBean.getTaskDetail().getUserInfo().getUserId());
        });

        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
        list_righttimes.setLayoutManager(new LinearLayoutManager(homeActivity));
        rightUseTimeRecordAdapter = new RightUseTimeRecordAdapter(R.layout.item_right_record_layout, userGoodsAttrs);
        list_righttimes.setAdapter(rightUseTimeRecordAdapter);


        list_technological_process.setLayoutManager(new LinearLayoutManager(homeActivity));
        queryRightsRecordAdapter = new QueryRightsRecordAdapter(R.layout.item_processing_flow_layout, useRecordList);
        list_technological_process.setAdapter(queryRightsRecordAdapter);

        btn_processing_complete.setVisibility(taskDeatailBean.getExecFlag() == 1 ? View.GONE : View.VISIBLE); //如果是已办 则不显示处理完成按钮
        tv_goChat.setVisibility(taskDeatailBean.getExecFlag() == 1 ? View.GONE : View.VISIBLE); //如果是已办 则不显示进入聊天按钮
        iv_icon.setImageDrawable(taskDeatailBean.getTaskDetail().getUserInfo().getUserSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
        tv_sex.setText(taskDeatailBean.getTaskDetail().getUserInfo().getUserSex());
        tv_name.setText(taskDeatailBean.getTaskDetail().getUserInfo().getUserName());
        tv_age.setText(String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserAge())+"岁");
        tv_phoneNumber.setText(taskDeatailBean.getTaskDetail().getUserInfo().getPhone());

        useEquityDialog = new UseEquityDialog(homeActivity);
        btn_processing_complete.setOnClickListener(v -> {
            useEquityDialog.setDepartment(taskDeatailBean.getTaskDetail().getDeptName()).setVisitName(taskDeatailBean.getTaskDetail().getUserInfo().getUserName()).setVisitType(taskDeatailBean.getTaskDetail().getRightsName()).setOnclickListener(new UseEquityDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    FinshMidRequestBean finshMidRequestBean = new FinshMidRequestBean();
                    String selectDoctor = useEquityDialog.getSelectDoc();
                    String selectContinueTime = useEquityDialog.getSelectContinueTime();
                    String selectTakeTime = useEquityDialog.getSelectTakeTime() + ":00";
                    int docUserId = useEquityDialog.getDocUserId();
                    finshMidRequestBean.deptName = loginBean.getUser().user.departmentName;
                    finshMidRequestBean.lastTime = Integer.valueOf(selectContinueTime);
                    finshMidRequestBean.execDept = loginBean.getUser().user.departmentCode; //这里传科室代码
                    finshMidRequestBean.execFlag = 2;
                    finshMidRequestBean.execTime = selectTakeTime;
                    finshMidRequestBean.execUser = String.valueOf(docUserId);    //这里要传医生ID
                    finshMidRequestBean.rightsId = taskDeatailBean.getTaskDetail().getRightsId();
                    finshMidRequestBean.rightsName = taskDeatailBean.getTaskDetail().getRightsName();
                    finshMidRequestBean.rightsType = taskDeatailBean.getTaskDetail().getRightsType();
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

                     sendSystemRemind();

                }

                @Override
                public void onNegtiveClick() {
                    useEquityDialog.cancel();
                }
            }).show();
        });


        tv_goChat.setOnClickListener(v -> {
            // TODO: 2022/3/25 进入聊天界面
            NewLeaveBean.RowsDTO info = new NewLeaveBean.RowsDTO();
            info.setUserName(taskDeatailBean.getTaskDetail().getUserInfo().getUserName());
            info.setUserId(String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));
            info.setKsmc(taskDeatailBean.getTaskDetail().getDeptName());
            homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, info);
        });

    }


    private void sendSystemRemind(){
//        List<String> stringList = SharedPreManager.getStringList(homeActivity);
//        if (stringList != null && stringList.size() > 0) {
//            for (int i = 0; i < stringList.size(); i++) {
//                if (String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()).equals(stringList.get(i))) {
//                    return;
//                }
//            }
//        }
//        SharedPreManager.putStringList(String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));
        SystemRemindObj systemRemindObj = new SystemRemindObj();
        systemRemindObj.remindType = "videoRemind";
        systemRemindObj.userId = String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId());
        EasyHttp.post(homeActivity).api(systemRemindObj).request(new OnHttpListener<ApiResult<String>>() {
            @Override
            public void onSucceed(ApiResult<String> result) {
//                Log.e(TAG, "通知请求: "+result.getMessage() );
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }





    @Override
    public void initData() {
        super.initData();
        mPresenter.getMyRight(0, String.valueOf(taskDeatailBean.getTaskDetail().getRightsId()), String.valueOf(taskDeatailBean.getTaskDetail().getUserInfo().getUserId()));  //调试 userID用219  其他参数不传
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
                mPresenter.getDocList(Integer.valueOf(myRightBean.getBelong())); //请求获取医生
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
                useEquityDialog.cancel();
            }
            btn_processing_complete.setVisibility(View.GONE); //这里如果完成成功了 则隐藏 申请完成处理 按钮，不能让一直点
            tv_goChat.setVisibility(View.GONE); //这里如果完成成功了 则隐藏 进入聊天 按钮，不能让一直点
            EventBus.getDefault().post(new NotifyactionObj());
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
}
