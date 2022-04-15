package com.bitvalue.health.ui.fragment.healthmanage;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.bitvalue.health.util.Constants.TASKDETAIL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.eventbusbean.RefreshViewUseApply;
import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.requestbean.filemodel.SystemRemindObj;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.TaskDeatailBean;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.MoreDataDetailContract;
import com.bitvalue.health.presenter.healthmanager.MoreDataDetailPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.DataReViewRecordAdapter;
import com.bitvalue.health.ui.adapter.ImageListDisplayAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.GsonUtils;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.customview.ReasonDialog;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 04/12
 * <p>
 * 资料审核界面
 */
public class DataReviemFragment extends BaseFragment<MoreDataDetailPresenter> implements MoreDataDetailContract.View {

    @BindView(R.id.rl_back)
    RelativeLayout layout_back; //返回
    @BindView(R.id.tv_patient_name)
    TextView tv_patient_name; //就诊人
    @BindView(R.id.tv_phone)
    TextView tv_phone; // 电话
    @BindView(R.id.tv_patient_sex)
    TextView tv_sex; //性别
    @BindView(R.id.tv_patent_age)
    TextView tv_age; //年龄
    @BindView(R.id.iv_patient_icon)
    ImageView img_icon;//头像
    @BindView(R.id.btn_fail)
    TextView btn_notPass;
    @BindView(R.id.btn_pass)
    TextView btn_pass;


    @BindView(R.id.list_condition_profile)
    WrapRecyclerView list_condition_profile;  //病情简介

    @BindView(R.id.list_lately_test_results)
    WrapRecyclerView list_lately_test_results;  //最近的化验结果

    @BindView(R.id.list_lately_imagedata)
    WrapRecyclerView list_lately_imagedata;  //最近的X线

    @BindView(R.id.list_lately_ct)
    WrapRecyclerView list_lately_ct;  //最近的CT

    @BindView(R.id.list_lately_mri)
    WrapRecyclerView list_lately_mri;  //最近的MRI


    @BindView(R.id.list_datarevicew_recordlist)
    WrapRecyclerView list_datarevicew_recordlist;  //审核记录

    @BindView(R.id.ll_bottom_button)
    LinearLayout ll_bottom_button;


    private HomeActivity homeActivity;
    private ImageListDisplayAdapter condition_profile_Adapter;
    private ImageListDisplayAdapter lately_test_results_Adapter;
    private ImageListDisplayAdapter lately_imagedata_Adapter;
    private ImageListDisplayAdapter lately_ct_Adapter;
    private ImageListDisplayAdapter lately_mri_Adapter;
    private DataReViewRecordAdapter dataReViewRecordAdapter;  //审核记录adapter
    private TaskDeatailBean taskDeatailBean;
    private List<String> BQJJurlList = new ArrayList<>();
    private List<String> HYJGurlList = new ArrayList<>();
    private List<String> XGurlList = new ArrayList<>();
    private List<String> CTurlList = new ArrayList<>();
    private List<String> MRIurlList = new ArrayList<>();
    private List<DataReViewRecordResponse> dataReViewRecordList = new ArrayList<>(); //审核记录列表
    private ReasonDialog reasonDialog;
    private LoginBean loginBean;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    protected MoreDataDetailPresenter createPresenter() {
        return new MoreDataDetailPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_data_review_layout;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        taskDeatailBean = (TaskDeatailBean) getArguments().getSerializable(TASKDETAIL);
        if (taskDeatailBean == null || taskDeatailBean.getTaskDetail() == null || taskDeatailBean.getTaskDetail().getUserInfo() == null) {
            return;
        }
        loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
        UserLocalVisitBean userLocalVisitBean = new UserLocalVisitBean();
        userLocalVisitBean.userId = taskDeatailBean.getTaskDetail().getUserInfo().getUserId() + "";
        userLocalVisitBean.contentId = taskDeatailBean.getTaskDetail().getTradeId();
        mPresenter.qryUserLocalVisit(userLocalVisitBean);
        mPresenter.getDataReviewRecord(taskDeatailBean.getTaskDetail().getTradeId(), taskDeatailBean.getTaskDetail().getUserInfo().getUserId() + "");
        initView();
        reasonDialog = new ReasonDialog(homeActivity);

    }


