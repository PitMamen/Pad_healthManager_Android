package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.ClickUtils.isFastClick;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.api.eventbusbean.MainRefreshObj;
import com.bitvalue.health.api.responsebean.DiseaseSpeciesBean;
import com.bitvalue.health.api.responsebean.InpatientAreaBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.healthmanagercontract.PatientReportContract;
import com.bitvalue.health.presenter.healthmanager.PatientReportPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.DiseaseSpeciesAdapter;
import com.bitvalue.health.ui.adapter.InPatientAreaAdapter;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author created by bitvalue
 * @data : 10/27
 * 患者报道 界面
 */
public class PatientReportFragment extends BaseFragment<PatientReportPresenter> implements PatientReportContract.View {


    @BindView(R.id.list_inpatient_area)
    WrapRecyclerView list_inpatient_area;  //病区
    @BindView(R.id.list_disease_species)
    WrapRecyclerView list_disease_species;  //病种


    private HomeActivity homeActivity;


    private List<DiseaseSpeciesBean> diseaseSpeciesBeanList = new ArrayList<>();
    private List<InpatientAreaBean> inpatientAreaBeanList = new ArrayList<>();
    private DiseaseSpeciesAdapter speciesAdapter;
    private InPatientAreaAdapter inPatientAreaAdapter;


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
        list_inpatient_area.setLayoutManager(new LinearLayoutManager(homeActivity));
        list_inpatient_area.addItemDecoration(MUtils.spaceDivider(DensityUtil.dip2px(homeActivity, this.getResources().getDimension(R.dimen.qb_px_4)), false));
        list_disease_species.setLayoutManager(new LinearLayoutManager(homeActivity));
        list_disease_species.addItemDecoration(MUtils.spaceDivider(DensityUtil.dip2px(homeActivity, this.getResources().getDimension(R.dimen.qb_px_4)), false));

        speciesAdapter = new DiseaseSpeciesAdapter(R.layout.item_dis_spec_layout,null);
        inPatientAreaAdapter = new InPatientAreaAdapter(R.layout.item_dis_spec_layout,null);

        list_disease_species.setAdapter(speciesAdapter);
        list_inpatient_area.setAdapter(inPatientAreaAdapter);

    }


    @Override
    public void initData() {
        super.initData();
        for (int i = 1; i < 11; i++) {
            diseaseSpeciesBeanList.add(new DiseaseSpeciesBean("病种"+i));
            inpatientAreaBeanList.add(new InpatientAreaBean("病区"+i));
        }

        speciesAdapter.setNewData(diseaseSpeciesBeanList);
        inPatientAreaAdapter.setNewData(inpatientAreaBeanList);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
