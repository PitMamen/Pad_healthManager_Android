package com.bitvalue.healthmanage.widget.tasks;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.util.UiUtil;
import com.bitvalue.healthmanage.widget.DataUtil;
import com.bitvalue.healthmanage.widget.popupwindow.CommonPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskView extends LinearLayout {

    @BindView(R.id.et_first_mission)
    EditText et_first_mission;

    @BindView(R.id.layout_mission_time)
    RelativeLayout layout_mission_time;

    @BindView(R.id.layout_add_mission)
    LinearLayout layout_add_mission;

    @BindView(R.id.layout_mission_wrap)
    LinearLayout layout_mission_wrap;

    @BindView(R.id.tv_mission_no)
    public TextView tv_mission_no;

    private HomeActivity homeActivity;
    private TaskViewCallBack taskViewCallBack;
    private CommonPopupWindow popupWindow;
    private List<View> missionViews = new ArrayList<>();

    public TaskView(Context context) {
        super(context);
        initView(context);
    }

    public TaskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TaskView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        homeActivity = (HomeActivity) context;
        View.inflate(context, R.layout.layout_task, this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_delete_mission, R.id.layout_add_mission})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_delete_mission:
                if (null != taskViewCallBack) {
                    taskViewCallBack.onDeleteTask();
                }
                break;
            case R.id.layout_add_mission:
                showFullPop(R.layout.choose_mission);
                break;
        }
    }

    //全屏弹出 底部
    private void showFullPop(int layoutResId) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View upView = LayoutInflater.from(homeActivity).inflate(layoutResId, null);
        //测量View的宽高
        UiUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(homeActivity)
                .setView(layoutResId)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        switch (layoutResId) {
                            case R.layout.choose_mission:
                                view.findViewById(R.id.layout_questions).setOnClickListener(onClickListener);
                                view.findViewById(R.id.layout_articles).setOnClickListener(onClickListener);
                                view.findViewById(R.id.layout_reminds).setOnClickListener(onClickListener);
                                view.findViewById(R.id.layout_analyse).setOnClickListener(onClickListener);
                                break;
                        }
                    }
                })
                .create();
        popupWindow.showAtLocation(AppApplication.instance().getHomeActivity().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_questions:
                    addMissionViewQuestion();
                    break;
                case R.id.layout_articles:
                    addMissionViewArticle();
                    break;
                case R.id.layout_reminds:
                    addMissionViewRemind();
                    break;
                case R.id.layout_analyse:
                    addMissionViewAnalyse();
                    break;
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    };

    private void addMissionViewQuestion() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) homeActivity.getResources().getDimension(R.dimen.qb_px_10);
        MissionViewQuestion missionViewQuestion = new MissionViewQuestion(homeActivity);
        missionViewQuestion.setMissionViewCallBack(new MissionViewQuestion.MissionViewCallBack() {
            @Override
            public void onDeleteMission() {
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定删除项目吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        layout_mission_wrap.removeView(missionViewQuestion);
                        missionViews.remove(missionViewQuestion);
                    }

                    @Override
                    public void onNegative() {

                    }
                });


            }

            @Override
            public void onGetMissionData() {

            }
        });
        missionViews.add(missionViewQuestion);
        layout_mission_wrap.addView(missionViewQuestion, layoutParams);
    }

    private void addMissionViewArticle() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) homeActivity.getResources().getDimension(R.dimen.qb_px_10);
        MissionViewArticle missionViewArticle = new MissionViewArticle(homeActivity);
        missionViewArticle.setMissionViewCallBack(new MissionViewArticle.MissionViewCallBack() {
            @Override
            public void onDeleteMission() {
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定删除项目吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        layout_mission_wrap.removeView(missionViewArticle);
                        missionViews.remove(missionViewArticle);
                    }

                    @Override
                    public void onNegative() {

                    }
                });

            }

            @Override
            public void onGetMissionData() {

            }
        });
        missionViews.add(missionViewArticle);
        layout_mission_wrap.addView(missionViewArticle, layoutParams);
    }

    private void addMissionViewRemind() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) homeActivity.getResources().getDimension(R.dimen.qb_px_10);
        MissionViewRemind missionViewRemind = new MissionViewRemind(homeActivity);
        missionViewRemind.setMissionViewCallBack(new MissionViewRemind.MissionViewCallBack() {
            @Override
            public void onDeleteMission() {
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定删除项目吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        layout_mission_wrap.removeView(missionViewRemind);
                        missionViews.remove(missionViewRemind);
                    }

                    @Override
                    public void onNegative() {

                    }
                });

            }

            @Override
            public void onGetMissionData() {

            }
        });
        missionViews.add(missionViewRemind);
        layout_mission_wrap.addView(missionViewRemind, layoutParams);
    }

    private void addMissionViewAnalyse() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) homeActivity.getResources().getDimension(R.dimen.qb_px_10);
        MissionViewAnalyse missionViewAnalyse = new MissionViewAnalyse(homeActivity);
        missionViewAnalyse.setMissionViewCallBack(new MissionViewAnalyse.MissionViewCallBack() {
            @Override
            public void onDeleteMission() {
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定删除项目吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        layout_mission_wrap.removeView(missionViewAnalyse);
                        missionViews.remove(missionViewAnalyse);
                    }

                    @Override
                    public void onNegative() {

                    }
                });


            }

            @Override
            public void onGetMissionData() {

            }
        });
        missionViews.add(missionViewAnalyse);
        layout_mission_wrap.addView(missionViewAnalyse, layoutParams);
    }

    public void setTaskViewCallBack(TaskViewCallBack taskViewCallBack) {
        this.taskViewCallBack = taskViewCallBack;
    }

    public interface TaskViewCallBack {
        void onDeleteTask();

        void onGetTaskData();
    }
}
