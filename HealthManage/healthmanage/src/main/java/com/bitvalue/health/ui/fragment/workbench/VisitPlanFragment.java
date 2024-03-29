package com.bitvalue.health.ui.fragment.workbench;

import static com.bitvalue.health.util.DataUtil.isNumeric;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.api.eventbusbean.VisitPlanRefreshObj;
import com.bitvalue.health.api.requestbean.AllocatedPatientRequest;
import com.bitvalue.health.api.requestbean.RequestPlanPatientListBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.callback.PhoneFollowupCliclistener;
import com.bitvalue.health.contract.visitplancontact.VisitPlanContact;
import com.bitvalue.health.presenter.visitplanpersenter.VisitPlanPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.HealthPlanListAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.health.util.layout.XLinearLayoutManager;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.helper.CustomMessage;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.bitvalue.sdk.collab.modules.message.MessageInfoUtil;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author created by bitvalue
 * @data : 11/10
 * <p>
 * 随访计划Fragment
 */
public class VisitPlanFragment extends BaseFragment<VisitPlanPersenter> implements VisitPlanContact.VisitPlanView, PhoneFollowupCliclistener {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.rl_undistribution_refresh)
    SmartRefreshLayout AllsmartRefreshLayout;  //上下拉刷新控件
    @BindView(R.id.list_patient_dynamic)
    WrapRecyclerView list_dynamic;  //患者动态 list

    @BindView(R.id.layout_search_result)
    SmartRefreshLayout searchsmartRefreshLayout;  //搜索上下拉刷新控件
    @BindView(R.id.search_allpatient)
    WrapRecyclerView search_recyclerView;  //患者动态 list

    @BindView(R.id.rl_default_view)
    RelativeLayout default_view;

    @BindView(R.id.et_search)
    EditText ed_search;

    //    @BindView(R.id.framelayout)
//    FrameLayout framelayout;
    private HomeActivity homeActivity;
    private RequestPlanPatientListBean allocatedPatientRequest = new RequestPlanPatientListBean();
    private HealthPlanListAdapter healthPlanListAdapter;
    private HealthPlanListAdapter search_patientAdapter;

    private List<NewLeaveBean.RowsDTO> allDynamicList = new ArrayList<>(); //随访计划 数据
    private List<NewLeaveBean.RowsDTO> searchPatientList = new ArrayList<>(); // 随访计划搜索数据


    private int cureentPage = 0;
    private int pageNo = 1;
    private final int pageSize = 30;
    private int searchPageNo = 1;


    //初始化当前Fragment的实例
    public static VisitPlanFragment getInstance(boolean is_need_toast) {
        VisitPlanFragment workbenchFragment = new VisitPlanFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_toast", is_need_toast);
        workbenchFragment.setArguments(bundle);
        return workbenchFragment;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {     //可见的时候 请求
            requestData("");
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        tv_title.setText("随访计划");
        img_back.setVisibility(View.GONE);
        list_dynamic.setLayoutManager(new XLinearLayoutManager(homeActivity, LinearLayoutManager.VERTICAL, false));
        search_recyclerView.setLayoutManager(new XLinearLayoutManager(homeActivity, LinearLayoutManager.VERTICAL, false));

        initList();
        initSearchButton();
        initSearchList();
        EventBus.getDefault().register(this);

    }


    //初始化搜索控件 并 设置监听
    private void initSearchButton() {
        ed_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (ed_search.getText().toString().isEmpty()) {

                    ToastUtil.toastShortMessage("请输入搜索内容");
                    return true;
                }

                //关闭软键盘
                hideKeyboard(ed_search);
                requestData(ed_search.getText().toString());
                return true;
            }
            return false;
        });

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    AllsmartRefreshLayout.setVisibility(View.VISIBLE);
                    searchsmartRefreshLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    private void initList() {
        healthPlanListAdapter = new HealthPlanListAdapter(homeActivity);
        list_dynamic.setAdapter(healthPlanListAdapter);
        healthPlanListAdapter.setItemHeadOnclickListenner(userId -> {
            homeActivity.switchSecondFragment(Constants.DATA_REVIEW, userId);
        });
        healthPlanListAdapter.setOnPlanTaskItemClickListener((planId, rowsDTO) -> {
            rowsDTO.planId = planId;
            homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN_PREVIEW, rowsDTO);
        });


        //        上下拉刷新 最外层的
        AllsmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            //上拉刷新
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                // TODO: 2021/12/8 加载下一页
                if (cureentPage == pageNo) {
                    pageNo = 1;
                    Log.e(TAG, "无更多数据");
                    AllsmartRefreshLayout.finishLoadMore();
                    AllsmartRefreshLayout.finishRefresh();
                    return;
                }
                pageNo++;
                requestData("");
                AllsmartRefreshLayout.finishLoadMore();
                AllsmartRefreshLayout.finishRefresh();
            }

            //下拉刷新
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                if (pageNo > 1) {
                    pageNo--;
                } else {
                    AllsmartRefreshLayout.finishRefresh();
                    return;
                }

