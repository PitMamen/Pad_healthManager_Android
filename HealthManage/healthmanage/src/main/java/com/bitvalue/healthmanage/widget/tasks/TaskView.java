package com.bitvalue.healthmanage.widget.tasks;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.util.InputMethodUtils;
import com.bitvalue.healthmanage.util.UiUtil;
import com.bitvalue.healthmanage.widget.DataUtil;
import com.bitvalue.healthmanage.widget.StatusLayout;
import com.bitvalue.healthmanage.widget.popupwindow.CommonPopupWindow;
import com.bitvalue.healthmanage.widget.tasks.bean.SavePlanApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @BindView(R.id.tv_mission_time_choose)
    public TextView tv_mission_time_choose;

    private HomeActivity homeActivity;
    private TaskViewCallBack taskViewCallBack;
    private CommonPopupWindow popupWindow;
    private List<View> missionViews = new ArrayList<>();
    public SavePlanApi.TemplateTaskDTO templateTaskDTO = new SavePlanApi.TemplateTaskDTO();
    private TimePickerView pvTime;

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
        initTimePick();
    }

    private void initTimePick() {
        //初始化时间选择器
        pvTime = new TimePickerBuilder(homeActivity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.format(date);
                tv_mission_time_choose.setText(simpleDateFormat.format(date).substring(0, 11));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
//                .setRangDate(new GregorianCalendar(1900, 1, 1), Calendar.getInstance())
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

    }

    @OnClick({R.id.tv_delete_mission, R.id.layout_add_mission, R.id.tv_mission_time_choose})
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
            case R.id.tv_mission_time_choose:
                pvTime.show();
                InputMethodUtils.hideSoftInput(homeActivity);
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

    public List<SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO> getMissionData() {
        List<SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO> templateTaskContentDTOS = new ArrayList<>();
        for (int i = 0; i < missionViews.size(); i++) {
            View view = missionViews.get(i);
            SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO data;
            if (view instanceof MissionViewRemind) {
                data = ((MissionViewRemind) view).getData();
            } else if (view instanceof MissionViewArticle) {
                data = ((MissionViewArticle) view).getData();

            } else {
                data = ((MissionViewQuestion) view).getData();
            }
            templateTaskContentDTOS.add(data);
        }
        return templateTaskContentDTOS;
    }

    public SavePlanApi.TemplateTaskDTO getTaskData() {
        templateTaskDTO.execTime = tv_mission_time_choose.getText().toString();
        templateTaskDTO.taskName = et_first_mission.getText().toString();
        templateTaskDTO.templateTaskContent = getMissionData();
        return templateTaskDTO;
    }

    public interface TaskViewCallBack {
        void onDeleteTask();

        void onGetTaskData();
    }
}
