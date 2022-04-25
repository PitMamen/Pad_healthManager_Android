package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.ClickUtils.isFastClick;
import static com.bitvalue.health.util.Constants.FRAGMENT_PLAN_LIST;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.eventbusbean.MainRefreshObj;
import com.bitvalue.health.api.requestbean.AllocatedPatientRequest;
import com.bitvalue.health.api.responsebean.DiseaseSpeciesBean;
import com.bitvalue.health.api.responsebean.InpatientBean;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.callback.OnItemClick;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.contract.healthmanagercontract.PatientReportContract;
import com.bitvalue.health.presenter.healthmanager.PatientReportPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.AllPatientAdapter;
import com.bitvalue.health.ui.adapter.AllocatedPatientAdapter;
import com.bitvalue.health.ui.adapter.DiseaseSpeciesAdapter;
import com.bitvalue.health.ui.adapter.InPatientAreaAdapter;
import com.bitvalue.health.ui.adapter.UnRegisterAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.health.util.customview.spinner.EditSpinner;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.blankj.utilcode.util.LogUtils;
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
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 10/27
 * 患者报道 界面
 */
public class PatientReportFragment extends BaseFragment<PatientReportPresenter> implements PatientReportContract.View, OnItemClick, OnItemClickCallback {


    @BindView(R.id.allocated_patient)
    TextView tv_allocated_patient;  //未分配患者
    @BindView(R.id.unregister_patient)
    TextView tv_unregister_patient;  //未注册患者

    @BindView(R.id.sp_bq)
    EditSpinner spinnew_inpatient;

    @BindView(R.id.et_search)
    EditText et_search;


    @BindView(R.id.all_check)
    RelativeLayout rl_allcheck;

    @BindView(R.id.rl_allcontent)
    RelativeLayout rl_contect;


