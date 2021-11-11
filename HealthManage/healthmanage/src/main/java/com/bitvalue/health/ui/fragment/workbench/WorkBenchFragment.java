package com.bitvalue.health.ui.fragment.workbench;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.api.responsebean.VisitBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.callback.OnItemClickCallback;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.DiagnosisTreatmentAdapter;
import com.bitvalue.health.ui.adapter.VisitAdapter;
import com.bitvalue.health.ui.adapter.WaitVisitListAdapter;
import com.bitvalue.health.ui.fragment.docfriend.DocFriendsFragment;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.healthmanage.R;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author created by bitvalue
 * @data : 11/10
 */
public class WorkBenchFragment extends BaseFragment implements OnItemClickCallback {

    @BindView(R.id.list_tobeseen)
    RecyclerView recyclerView_tobeseen;  //待就诊 list

    @BindView(R.id.list_zzjz)
    RecyclerView recyclerView_zzjzlist;  // 正在就诊 list

    @BindView(R.id.rl_wait_list)
    RecyclerView recyclerView_dsflblist;  //待随访就诊列表 list


    private ArrayList<VisitBean> beanArrayList = new ArrayList<>();


    private VisitAdapter visitAdapter; //待诊列表 adapter
    private DiagnosisTreatmentAdapter diagnosisTreatmentAdapter; //诊治中 adapter
    private WaitVisitListAdapter waitvisitAdapter; //待随访列表 adapter


    private HomeActivity homeActivity;


    //初始化当前Fragment的实例
    public static WorkBenchFragment getInstance(boolean is_need_toast) {
        WorkBenchFragment workbenchFragment = new WorkBenchFragment();
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
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                beanArrayList.add(new VisitBean("株洲市人民医院", "2021/08/14", "14:20~15:30", "云门诊"));
            } else if (i % 3 == 0) {
                beanArrayList.add(new VisitBean("长沙市人民医院", "2021/08/15", "16:20~18:30", "院内门诊"));
            } else if (i % 4 == 0) {
                beanArrayList.add(new VisitBean("常德市人民医院", "2021/02/05", "12:20~11:30", "联合门诊"));
            } else if (i % 5 == 0) {
                beanArrayList.add(new VisitBean("湘潭市人民医院", "2021/04/13", "12:04~16:30", "远程门诊"));
            }
        }
        recyclerView_tobeseen.setLayoutManager(new LinearLayoutManager(homeActivity));
        recyclerView_tobeseen.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));


        recyclerView_zzjzlist.setLayoutManager(new LinearLayoutManager(homeActivity));
        recyclerView_zzjzlist.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));

        recyclerView_dsflblist.setLayoutManager(new LinearLayoutManager(homeActivity));
        recyclerView_dsflblist.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));



        visitAdapter = new VisitAdapter(R.layout.item_visit_patient, this,beanArrayList);
        waitvisitAdapter = new WaitVisitListAdapter(R.layout.item_visit_patient, beanArrayList);
        diagnosisTreatmentAdapter = new DiagnosisTreatmentAdapter(R.layout.item_visit_patient, this,beanArrayList);


        recyclerView_tobeseen.setAdapter(visitAdapter);
        recyclerView_zzjzlist.setAdapter(diagnosisTreatmentAdapter);
        recyclerView_dsflblist.setAdapter(waitvisitAdapter);


    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_workbench;
    }

    @Override
    public void onItemClick(VideoClientsResultBean item, int messageCount) {

    }
}