//                allDynamicList.clear();
                requestData("");
                AllsmartRefreshLayout.finishRefresh();

            }
        });
    }


    private void initSearchList() {
        search_patientAdapter = new HealthPlanListAdapter(homeActivity);
        search_recyclerView.setAdapter(search_patientAdapter);
        healthPlanListAdapter.setItemHeadOnclickListenner(userID -> {
            homeActivity.switchSecondFragment(Constants.DATA_REVIEW, userID);
        });
        search_patientAdapter.setOnPlanTaskItemClickListener((planId, rowsDTO) -> {
            rowsDTO.planId = planId;
            homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN_PREVIEW, rowsDTO);
        });

        //        上下拉刷新 最外层的
        searchsmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            //上拉刷新
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                // TODO: 2021/12/8 加载下一页
                if (cureentPage == searchPageNo) {
                    searchPageNo = 1;
                    Log.e(TAG, "无更多数据");
                    searchsmartRefreshLayout.finishLoadMore();
                    searchsmartRefreshLayout.finishRefresh();
                    return;
                }
                searchPageNo++;
                requestData(ed_search.getText().toString());
                searchsmartRefreshLayout.finishLoadMore();
                searchsmartRefreshLayout.finishRefresh();
            }

            //下拉刷新
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                if (searchPageNo > 1) {
                    searchPageNo--;
                } else {
                    searchsmartRefreshLayout.finishRefresh();
                    return;
                }

                searchPatientList.clear();
                requestData(ed_search.getText().toString());
                searchsmartRefreshLayout.finishRefresh();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        requestData("");
    }


    private void requestData(String name) {
//        showLoading();
        allocatedPatientRequest.userName = name;
        if (!EmptyUtil.isEmpty(name)) {
            allocatedPatientRequest.pageNo = 1;
        } else {
            allocatedPatientRequest.pageNo = pageNo;
        }
        allocatedPatientRequest.pageSize = pageSize;
        if (!EmptyUtil.isEmpty(name)) {
            mPresenter.qryPatientByName(allocatedPatientRequest);
        } else {
            mPresenter.qryPatientList(allocatedPatientRequest);
        }
    }


    @Override
    protected VisitPlanPersenter createPresenter() {
        return new VisitPlanPersenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_workbench;
    }


    @Override
    public void phoneNumberCallback(String phoneNumber) {
        if (isNumeric(phoneNumber)) {
            callPhone(phoneNumber);
        } else {
            ToastUtils.show("非法号码!");
        }
    }


    /**
     * 处理结束计划成功后 更新随访计划列表
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventHandler(VisitPlanRefreshObj message) {//不用区分类型，全部直接转换成json发送消息出去
        Log.e(TAG, "接收更新随访计划列表消息");
        allDynamicList.clear();
        pageNo = 1;
        requestData("");
    }


    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void qryPatientListSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList) {
        getActivity().runOnUiThread(() -> {
            //拿到所有患者 需要过滤一些没有userID的患者
            hideDialog();
            if (pageNo > 1 && infoDetailDTOList != null && infoDetailDTOList.size() == 0) {
                cureentPage = pageNo;
                return;
            }
            allDynamicList.clear();
            if (infoDetailDTOList != null) {
                allDynamicList = infoDetailDTOList;
                default_view.setVisibility(infoDetailDTOList.size() == 0 ? View.VISIBLE : View.GONE);
            }
            healthPlanListAdapter.setNewData(allDynamicList);

        });
    }

    @Override
    public void qryPatientListFail(String messageFail) {
        getActivity().runOnUiThread(() -> {
            hideDialog();
//            ToastUtils.show(messageFail);
        });
    }

    @Override
    public void qryPatientByNameSuccess(List<NewLeaveBean.RowsDTO> initinfoDetailDTOList) {
        homeActivity.runOnUiThread(() -> {
            hideDialog();
            searchPatientList.clear();
            searchPatientList.addAll(initinfoDetailDTOList);
            if (null == searchPatientList || searchPatientList.size() == 0) {
                cureentPage = searchPageNo;
                searchsmartRefreshLayout.setVisibility(View.GONE);
                AllsmartRefreshLayout.setVisibility(View.VISIBLE);
            } else {
                searchsmartRefreshLayout.setVisibility(View.VISIBLE);
                AllsmartRefreshLayout.setVisibility(View.GONE);
                search_patientAdapter.setNewData(searchPatientList);
            }

        });
    }

    @Override
    public void qryPatientByNameFail(String failmessage) {
        homeActivity.runOnUiThread(() -> {
            hideDialog();
//            ToastUtils.show(failmessage);
        });
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
