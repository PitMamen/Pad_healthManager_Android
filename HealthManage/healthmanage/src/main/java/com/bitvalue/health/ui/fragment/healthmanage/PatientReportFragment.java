package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.ClickUtils.isFastClick;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.api.eventbusbean.MainRefreshObj;
import com.bitvalue.health.api.requestbean.AllocatedPatientRequest;
import com.bitvalue.health.api.responsebean.DiseaseSpeciesBean;
import com.bitvalue.health.api.responsebean.InpatientAreaBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.callback.OnItemClick;
import com.bitvalue.health.contract.healthmanagercontract.PatientReportContract;
import com.bitvalue.health.presenter.healthmanager.PatientReportPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.AllocatedPatientAdapter;
import com.bitvalue.health.ui.adapter.DiseaseSpeciesAdapter;
import com.bitvalue.health.ui.adapter.InPatientAreaAdapter;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.hjq.toast.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 10/27
 * 患者报道 界面
 */
public class PatientReportFragment extends BaseFragment<PatientReportPresenter> implements PatientReportContract.View, OnItemClick {


    @BindView(R.id.allocated_patient)
    TextView tv_allocated_patient;  //未分配患者
    @BindView(R.id.unregister_patient)
    TextView tv_unregister_patient;  //未注册患者

    @BindView(R.id.rl_status_refresh)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.list_newly_discharged_patient)
    WrapRecyclerView recyclerView_all_patient;


    @BindView(R.id.sp_bq)
    Spinner sp_bq;

    @BindView(R.id.all_check)
    RelativeLayout rl_allcheck;


    @BindView(R.id.layout_search_result)
    SmartRefreshLayout search_smartRefreshLayout;

    @BindView(R.id.search_allpatient)
    WrapRecyclerView recyclerView_search_patient;


    private List<NewLeaveBean.RowsDTO> tempPaitentList = new ArrayList<>();
    private List<NewLeaveBean.RowsDTO> rowsDTOList = new ArrayList<>();
    private AllocatedPatientAdapter allocatedPatientAdapter;

    private AllocatedPatientRequest allocatedPatientRequest = new AllocatedPatientRequest();


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
     * 接收来住Homeactivity消息 请求接口 获取病区病种
     *
     * @param mainRefreshObj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainRefreshObj mainRefreshObj) {
        // TODO: 2022/1/11 request
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

        recyclerView_all_patient.setLayoutManager(new LinearLayoutManager(homeActivity));
//        recyclerView_all_patient.addItemDecoration(MUtils.spaceDivider(
//                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));
        allocatedPatientAdapter = new AllocatedPatientAdapter(rowsDTOList, this);
        recyclerView_all_patient.setAdapter(allocatedPatientAdapter);


    }


    @Override
    public void initData() {
        super.initData();
        allocatedPatientRequest.pageNo = 1;
        allocatedPatientRequest.pageSize = 20;
//        allocatedPatientRequest.isRegister = "1";
//        allocatedPatientRequest.existsPlanFlag = "2";
        mPresenter.qryAllocatedPatienList(allocatedPatientRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.all_check})
    public void OnClick(View view) {
        switch (view.getId()) {
            //全选
            case R.id.all_check:
                if (allocatedPatientAdapter != null) {
                    allocatedPatientAdapter.AllChecked();
                    tempPaitentList.addAll(rowsDTOList);
                    for (int i = 0; i < tempPaitentList.size(); i++) {
                        Log.e(TAG, "已有Userid: " + tempPaitentList.get(i).getUserId());
                    }
                }
                break;
        }
    }


    @Override
    public void qryAllocatedPatienSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList) {
        homeActivity.runOnUiThread(() -> {
            Log.e(TAG, "未分配查询成功-----");
            rowsDTOList = infoDetailDTOList;
            allocatedPatientAdapter.updateList(rowsDTOList);
        });
    }

    @Override
    public void qryAllocatedPatienFail(String failMessage) {
        homeActivity.runOnUiThread(() -> {
            Log.e(TAG, "未分配查询失败-----" + failMessage);
            ToastUtils.show(failMessage);
        });
    }

    @Override
    public void onItemClick(Object object, boolean isCheck) {
        Log.e(TAG, "onItemClick: " + isCheck);
        NewLeaveBean.RowsDTO item = (NewLeaveBean.RowsDTO) object;
        if (isCheck) {
            if (!tempPaitentList.contains(item)) {
                tempPaitentList.add(item);
            }
        } else {
            if (tempPaitentList.contains(object)) {
                tempPaitentList.remove(item);
            }
        }

        for (int i = 0; i < tempPaitentList.size(); i++) {
            Log.e(TAG, "已有Userid: " + tempPaitentList.get(i).getUserId());
        }

    }
}
