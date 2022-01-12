package com.bitvalue.health.ui.fragment.schedule;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data :
 */
public class ScheduleFragment extends BaseFragment {

    @BindView(R.id.tv_month)
    TextView tv_month;
    @BindView(R.id.tv_week)
    TextView tv_week;
    @BindView(R.id.tv_day)
    TextView tv_days;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int currentFragment = -1;
    private HomeActivity homeActivity;

    //初始化当前Fragment的实例
    public static ScheduleFragment getInstance(boolean is_need_toast) {
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_toast", is_need_toast);
        scheduleFragment.setArguments(bundle);
        return scheduleFragment;
    }


    private void initFragment() {
        fragmentList.add(new ScheduleMonthFragment());
        fragmentList.add(new ScheduleWeekFragment());
        fragmentList.add(new ScheduleDayFragment());
    }

    /**
     * fragment 界面之间切换
     * @param index
     */
    private void switchFragment(int index) {
        if (index == currentFragment) return;
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment fragment = fragmentList.get(i);
            if (i == index) {
                if (!fragment.isAdded()) {   //未添加  则添加
                    fragmentTransaction.add(R.id.framelaout_bottom, fragment);
                } else {
                    fragmentTransaction.show(fragment);   //添加了的  直接显示
                }
            } else {
                if (fragment.isAdded()) {     //非当前点击的 如果已经添加了则 都隐藏
                    fragmentTransaction.hide(fragment);
                }
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
        currentFragment = index;
        changeTextBackground(index);
    }

    private void changeTextBackground(int index) {
        if (index == 0) {
            tv_month.setTextColor(getResources().getColor(R.color.white_FFFFFF));
            tv_month.setBackgroundResource(R.drawable.shape_textbg_bule);

            tv_week.setTextColor(getResources().getColor(R.color.beauty_color_black));
            tv_days.setTextColor(getResources().getColor(R.color.beauty_color_black));
            tv_week.setBackground(getResources().getDrawable(R.drawable.shape_text_bg));
            tv_days.setBackground(getResources().getDrawable(R.drawable.shape_text_bg));

        } else if (index == 1) {
            tv_week.setTextColor(getResources().getColor(R.color.white_FFFFFF));
            tv_week.setBackgroundResource(R.drawable.shape_textbg_bule);

            tv_month.setTextColor(getResources().getColor(R.color.beauty_color_black));
            tv_days.setTextColor(getResources().getColor(R.color.beauty_color_black));
            tv_month.setBackground(getResources().getDrawable(R.drawable.shape_text_bg));
            tv_days.setBackground(getResources().getDrawable(R.drawable.shape_text_bg));

        } else if (index == 2) {
            tv_days.setTextColor(getResources().getColor(R.color.white_FFFFFF));
            tv_days.setBackgroundResource(R.drawable.shape_textbg_bule);

            tv_month.setTextColor(getResources().getColor(R.color.beauty_color_black));
            tv_week.setTextColor(getResources().getColor(R.color.beauty_color_black));
            tv_month.setBackground(getResources().getDrawable(R.drawable.shape_text_bg));
            tv_week.setBackground(getResources().getDrawable(R.drawable.shape_text_bg));
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        initFragment();
        switchFragment(0);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_scherdule;
    }


    /***
     *
     * 月 周 日 控件的点击事件
     * @param view
     */
    @OnClick({R.id.tv_month, R.id.tv_week, R.id.tv_day})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_month:
                switchFragment(0);
                break;

            case R.id.tv_week:
                switchFragment(1);
                break;


            case R.id.tv_day:
                switchFragment(2);
                break;

        }
    }


}
