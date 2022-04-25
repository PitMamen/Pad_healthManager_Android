package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.FRAGMENT_DETAIL;
import static com.bitvalue.health.util.Constants.MORE_DATA;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.requestbean.QueryPlanDetailApi;
import com.bitvalue.health.api.requestbean.UserLocalVisitBean;
import com.bitvalue.health.api.responsebean.DataReViewRecordResponse;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PatientBaseInfoBean;
import com.bitvalue.health.api.responsebean.PlanCheckEamResponseBean;
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
import com.bitvalue.health.ui.adapter.MoreDataDetailChildImageAdapter;
import com.bitvalue.health.util.EmptyUtil;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.http.listener.OnHttpListener;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author created by bitvalue
 * @data : 02/11
 * 患者更多数据界面
 */
public class MoreDataFragment extends BaseFragment<MoreDataDetailPresenter> implements MoreDataDetailContract.View, OnHttpListener<Object> {
    @BindView(R.id.item_image_datalist)
    WrapRecyclerView totalRecyclerView;
    @BindView(R.id.layout_back)
    View back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rl_default_view)
    RelativeLayout default_view;   //无数据的默认界面
    @BindView(R.id.rl_tips_layout)
    RelativeLayout rl_tips_layout;
    @BindView(R.id.tv_data_time)
    TextView tv_data_time;
    @BindView(R.id.tv_data_title)
    TextView tv_title_data;
    @BindView(R.id.tv_diagnosis)
    TextView tv_diagnosis;


    private MoreDataAdapter moreDataAdapter;
    private MoreDataDetailChildImageAdapter moreDataDetailChildImageAdapter;


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
        QueryPlanDetailApi queryPlanDetailApi = (QueryPlanDetailApi) getArguments().getSerializable(MORE_DATA);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(v -> {
            if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0)
                homeActivity.getSupportFragmentManager().popBackStack();
        });
        initList();
        EasyHttp.get(this).api(queryPlanDetailApi).request(new HttpCallback<ApiResult<PlanCheckEamResponseBean>>(this) {
            @Override
            public void onSucceed(ApiResult<PlanCheckEamResponseBean> result) {
                super.onSucceed(result);
                if (!EmptyUtil.isEmpty(result)) {
                    if (result.getCode() == 0) {
                        if (!EmptyUtil.isEmpty(result.getData())) {
                            PlanCheckEamResponseBean responseBean = result.getData();
                            String name = "";
                            String Describe = "";
                            switch (queryPlanDetailApi.planType) {
                                case "Exam":
                                    name = responseBean.examName;
                                    Describe = responseBean.examDescribe;
                                    break;

                                case "Check":
                                    name = responseBean.checkName;
                                    Describe = responseBean.checkDescribe;
                                    break;
                            }

                            tv_title.setText(name);
                            tv_title_data.setText(name);
                            tv_diagnosis.setVisibility(!EmptyUtil.isEmpty(Describe) ? View.VISIBLE : View.GONE);
                            tv_diagnosis.setText(Describe);
                            tv_data_time.setText(TimeUtils.getTime(responseBean.createdTime));
                            List<TaskDetailBean.HealthImagesDTO> imagesDTOList = responseBean.healthImages;
                            if (imagesDTOList != null && imagesDTOList.size() > 0) {
//                                List<String> urlList = new ArrayList<>();
//                                for (int i = 0; i < imagesDTOList.size(); i++) {
//                                    urlList.add(imagesDTOList.get(i).getPreviewFileUrl());
//                                }
                                moreDataDetailChildImageAdapter.setNewData(imagesDTOList);
                            } else {
                                rl_tips_layout.setVisibility(View.GONE);
                                default_view.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });


//        itemPosition = (NewLeaveBean.RowsDTO) getArguments().getSerializable(FRAGMENT_DETAIL);
//        if (!EmptyUtil.isEmpty(itemPosition)) {
//            if (!EmptyUtil.isEmpty(itemPosition.getUserId())) {
//                showLoading();
//                LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity);
//                totalRecyclerView.setLayoutManager(layoutManager);
//                moreDataAdapter = new MoreDataAdapter(R.layout.item_moredada_layout, null,homeActivity);
//                UserLocalVisitBean bean = new UserLocalVisitBean();
//                bean.userId = itemPosition.getUserId();
//                mPresenter.qryUserLocalVisit(bean);
//                totalRecyclerView.setAdapter(moreDataAdapter);
//            } else {
//                default_view.setVisibility(View.VISIBLE);
//            }
//
//        }
    }


    private void initList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(Application.instance());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        totalRecyclerView.setLayoutManager(layoutManager);
        moreDataDetailChildImageAdapter = new MoreDataDetailChildImageAdapter(R.layout.item_moredata_child_layout, null, homeActivity);
        totalRecyclerView.setAdapter(moreDataDetailChildImageAdapter);
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
     *
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
     *
     * @param reViewRecordResponse
     */
    @Override
    public void saveDataReviewRecordSuucess(DataReViewRecordResponse reViewRecordResponse) {

    }

    @Override
    public void saveDataReviewRecordFail(String messageFail) {

    }
}