    @OnClick({R.id.btn_fail, R.id.btn_pass})
    public void OnClickBtn(View view) {
        switch (view.getId()) {
            //审核不通过
            case R.id.btn_fail:
                if (reasonDialog != null) {
                    reasonDialog.setOnclickListener(new ReasonDialog.OnClickBottomListener() {
                        @Override
                        public void onPositiveClick() {
                            String inputString = reasonDialog.getInputString();
                            if (EmptyUtil.isEmpty(inputString)){
                                ToastUtils.show("阐述原因不能为空!");
                                return;
                            }
                            examineResult(false, inputString);  //请求接口 保存审核结果

                        }

                        @Override
                        public void onNegtiveClick() {
                            reasonDialog.cancel();
                        }
                    }).show();

                    reasonDialog.showKeyboard();
                }


                break;
            //审核通过
            case R.id.btn_pass:
                examineResult(true, "");
                break;
        }

    }


    private void initView() {
        ll_bottom_button.setVisibility(taskDeatailBean.isShowBottomBuntton ? View.VISIBLE : View.GONE);
        layout_back.setVisibility(View.VISIBLE);
        layout_back.setOnClickListener(v -> {
            if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                homeActivity.getSupportFragmentManager().popBackStack();
            }
        });
        tv_patient_name.setText(taskDeatailBean.getTaskDetail().getUserInfo().getUserName());
        tv_phone.setText(taskDeatailBean.getTaskDetail().getUserInfo().getPhone());
        tv_sex.setText(taskDeatailBean.getTaskDetail().getUserInfo().getUserSex());
        tv_age.setText(taskDeatailBean.getTaskDetail().getUserInfo().getUserAge()+"岁");
        img_icon.setImageDrawable(taskDeatailBean.getTaskDetail().getUserInfo().getUserSex().equals("男") ? Application.instance().getResources().getDrawable(R.drawable.head_male) : Application.instance().getResources().getDrawable(R.drawable.head_female));
        LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        list_condition_profile.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(homeActivity);
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        list_lately_test_results.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(homeActivity);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        list_lately_imagedata.setLayoutManager(layoutManager2);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(homeActivity);
        layoutManager3.setOrientation(RecyclerView.HORIZONTAL);
        list_lately_ct.setLayoutManager(layoutManager3);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(homeActivity);
        layoutManager4.setOrientation(RecyclerView.HORIZONTAL);
        list_lately_mri.setLayoutManager(layoutManager4);


        LinearLayoutManager datareviewrecordlayoutManager = new LinearLayoutManager(homeActivity);
        list_datarevicew_recordlist.setLayoutManager(datareviewrecordlayoutManager);
        dataReViewRecordAdapter = new DataReViewRecordAdapter(R.layout.item_datarevicview_layout, dataReViewRecordList);
        list_datarevicew_recordlist.setAdapter(dataReViewRecordAdapter);

        condition_profile_Adapter = new ImageListDisplayAdapter(R.layout.item_imagevisitlod_layout, BQJJurlList, homeActivity);
        list_condition_profile.setAdapter(condition_profile_Adapter);

        lately_test_results_Adapter = new ImageListDisplayAdapter(R.layout.item_imagevisitlod_layout, HYJGurlList, homeActivity);
        list_lately_test_results.setAdapter(lately_test_results_Adapter);


        lately_imagedata_Adapter = new ImageListDisplayAdapter(R.layout.item_imagevisitlod_layout, XGurlList, homeActivity);
        list_lately_imagedata.setAdapter(lately_imagedata_Adapter);

        lately_ct_Adapter = new ImageListDisplayAdapter(R.layout.item_imagevisitlod_layout, CTurlList, homeActivity);
        list_lately_ct.setAdapter(lately_ct_Adapter);

