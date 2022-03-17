//package com.bitvalue.health.ui.fragment.healthmanage;
//
//import android.content.Context;
//import android.content.Intent;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
//import com.bitvalue.health.api.responsebean.NewLeaveBean;
//import com.bitvalue.health.base.BaseFragment;
//import com.bitvalue.health.callback.OnItemClick;
//import com.bitvalue.health.contract.healthmanagercontract.NewOutHospitolContract;
//import com.bitvalue.health.presenter.healthmanager.NewOutHospitolPresenter;
//import com.bitvalue.health.ui.activity.HomeActivity;
//import com.bitvalue.health.ui.adapter.NewLyDisCharPatienAdapter;
//import com.bitvalue.health.util.Constants;
//import com.bitvalue.health.util.DensityUtil;
//import com.bitvalue.health.util.EmptyUtil;
//import com.bitvalue.health.util.MUtils;
//import com.bitvalue.health.util.customview.WrapRecyclerView;
//import com.bitvalue.healthmanage.R;
//import com.bitvalue.sdk.collab.utils.ToastUtil;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.hjq.toast.ToastUtils;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//
//import static com.bitvalue.health.util.Constants.USER_ID;
//
///**
// * 新出院患者列表 界面
// *
// * @author created by bitvalue
// * @data : 11/29
// */
//public class NewDischargedFragment extends BaseFragment<NewOutHospitolPresenter> implements NewOutHospitolContract.View, OnItemClick {
//    @BindView(R.id.rl_back)
//    RelativeLayout iv_back; //返回
//    @BindView(R.id.im_search_patient)
//    ImageView iv_search; //搜索按钮
//    @BindView(R.id.et_search)
//    EditText ed_search_patient; //搜索
//    @BindView(R.id.tv_title)
//    TextView tv_title;
//
//    @BindView(R.id.rl_status_refresh)
//    SmartRefreshLayout smartRefreshLayout; //下拉刷新
//    @BindView(R.id.list_newly_discharged_patient)
//    WrapRecyclerView recyclerView_patient; //新出院的所有患者列表
//
//
//    @BindView(R.id.layout_search_result)
//    SmartRefreshLayout smartRe_search_freshLayout; //搜索的下拉列表
//    @BindView(R.id.search_allpatient)
//    WrapRecyclerView rv_search_patientlist;  //搜索出来的患者列表
//
//
//    @BindView(R.id.distribution_plan)
//    TextView distributionPlan;
//    @BindView(R.id.ll_allcheck_layout)
//    LinearLayout ll_allCheck;
//    @BindView(R.id.ck_allcheck)
//    CheckBox ck_allCheck;
//
//
//    private NewLyDisCharPatienAdapter newLyDisCharPatienAdapter; //所有患者adapter
//    private NewLyDisCharPatienAdapter searchDisCharPatienAdapter; //查询adapter
//    private List<NewLeaveBean.RowsDTO> rowsDTOList = new ArrayList<>();  //所有出院患者
//    private List<NewLeaveBean.RowsDTO> searchrowsDTOList = new ArrayList<>();  //查询出院患者
//    private RequestNewLeaveBean requestNewLeaveBean = new RequestNewLeaveBean();
//    private int cureentPage = 0;
//    private int pageNo = 1;
//    private int pageSize = 10;
//    private HomeActivity homeActivity;
//
//
//    private List<NewLeaveBean.RowsDTO> userIDList = new ArrayList<>();
//    private boolean isAllCheck;
//
//    @Override
//    protected NewOutHospitolPresenter createPresenter() {
//        return new NewOutHospitolPresenter();
//    }
//
//    @Override
//    protected int provideContentViewId() {
//        return R.layout.newly_discharged_patients_layout;
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        homeActivity = (HomeActivity) context;
//    }
//
//
//    @Override
//    public void initView(View rootView) {
//        super.initView(rootView);
//        tv_title.setText(getString(R.string.newly_discharged_patients));
//
//        initAllNewlyPatient();
////        initSearchButton();
//        distributionPlan.setOnClickListener(v -> {
//            if (distributionPlan.getText().toString().contains("分配")){
//                ll_allCheck.setVisibility(View.VISIBLE);
//                newLyDisCharPatienAdapter.showCheckBox();
//            }else {
//                ll_allCheck.setVisibility(View.GONE);
//                newLyDisCharPatienAdapter.unAllCheckShowcheck(false);
//                // TODO: 2022/1/14 跳转至分配计划列表 需要携带 useridlist 过去
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_PLAN_LIST,userIDList);
//            }
//            distributionPlan.setText(distributionPlan.getText().toString().contains("分配") ? "下一步" : "分配随访计划");
//
//        });
//    }
//
//
//    private void initAllNewlyPatient() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity);
//        recyclerView_patient.setLayoutManager(layoutManager);
//        recyclerView_patient.addItemDecoration(MUtils.spaceDivider(DensityUtil.dip2px(homeActivity, this.getResources().getDimension(R.dimen.qb_px_3)), false));
//        newLyDisCharPatienAdapter = new NewLyDisCharPatienAdapter(rowsDTOList, this);
//        recyclerView_patient.setAdapter(newLyDisCharPatienAdapter);
//        //        全选
//        ck_allCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            userIDList.clear();
//            Log.e(TAG, "initAllNewlyPatient: "+isChecked );
//            if (isChecked) {
//                isAllCheck = true;
//                newLyDisCharPatienAdapter.AllChecked();
//                for (int i = 0; i < rowsDTOList.size(); i++) {
////                    if (!userIDList.contains(rowsDTOList.get(i).getUserId()) && !EmptyUtil.isEmpty(rowsDTOList.get(i).getUserId())) {
//                        Log.e(TAG, "添加了----");
//                        userIDList.add(rowsDTOList.get(i));
////                    }
//                }
//                Log.e(TAG, "患者ID集合: " + userIDList.size());
//            } else {
//                isAllCheck = false;
//                userIDList.clear();
//                newLyDisCharPatienAdapter.unAllCheckShowcheck(true);
//            }
//        });
//
//
//        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//            //上拉刷新
//            @Override
//            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
//                // TODO: 2021/12/8 加载下一页
//                if (cureentPage == pageNo) {
//                    pageNo = 1;
//                    smartRefreshLayout.finishLoadMore();
//                    smartRefreshLayout.finishRefresh();
//                    return;
//                }
//                pageNo++;
//                requestNewLeaveBean.setPageNo(pageNo);
//                requestNewLeaveBean.setPageSize(pageSize);
//                mPresenter.getAllLeaveHospitolPatients(requestNewLeaveBean);
//                smartRefreshLayout.finishLoadMore();
//                smartRefreshLayout.finishRefresh();
//            }
//
//            //下拉刷新
//            @Override
//            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
//                if (pageNo > 1) {
//                    pageNo--;
//                } else {
//                    smartRefreshLayout.finishRefresh();
//                    return;
//                }
//                rowsDTOList.clear();
//                requestNewLeaveBean.setPageNo(pageNo);
//                requestNewLeaveBean.setPageSize(pageSize);
//                mPresenter.getAllLeaveHospitolPatients(requestNewLeaveBean);
//                smartRefreshLayout.finishRefresh();
//            }
//        });
//
//    }
//
//
//    private void initSearchButton() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity);
//        rv_search_patientlist.setLayoutManager(layoutManager);
//        searchDisCharPatienAdapter = new NewLyDisCharPatienAdapter(searchrowsDTOList, this);
//        rv_search_patientlist.setAdapter(searchDisCharPatienAdapter);
//
//        //        全选
//        ck_allCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            userIDList.clear();
//            if (isChecked) {
//                isAllCheck = true;
//                ck_allCheck.setChecked(isChecked);
//                searchDisCharPatienAdapter.AllChecked();
//                for (int i = 0; i < searchrowsDTOList.size(); i++) {
//                    if (!userIDList.contains(searchrowsDTOList.get(i).getUserId()) && !EmptyUtil.isEmpty(searchrowsDTOList.get(i).getUserId())) {
//                        Log.e(TAG, "添加了11----");
//                        userIDList.add(searchrowsDTOList.get(i));
//                    }
//                }
//                Log.e(TAG, "患者ID集合11: " + userIDList.size());
//            } else {
//                isAllCheck = false;
//                userIDList.clear();
//                searchDisCharPatienAdapter.unAllCheckShowcheck(true);
//            }
//        });
//
//
//
//
//        //上下拉刷新
//        smartRe_search_freshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//
//            //上拉刷新
//            @Override
//            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
//                // TODO: 2021/12/8 加载下一页
//                pageNo++;
//                requestNewLeaveBean.setPageNo(pageNo);
//                requestNewLeaveBean.setPageSize(pageSize);
//                mPresenter.qryPatientByName(requestNewLeaveBean);
//                smartRe_search_freshLayout.finishLoadMore();
//                smartRe_search_freshLayout.finishRefresh();
//            }
//
//            //下拉刷新
//            @Override
//            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
//                if (pageNo > 1) {
//                    pageNo--;
//                }
//                searchrowsDTOList.clear();
//                requestNewLeaveBean.setPageNo(pageNo);
//                requestNewLeaveBean.setPageSize(pageSize);
//                mPresenter.qryPatientByName(requestNewLeaveBean);
//                smartRe_search_freshLayout.finishRefresh();
//            }
//        });
//
//        //搜索
//        ed_search_patient.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                if (ed_search_patient.getText().toString().isEmpty()) {
//
//                    ToastUtil.toastShortMessage("请输入搜索内容");
//                    return true;
//                }
//
//                //关闭软键盘
//                hideKeyboard(ed_search_patient);
//                getSearchPatients();
//                return true;
//            }
//            return false;
//        });
//
//        ed_search_patient.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().isEmpty()) {
//                    smartRefreshLayout.setVisibility(View.VISIBLE);
//                    smartRe_search_freshLayout.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//    }
//
//
//    @Override
//    public void initData() {
//        requestNewLeaveBean.setKeyWord("");
//        requestNewLeaveBean.setPageNo(pageNo);
//        requestNewLeaveBean.setPageSize(pageSize);
//        mPresenter.getAllLeaveHospitolPatients(requestNewLeaveBean);
//    }
//
//
//    private void getSearchPatients() {
//        requestNewLeaveBean.setKeyWord(ed_search_patient.getText().toString());
//        requestNewLeaveBean.setPageNo(pageNo);
//        requestNewLeaveBean.setPageSize(pageSize);
//        mPresenter.qryPatientByName(requestNewLeaveBean);
//    }
//
//
//    @Override
//    public void getAllLeaveHospitolPatientsSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList) {
//        getActivity().runOnUiThread(() -> {
//            if (infoDetailDTOList != null && infoDetailDTOList.size() > 0) {
//                rowsDTOList = infoDetailDTOList;
//                newLyDisCharPatienAdapter.updateList(rowsDTOList);
//                if (ll_allCheck.getVisibility()==View.VISIBLE){
//                    newLyDisCharPatienAdapter.showCheckBox();
//                    if (isAllCheck){
//                        newLyDisCharPatienAdapter.AllChecked();
//                    }
//                }
//
//            } else {
//                cureentPage = pageNo;
//            }
//        });
//
//    }
//
//    @Override
//    public void getAllLeaveHospitolPatientsFail(String failMessage) {
//        getActivity().runOnUiThread(() -> {
//            cureentPage = pageNo;
//            ToastUtil.toastShortMessage(failMessage);
//        });
//    }
//
//
//    /***
//     * 查询成功回调
//     * @param itinfoDetailDTOList
//     */
//    @Override
//    public void qryPatientByNameSuccess(List<NewLeaveBean.RowsDTO> itinfoDetailDTOList) {
//        getActivity().runOnUiThread(() -> {
//            searchrowsDTOList = itinfoDetailDTOList;
//            if (null == searchrowsDTOList || searchrowsDTOList.size() == 0) {
//                ToastUtil.toastShortMessage("未查询到结果");
//                smartRe_search_freshLayout.setVisibility(View.GONE);
//                smartRefreshLayout.setVisibility(View.VISIBLE);
//            } else {
//                smartRe_search_freshLayout.setVisibility(View.VISIBLE);
//                smartRefreshLayout.setVisibility(View.GONE);
//                searchDisCharPatienAdapter.updateList(searchrowsDTOList);
//                if (ll_allCheck.getVisibility()==View.VISIBLE){
//                    searchDisCharPatienAdapter.showCheckBox();
//                    if (isAllCheck){
//                        searchDisCharPatienAdapter.AllChecked();
//                    }
//                }
//            }
//        });
//    }
//
//    @Override
//    public void qryPatientByNameFail(String failmessage) {
//        getActivity().runOnUiThread(() -> ToastUtil.toastShortMessage(failmessage));
//
//    }
//
//    @Override
//    public void onItemClick(Object object, boolean isCheck) {
//        NewLeaveBean.RowsDTO item = (NewLeaveBean.RowsDTO) object;
//        Log.e(TAG, "onItemClick: " + item.getUserName() + " id:" + item.getUserId());
//        if (!EmptyUtil.isEmpty(item.getUserId())) {
//            userIDList.add(item);
//        } else {
//            ToastUtils.show("该患者未注册,暂不可分配随访任务!");
//        }
//    }
//
//}
