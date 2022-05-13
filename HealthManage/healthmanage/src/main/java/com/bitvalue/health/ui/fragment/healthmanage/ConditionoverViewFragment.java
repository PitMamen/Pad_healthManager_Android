package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.Constants.MESSAGEINFO;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.responsebean.MessageInfoData;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.ConditionOverViewAdapter;
import com.bitvalue.health.util.customview.WrapRecyclerView;
import com.bitvalue.healthmanage.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author created by bitvalue
 * @data : 05/06
 * 病情概述 界面
 */
public class ConditionoverViewFragment extends BaseFragment {
    private HomeActivity homeActivity;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.image_list)
    WrapRecyclerView wrapRecyclerView_imagelist;
    @BindView(R.id.back_btn)
    ImageView btn_back;
    private List<String> imageList = new ArrayList<>();

    private ConditionOverViewAdapter conditionOverViewAdapter;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_condition_overview_layout;
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        btn_back.setOnClickListener(v -> {
            if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                homeActivity.getSupportFragmentManager().popBackStack();
            }
        });

        MessageInfoData info = (MessageInfoData) getArguments().getSerializable(MESSAGEINFO);
        if (info == null) return;
        tv_content.setText(info.content);
        String dataResource = info.imageList;
        String[] dataArray = dataResource.split(",");
        for (String data : dataArray) {
            imageList.add(data);
        }
        initList();
    }

    private void initList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(homeActivity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        wrapRecyclerView_imagelist.setLayoutManager(layoutManager);
        conditionOverViewAdapter = new ConditionOverViewAdapter(R.layout.item_imagevisitlod_layout, imageList, homeActivity);
        wrapRecyclerView_imagelist.setAdapter(conditionOverViewAdapter);
    }


    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }
}