    @BindView(R.id.rl_status_refresh)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.list_newly_discharged_patient)
    WrapRecyclerView recyclerView_all_patient;


    @BindView(R.id.layout_search_result)
    SmartRefreshLayout search_smartRefreshLayout;

    @BindView(R.id.search_allpatient)
    WrapRecyclerView recyclerView_SearchPatient;


    @BindView(R.id.rl_unregister_refresh)
    SmartRefreshLayout unregister_smatRefresh;
    @BindView(R.id.list_unregister)
    WrapRecyclerView unregister_list;


    @BindView(R.id.rl_undistribution)
    RelativeLayout rl_undistribution;
    @BindView(R.id.rl_unregister)
    RelativeLayout rl_unregister;

    @BindView(R.id.ll_all_patient)
    LinearLayout linearLayout_all;
    @BindView(R.id.ll_unregister)
    LinearLayout ll_unregister;
    @BindView(R.id.rl_default_view)
    RelativeLayout default_view;

    private boolean allCheck = false;


    private List<String> inpatientAreaList = null;  //病区
    private String selectedInpatientAreaName = "";//选中的病区名称

    private ArrayAdapter<String> spinnerAdapter;
    private List<NewLeaveBean.RowsDTO> tempPaitentList = new ArrayList<>();
    private List<NewLeaveBean.RowsDTO> rowsDTOList = new ArrayList<>();  //未分配患者集合列表
    private List<NewLeaveBean.RowsDTO> searchrowsDTOList = new ArrayList<>();  //搜索未分配患者集合列表
    private List<NewLeaveBean.RowsDTO> unregisterList = new ArrayList<>();  //未注册患者集合列表
    private AllocatedPatientAdapter allocatedPatientAdapter;
    private AllocatedPatientAdapter searchPatientAdapter;
    private UnRegisterAdapter unRegisterAdapter;

    private AllocatedPatientRequest allocatedPatientRequest = new AllocatedPatientRequest();
    private AllocatedPatientRequest unregisterRequest = new AllocatedPatientRequest();
    private int pageNo = 1;
    private int pageSize = 30;
    private int currentPage = 0;
    private int searchPageNo = 1;
    private String currentUserID = "";

    private HomeActivity homeActivity;


    @Override
    protected PatientReportPresenter createPresenter() {
        return new PatientReportPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_contacts;
    }

    public static PatientReportFragment getInstance(boolean is_need_toast) {
        PatientReportFragment patientReportFragment = new PatientReportFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_toast", is_need_toast);
        patientReportFragment.setArguments(bundle);
        return patientReportFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }


    /**
     * 接收来自随访分配界面 请求接口 刷新待分配和已分配列表
     *
     * @param mainRefreshObj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainRefreshObj mainRefreshObj) {
        // TODO: 2022/1/11 request
        tempPaitentList.clear();
        pageNo = 1; //这里防止分配计划之前有下拉刷新动作 所以分配成功之后重置一下分页
        requestDistribution(et_search.getText().toString());
        requestDistribution("");
    }

    /**
     * 初始化 各个控件 及IM
     *
     * @param rootView
     */
    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        EventBus.getDefault().register(this);
        //获取病区
        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, Application.instance());
        if (null != loginBean) {
            int departmentID = loginBean.getAccount().user.departmentId;
            mPresenter.getInpartientList(String.valueOf(departmentID));//请求获取病区接口
        }
        initallocatedList();
        initUnregisterPatient();
        initSpinnerCon();
        initSearchButton();
        initSearchList();
        if (loginBean.getAccount().roleName.equals("casemanager")) {
            homeActivity.switchSecondFragment(FRAGMENT_PLAN_LIST, "");
        }
        ll_unregister.setVisibility(View.GONE);

    }


    @Override
    public void initData() {
        super.initData();
        requestDistribution("");
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {     //可见的时候 请求未注册 和  未分配患者
            requestDistribution("");
        }
    }


    //初始化未分配患者List
    private void initallocatedList() {
        recyclerView_all_patient.setLayoutManager(new LinearLayoutManager(homeActivity));
        allocatedPatientAdapter = new AllocatedPatientAdapter(rowsDTOList, this, this);
        recyclerView_all_patient.setAdapter(allocatedPatientAdapter);
        //        上下拉刷新 最外层的
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            //上拉刷新
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                // TODO: 2021/12/8 加载下一页
                Log.e(TAG, "上拉: " + pageNo + " currentPage: " + currentPage);
                if (currentPage == pageNo) {
                    pageNo = 1;
                    Log.e(TAG, "无更多数据");
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                    return;
                }
                pageNo++;
                requestDistribution("");
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
            }

            //下拉刷新
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                Log.e(TAG, "下拉: " + pageNo);
                if (pageNo > 1) {
                    pageNo--;
                } else {
                    smartRefreshLayout.finishRefresh();
                    return;
                }

                requestDistribution("");
                smartRefreshLayout.finishRefresh();
            }
        });

    }


    //初始化选择病区spinner控件
    private void initSpinnerCon() {
        spinnew_inpatient.setTextSize(12);
        spinnew_inpatient.setRightImageResource(R.mipmap.down_shixin);
        spinnew_inpatient.setHint(getString(R.string.please_select_inpatient_area));
    }


    //初始化未注册患者List
    private void initUnregisterPatient() {
        unregister_list.setLayoutManager(new LinearLayoutManager(homeActivity));
        unRegisterAdapter = new UnRegisterAdapter(unregisterList, this, this);
        unregister_list.setAdapter(unRegisterAdapter);
    }


    //初始化搜索控件
    private void initSearchList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity);
        recyclerView_SearchPatient.setLayoutManager(layoutManager);
        searchPatientAdapter = new AllocatedPatientAdapter(searchrowsDTOList, this, this);
        recyclerView_SearchPatient.setAdapter(searchPatientAdapter);

        search_smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                // TODO: 2021/12/8 加载下一页
                Log.e(TAG, "下拉刷新: " + searchPageNo);
                if (currentPage == searchPageNo) {
                    searchPageNo = 1;
                    Log.e(TAG, "无更多数据");
                    search_smartRefreshLayout.finishLoadMore();
                    search_smartRefreshLayout.finishRefresh();
                    return;
                }
                searchPageNo++;
                pageNo = searchPageNo;
                requestDistribution(et_search.getText().toString());
                search_smartRefreshLayout.finishLoadMore();
                search_smartRefreshLayout.finishRefresh();


            }

            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                Log.e(TAG, "上拉刷新当前页===: " + searchPageNo);
                if (searchPageNo > 1) {
                    searchPageNo--;
                } else {
                    Log.e(TAG, "不执行加载----");
                    search_smartRefreshLayout.finishRefresh();
                    return;
                }
                pageNo = searchPageNo;
                requestDistribution(et_search.getText().toString());
                search_smartRefreshLayout.finishRefresh();
            }
        });
    }


    private void initSearchButton() {
        //初始化搜索控件 并 设置监听
        et_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (et_search.getText().toString().isEmpty()) {

                    ToastUtil.toastShortMessage("请输入搜索内容");
                    return true;
                }
                isSearchButton = true;
                //关闭软键盘
                hideKeyboard(et_search);
                getSearchPatients();
                return true;
            }
            return false;
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    isSearchButton = false;
                    smartRefreshLayout.setVisibility(View.VISIBLE);
                    search_smartRefreshLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    //根据关键字 查询相关文章(线上获取)
    private void getSearchPatients() {
        String name = et_search.getText().toString();
        if (name.isEmpty()) {
            ToastUtil.toastShortMessage("请输入关键字以查询");
            return;
        }
        //根据关键字
        requestDistribution(name);
    }

    //请求未分配患者
    private void requestDistribution(String name) {
        allocatedPatientRequest.bqmc = selectedInpatientAreaName;   //病区名称
        allocatedPatientRequest.existsPlanFlag = "2";
        allocatedPatientRequest.pageNo = pageNo;
        allocatedPatientRequest.pageSize = pageSize;
        allocatedPatientRequest.userName = name;
        if (!EmptyUtil.isEmpty(name)) {
            allocatedPatientRequest.pageNo = 1;
            mPresenter.qryByNameAllocatedPatienList(allocatedPatientRequest);  //请求 待分配  根据关键字查询
        } else {
            mPresenter.qryAllocatedPatienList(allocatedPatientRequest);  //请求 待分配
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private boolean isSearchButton = false; //区分 所有患者全选 或 搜索出来的患者全选

    @OnClick({R.id.all_check, R.id.rl_undistribution, R.id.rl_unregister})
    public void OnClick(View view) {
        switch (view.getId()) {
            //全选
            case R.id.all_check:
                if (!allCheck) {
                    allCheck = true;
                    rl_allcheck.setBackground(homeActivity.getDrawable(R.drawable.shape_button_select));

                    if (!isSearchButton) {
                        if (allocatedPatientAdapter != null) {
                            allocatedPatientAdapter.AllChecked();
                            tempPaitentList.addAll(rowsDTOList);
                        }
                    } else {
                        if (searchPatientAdapter != null) {
                            searchPatientAdapter.AllChecked();
                            tempPaitentList.addAll(searchrowsDTOList);
                        }
                    }
                } else {
                    allCheck = false;
                    rl_allcheck.setBackground(homeActivity.getDrawable(R.drawable.shape_bg_gray_dark));
                    if (!isSearchButton) {
                        if (allocatedPatientAdapter != null) {
                            allocatedPatientAdapter.unAllCheck();
                            tempPaitentList.removeAll(rowsDTOList);
                        }
                    } else {
                        if (searchPatientAdapter != null) {
                            searchPatientAdapter.unAllCheck();
                            tempPaitentList.removeAll(searchrowsDTOList);
                        }
                    }


                }
                break;

            //待分配
            case R.id.rl_undistribution:
                rl_contect.setVisibility(View.VISIBLE);
                tv_allocated_patient.setTextColor(homeActivity.getColor(R.color.white));
                rl_undistribution.setBackground(homeActivity.getDrawable(R.drawable.shape_button_select));
                tv_unregister_patient.setTextColor(homeActivity.getColor(R.color.black));
                rl_unregister.setBackground(homeActivity.getDrawable(R.drawable.shape_bg_item_unse));

                linearLayout_all.setVisibility(View.VISIBLE);
                ll_unregister.setVisibility(View.GONE);
                default_view.setVisibility(rowsDTOList.size() == 0 ? View.VISIBLE : View.GONE);
                if (allocatedPatientAdapter != null) {
                    allocatedPatientAdapter.updateList(rowsDTOList);
                }
                homeActivity.switchSecondFragment(FRAGMENT_PLAN_LIST, "");


                break;
            //未注册
            case R.id.rl_unregister:
                rl_contect.setVisibility(View.GONE);
                tv_unregister_patient.setTextColor(homeActivity.getColor(R.color.white));
                rl_unregister.setBackground(homeActivity.getDrawable(R.drawable.shape_button_select));
                tv_allocated_patient.setTextColor(homeActivity.getColor(R.color.black));
                rl_undistribution.setBackground(homeActivity.getDrawable(R.drawable.shape_bg_item_unse));
                default_view.setVisibility(unregisterList.size() == 0 ? View.VISIBLE : View.GONE);
                linearLayout_all.setVisibility(View.GONE);
                ll_unregister.setVisibility(View.VISIBLE);
                if (unRegisterAdapter != null) {
                    unRegisterAdapter.updateList(unregisterList);
                }

                break;
        }
    }


    /***
     * 未分配患者成功回调
     * @param infoDetailDTOList
     */
    @Override
    public void qryAllocatedPatienSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList) {
        homeActivity.runOnUiThread(() -> {
//            Log.e(TAG, "qryAllocatedPatienSuccess: " + infoDetailDTOList.size());
            if (pageNo > 1 && null != infoDetailDTOList && infoDetailDTOList.size() == 0) {
                currentPage = pageNo;
                return;
            }

            if (infoDetailDTOList.size() == 0) {
                default_view.setVisibility(View.VISIBLE);
                return;
            } else {
                default_view.setVisibility(View.GONE);
            }
            rowsDTOList = infoDetailDTOList;
            allocatedPatientAdapter.updateList(rowsDTOList);
        });
    }

    /***
     * 未分配患者失败回调
     * @param
     */
    @Override
    public void qryAllocatedPatienFail(String failMessage) {
        homeActivity.runOnUiThread(() -> {
            Log.e(TAG, "未分配查询失败-----" + failMessage);
//            ToastUtils.show(failMessage);
        });
    }


    /**
     * 未注册患者成功回调
     *
     * @param infoDetailDTOList
     */
    @Override
    public void qryUnregisterSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList) {
        homeActivity.runOnUiThread(() -> {
            unregisterList = infoDetailDTOList;
//            default_view.setVisibility(infoDetailDTOList.size() == 0 ? View.VISIBLE : View.GONE);
            unRegisterAdapter.updateList(unregisterList);

        });
    }

    /**
     * 未注册接口失败回调
     *
     * @param messageFail
     */
    @Override
    public void qryUnregisterFail(String messageFail) {
    }


    /**
     * 根据名字搜索 患者成功回调
     *
     * @param infoDetailDTOList
     */
    @Override
    public void qryByNameAllocatedPatienListSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList) {
        homeActivity.runOnUiThread(() -> {
            Log.e(TAG, "qryByNameAllocatedPatienListSuccess-------");
            searchrowsDTOList = infoDetailDTOList;
            if (searchrowsDTOList == null || searchrowsDTOList.size() == 0) {
                currentPage = searchPageNo;
                ToastUtils.show("无相关患者!");
                search_smartRefreshLayout.setVisibility(View.GONE);
                smartRefreshLayout.setVisibility(View.VISIBLE);
            } else {
                search_smartRefreshLayout.setVisibility(View.VISIBLE);
                smartRefreshLayout.setVisibility(View.GONE);
                searchPatientAdapter.updateList(searchrowsDTOList);
            }
        });
    }

    /**
     * 根据名字搜索 患者失败回调
     *
     * @param
     */
    @Override
    public void qryByNameAllocatedPatienListFail(String failMessage) {
        homeActivity.runOnUiThread(() -> {
            currentPage = searchPageNo;
            Log.e(TAG, "qryByNameAllocatedPatienListFail: " + failMessage);
//            ToastUtils.show(failMessage);
        });
    }


    /***
     * 获取病区成功回调
     * @param beanList
     */
    @Override
    public void getInpartientListSuccess(List<InpatientBean> beanList) {
        if (beanList != null && beanList.size() > 0) {
            inpatientAreaList = new ArrayList<>();
            inpatientAreaList.add(getString(R.string.all_inpatient_area));  //这里要默认一个所有病区 第一个 当点中 所有病区 的时候 获取所有病区下的患者
            for (int i = 0; i < beanList.size(); i++) {
                inpatientAreaList.add(beanList.get(i).getInpatientAreaName());
            }
            spinnew_inpatient.setItemData(inpatientAreaList);
            spinnew_inpatient.setOnItemClickListener(name -> {
                selectedInpatientAreaName = name.equals(getString(R.string.all_inpatient_area)) ? "" : name;
                requestDistribution(""); //更新数据
            });
        }
    }

    /***
     * 获取病区失败回调
     * @param faileMessage
     */
    @Override
    public void getInpartientListFail(String faileMessage) {

    }

    @Override
    public void onItemClick(Object object, boolean isCheck) {
        NewLeaveBean.RowsDTO item = (NewLeaveBean.RowsDTO) object;
        if (isCheck) {
            if (!tempPaitentList.contains(item)) {
                tempPaitentList.add(item);
            }
        } else {
            tempPaitentList.remove(item);
        }
        EventBus.getDefault().post(tempPaitentList);

    }



    /***
     * 点击头像切换至患者详情界面回调
     * @param object
     */
    @Override
    public void onItemClick(Object object) {
        NewLeaveBean.RowsDTO item = (NewLeaveBean.RowsDTO) object;
        if (!item.getUserId().equals(currentUserID)) {
            currentUserID = item.getUserId();
            homeActivity.switchSecondFragment(Constants.DATA_REVIEW, item.getUserId());
        }
    }
}
