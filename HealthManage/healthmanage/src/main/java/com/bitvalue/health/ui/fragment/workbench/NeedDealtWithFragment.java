package com.bitvalue.health.ui.fragment.workbench;

import static com.bitvalue.health.util.DataUtil.isNumeric;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.api.responsebean.VisitBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.callback.PhoneFollowupCliclistener;
import com.bitvalue.health.contract.mytodolistcontact.MyToDoListContact;
import com.bitvalue.health.presenter.mytodolistpersenter.MyToDoListPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.DiagnosisTreatmentAdapter;
import com.bitvalue.health.ui.adapter.SFJH_HZZX_Adapter;
import com.bitvalue.health.ui.adapter.VisitAdapter;
import com.bitvalue.health.ui.adapter.WaitOutRemindAdapter;
import com.bitvalue.health.ui.adapter.WaitVisitListAdapter;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * @author created by bitvalue
 * @data : 11/10
 *
 * 我的待办Fragment
 */
public class NeedDealtWithFragment extends BaseFragment<MyToDoListPersenter> implements MyToDoListContact.MyToDoListView, PhoneFollowupCliclistener {

    @BindView(R.id.list_patient_dynamic)
    RecyclerView list_dynamic;  //患者动态 list

    @BindView(R.id.list_timeout_remind)
    RecyclerView list_timeout_remind;  //超时提醒 list
    private HomeActivity homeActivity;
    private RequestNewLeaveBean requestNewLeaveBean = new RequestNewLeaveBean();
    private SFJH_HZZX_Adapter  Adapter_Dynamic;
    private WaitOutRemindAdapter Adapter_WaitOutRemind;
    private List<NewLeaveBean.RowsDTO> allDynamicList = new ArrayList<>(); //我的待办患者列表
    private List<NewLeaveBean.RowsDTO> waitoutListData = new ArrayList<>(); //超时提醒list
    private int pageNo = 1;
    private int pageSize = 100;




    //初始化当前Fragment的实例
    public static NeedDealtWithFragment getInstance(boolean is_need_toast) {
        NeedDealtWithFragment workbenchFragment = new NeedDealtWithFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_toast", is_need_toast);
        workbenchFragment.setArguments(bundle);
        return workbenchFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        list_dynamic.setLayoutManager(new LinearLayoutManager(homeActivity));
        list_timeout_remind.setLayoutManager(new LinearLayoutManager(homeActivity));
        Adapter_Dynamic = new SFJH_HZZX_Adapter(allDynamicList, homeActivity);
        list_dynamic.setAdapter(Adapter_Dynamic);
        Adapter_Dynamic.setOnItemPhoneNumCallback(this);

        Adapter_WaitOutRemind = new WaitOutRemindAdapter(waitoutListData, homeActivity);
        list_timeout_remind.setAdapter(Adapter_WaitOutRemind);
        Adapter_WaitOutRemind.setOnItemPhoneNumCallback(this);
    }


    @Override
    public void initData() {
        super.initData();
        showLoading();
        requestNewLeaveBean.setKeyWord("");
        requestNewLeaveBean.setPageNo(pageNo);
        requestNewLeaveBean.setPageSize(pageSize);
        mPresenter.qryPatientList(requestNewLeaveBean);
        mPresenter.qryWaitOotList(requestNewLeaveBean);
    }

    @Override
    protected MyToDoListPersenter createPresenter() {
        return new MyToDoListPersenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_workbench;
    }



    @Override
    public void phoneNumberCallback(String phoneNumber) {
        Log.e(TAG, "phoneNumberCallback: " + phoneNumber);
        if (isNumeric(phoneNumber)) {
            callPhone(phoneNumber);
        } else {
            ToastUtils.show("非法号码!");
        }
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
            allDynamicList.addAll(infoDetailDTOList);

            Adapter_Dynamic.updateSFJHList(allDynamicList);

        });
    }

    @Override
    public void qryWitoutListSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList) {
        getActivity().runOnUiThread(() -> {
            hideDialog();
            waitoutListData.addAll(infoDetailDTOList);
            Adapter_WaitOutRemind.updateList(waitoutListData);
        });

    }

    @Override
    public void qryWaitoutListFail(String messageFail) {
        getActivity().runOnUiThread(() -> {
            hideDialog();
            ToastUtils.show(messageFail);
        });
    }

    @Override
    public void qryPatientListFail(String messageFail) {
        getActivity().runOnUiThread(() -> {
            hideDialog();
            ToastUtils.show(messageFail);
        });
    }
}
