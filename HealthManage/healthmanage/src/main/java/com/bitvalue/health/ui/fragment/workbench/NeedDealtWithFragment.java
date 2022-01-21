package com.bitvalue.health.ui.fragment.workbench;

import static com.bitvalue.health.util.DataUtil.isNumeric;

import android.content.Context;
import android.content.Intent;
import android.icu.lang.UScript;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.requestbean.RequestNewLeaveBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.callback.PhoneFollowupCliclistener;
import com.bitvalue.health.contract.mytodolistcontact.MyToDoListContact;
import com.bitvalue.health.presenter.mytodolistpersenter.MyToDoListPersenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.HealthPlanListAdapter;
import com.bitvalue.health.ui.adapter.WaitOutRemindAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author created by bitvalue
 * @data : 11/10
 *
 * 我的待办Fragment
 */
public class NeedDealtWithFragment extends BaseFragment<MyToDoListPersenter> implements MyToDoListContact.MyToDoListView, PhoneFollowupCliclistener {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.list_patient_dynamic)
    RecyclerView list_dynamic;

    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    private HomeActivity homeActivity;
    private RequestNewLeaveBean requestNewLeaveBean = new RequestNewLeaveBean();
    private HealthPlanListAdapter healthPlanListAdapter;

    private List<NewLeaveBean.RowsDTO> allDynamicList = new ArrayList<>(); //我的待办患者列表

    private HealthPlanTaskDetailFragment healthPlanPreviewFragment;

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
        tv_title.setText("随访计划");
        img_back.setVisibility(View.GONE);
        list_dynamic.setLayoutManager(new LinearLayoutManager(homeActivity));

        healthPlanListAdapter = new HealthPlanListAdapter();
        list_dynamic.setAdapter(healthPlanListAdapter);



        healthPlanListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.iv_send_msg:
                        ToastUtil.toastShortMessage("发送消息");
                        break;
                    case R.id.iv_check_plan:
                        replaceFragment(386,(NewLeaveBean.RowsDTO)adapter.getItem(position));
                        break;
                }
            }
        });


    }

    private void replaceFragment(int planId, NewLeaveBean.RowsDTO userInfo){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle args=new Bundle();
        args.putInt(Constants.PLAN_ID,planId);
        args.putSerializable(Constants.USERINFO, userInfo);
        if (healthPlanPreviewFragment == null) {
            healthPlanPreviewFragment = new HealthPlanTaskDetailFragment();

            healthPlanPreviewFragment.setArguments(args);

            transaction.add(R.id.framelayout, healthPlanPreviewFragment);
            transaction.commit();
        }else {
            healthPlanPreviewFragment.refreshData(planId,userInfo);
        }


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

            healthPlanListAdapter.setNewData(allDynamicList);

        });
    }

    @Override
    public void qryWitoutListSuccess(List<NewLeaveBean.RowsDTO> infoDetailDTOList) {


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