        lately_mri_Adapter = new ImageListDisplayAdapter(R.layout.item_imagevisitlod_layout, MRIurlList, homeActivity);
        list_lately_mri.setAdapter(lately_mri_Adapter);


    }


    //保存审核结果
    private void examineResult(boolean isPass, String detail) {
        DataReViewRecordResponse request = new DataReViewRecordResponse();
        request.setDealDetail(isPass ? "审核通过" : "审核不通过!\n描述:" + detail);
        request.setDealResult(isPass ? "审核通过" : "审核不通过");
        request.setDealType("资料审核");
        request.setUserId(taskDeatailBean.getTaskDetail().getUserInfo().getUserId() + "");
        request.setDealUserName(loginBean.getUser().user.userName);
        request.setDealUser(loginBean.getUser().user.userId + "");
        request.setTradeId(taskDeatailBean.getTaskDetail().getTradeId());
        mPresenter.saveDataReviewRecord(request);
        if (!isPass) //审核不通过   发消息  通过 不发
        sendSystemRemind(GsonUtils.ModelToJson(request)); //消息通知
    }


    //资料审核不通过 需要发消息通知
    private void sendSystemRemind(String jsonString) {
        SystemRemindObj systemRemindObj = new SystemRemindObj();
        systemRemindObj.remindType = "videoRemind";
        systemRemindObj.eventType = 7;//  审核不通过
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
    public void qryUserVisitSuccess(List<TaskDetailBean> imageList) {
        homeActivity.runOnUiThread(() -> {
            if (imageList != null && imageList.size() > 0) {
                for (int i = 0; i < imageList.size(); i++) {
//                    Log.e(TAG, "类型: " + imageList.get(i).getVisitType() + "  集合大小:" + imageList.get(i).getHealthImages().size());
                    List<TaskDetailBean.HealthImagesDTO> URLlList = imageList.get(i).getHealthImages();
                    switch (imageList.get(i).getVisitType()) {
                        case "BQJJ":
                            for (TaskDetailBean.HealthImagesDTO bqjjUrlList : URLlList) {
                                BQJJurlList.add(bqjjUrlList.getFileUrl());
                            }
                            break;
                        case "HYJG":
                            for (TaskDetailBean.HealthImagesDTO hyjgUrlList : URLlList) {
                                HYJGurlList.add(hyjgUrlList.getFileUrl());
                            }
                            break;
                        case "XG":
                            for (TaskDetailBean.HealthImagesDTO xgUrlList : URLlList) {
                                XGurlList.add(xgUrlList.getFileUrl());
                            }
                            break;
                        case "CT":
                            for (TaskDetailBean.HealthImagesDTO ctUrlList : URLlList) {
                                CTurlList.add(ctUrlList.getFileUrl());
                            }
                            break;
                        case "MRI":
                            for (TaskDetailBean.HealthImagesDTO ctUrlList : URLlList) {
                                MRIurlList.add(ctUrlList.getFileUrl());
                            }
                            break;
                    }
                }

                condition_profile_Adapter.setNewData(BQJJurlList);
                lately_test_results_Adapter.setNewData(HYJGurlList);
                lately_imagedata_Adapter.setNewData(XGurlList);
                lately_ct_Adapter.setNewData(CTurlList);
                lately_mri_Adapter.setNewData(MRIurlList);

            }
        });

    }

    @Override
    public void qryUserVisitFail(String failMessage) {

    }


    /**
     * 获取审核记录成功回调
     *
     * @param responseList
     */
    @Override
    public void getDataReviewRecordSuccess(List<DataReViewRecordResponse> responseList) {
        homeActivity.runOnUiThread(() -> {
            dataReViewRecordList = responseList;
            dataReViewRecordAdapter.setNewData(dataReViewRecordList);
        });
    }


    /**
     * 获取审核记录失败回调
     *
     * @param messageFail
     */
    @Override
    public void getDataReviewRecordFail(String messageFail) {

    }


    /**
     * 保存审核资料 成功回调
     *
     * @param reViewRecordResponse
     */
    @Override
    public void saveDataReviewRecordSuucess(DataReViewRecordResponse reViewRecordResponse) {
        homeActivity.runOnUiThread(() -> {
            if (reasonDialog != null) {
                reasonDialog.cancel();
            }
            ToastUtils.show("操作成功!");
            if (reViewRecordResponse.getDealResult().equals("审核通过")) {
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {  //退出当前界面
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                // TODO: 2022/4/14 通知权益申请界面  更改视图
                EventBus.getDefault().post(new RefreshViewUseApply());
            }
        });
    }

    /**
     * 保存审核资料 失败回调
     *
     * @param messageFail
     */
    @Override
    public void saveDataReviewRecordFail(String messageFail) {

    }


}
