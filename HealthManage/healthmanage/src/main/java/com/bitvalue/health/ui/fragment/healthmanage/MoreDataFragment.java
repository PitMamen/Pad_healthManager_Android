package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.FRAGMENT_DETAIL;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PatientBaseInfoBean;
import com.bitvalue.health.api.responsebean.TaskDetailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.MoreDataDetailContract;
import com.bitvalue.health.contract.healthmanagercontract.VisitPlanDetailContract;
import com.bitvalue.health.presenter.healthmanager.MoreDataDetailPresenter;
import com.bitvalue.health.presenter.healthmanager.VisitPlanDetailPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.ImageListDisplayAdapter;
import com.bitvalue.health.ui.adapter.MoreDataAdapter;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.hjq.toast.ToastUtils;

import java.util.List;

import butterknife.BindView;

/**
 * @author created by bitvalue
 * @data : 02/11
 * 患者更多数据界面
 */
public class MoreDataFragment extends BaseFragment<MoreDataDetailPresenter> implements MoreDataDetailContract.View {
    @BindView(R.id.list_moredata)
    WrapRecyclerView totalRecyclerView;
    @BindView(R.id.layout_back)
    View back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rl_default_view)
    RelativeLayout default_view;   //无数据的默认界面


    private MoreDataAdapter moreDataAdapter;


    private HomeActivity homeActivity;

    private NewLeaveBean.RowsDTO itemPosition;

    @Override
    protected MoreDataDetailPresenter createPresenter() {
        return new MoreDataDetailPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_moredata_layout;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        back.setVisibility(View.VISIBLE);
        tv_title.setText("更多详情");
        back.setOnClickListener(v -> {
            if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0)
                homeActivity.getSupportFragmentManager().popBackStack();
        });
        itemPosition = (NewLeaveBean.RowsDTO) getArguments().getSerializable(FRAGMENT_DETAIL);
        if (!EmptyUtil.isEmpty(itemPosition)) {
            if (!EmptyUtil.isEmpty(itemPosition.getUserId())) {
                showLoading();
                LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity);
                totalRecyclerView.setLayoutManager(layoutManager);
                moreDataAdapter = new MoreDataAdapter(R.layout.item_moredada_layout, null,homeActivity);
                UserLocalVisitBean bean = new UserLocalVisitBean();
                bean.userId = itemPosition.getUserId();
                mPresenter.qryUserLocalVisit(bean);
                totalRecyclerView.setAdapter(moreDataAdapter);
            } else {
                default_view.setVisibility(View.VISIBLE);
            }

        }
    }


    @Override
    public void qryUserVisitSuccess(List<TaskDetailBean> iamgeList) {
        homeActivity.runOnUiThread(() -> {
            hideDialog();
            default_view.setVisibility((iamgeList.size() == 0) ? View.VISIBLE : View.GONE);
            moreDataAdapter.setNewData(iamgeList);
        });
    }

    @Override
    public void qryUserVisitFail(String failMessage) {
        homeActivity.runOnUiThread(() -> {
            hideDialog();
            default_view.setVisibility(View.VISIBLE);
//            ToastUtils.show(failMessage);
        });
    }


    //下面的两个回调这里不要实现
    @Override
    public void getPatientBaseInfoSuccess(PatientBaseInfoBean patientBaseInfoBean) {

    }

    @Override
    public void getPatientBaseInfoFail(String messageFail) {

    }


    /**
     * 以下两个回调不需要实现
     * @param responseList
     */
    @Override
    public void getDataReviewRecordSuccess(List<DataReViewRecordResponse> responseList) {

    }

    @Override
    public void getDataReviewRecordFail(String messageFail) {

    }


    /**
     * 以下两个回调不需要实现
     * @param reViewRecordResponse
     */
    @Override
    public void saveDataReviewRecordSuucess(DataReViewRecordResponse reViewRecordResponse) {

    }

    @Override
    public void saveDataReviewRecordFail(String messageFail) {

    }
}
