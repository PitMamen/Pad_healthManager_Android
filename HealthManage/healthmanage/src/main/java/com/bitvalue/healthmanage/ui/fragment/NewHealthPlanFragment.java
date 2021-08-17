package com.bitvalue.healthmanage.ui.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuBuilder;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.util.InputMethodUtils;
import com.bitvalue.healthmanage.widget.DataUtil;
import com.bitvalue.healthmanage.widget.SwitchButton;
import com.bitvalue.healthmanage.widget.tasks.TaskView;
import com.hjq.toast.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NewHealthPlanFragment extends AppFragment {

    @BindView(R.id.task_first)
    TaskView task_first;

    @BindView(R.id.layout_tasks_wrap)
    LinearLayout layout_tasks_wrap;

    private TextView tv_base_time;
    private TimePickerView pvTime;
    private SwitchButton switch_button;
    private HomeActivity homeActivity;
    private List<TaskView> taskViews = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_health_plan;
    }

    @Override
    protected void initView() {
        tv_base_time = getView().findViewById(R.id.tv_base_time);
        switch_button = getView().findViewById(R.id.switch_button);
        homeActivity = (HomeActivity) getActivity();
        initTimePick();
        initSwitchButton();
        taskViews.add(task_first);
        sortTasks();
        task_first.setTaskViewCallBack(new TaskView.TaskViewCallBack() {
            @Override
            public void onDeleteTask() {
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定删除任务吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        layout_tasks_wrap.removeView(task_first);
                        taskViews.remove(task_first);
                        sortTasks();
                    }

                    @Override
                    public void onNegative() {

                    }
                });


            }

            @Override
            public void onGetTaskData() {

            }
        });
    }

    private void initSwitchButton() {
        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
                ToastUtils.show("是否开启--" + isChecked);
            }
        });
    }

    private void initTimePick() {
        //初始化时间选择器
        pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.format(date);
                tv_base_time.setText(simpleDateFormat.format(date).substring(0, 11));
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

    @Override
    protected void initData() {

    }

    @OnClick({R.id.layout_base_time, R.id.layout_add_task})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_base_time:
                pvTime.show();
                InputMethodUtils.hideSoftInput(getActivity());
                break;
            case R.id.layout_add_task:
                addTaskView();
                break;
//            case R.id.layout_add_paper:
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_PAPER, "");
//                break;
        }
    }

    private void addTaskView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TaskView taskView = new TaskView(homeActivity);
        taskView.setTaskViewCallBack(new TaskView.TaskViewCallBack() {
            @Override
            public void onDeleteTask() {
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定删除任务吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        layout_tasks_wrap.removeView(taskView);
                        taskViews.remove(taskView);
                        sortTasks();
                    }

                    @Override
                    public void onNegative() {

                    }
                });

            }

            @Override
            public void onGetTaskData() {

            }
        });
        taskViews.add(taskView);
        sortTasks();
        layout_tasks_wrap.addView(taskView, layoutParams);
    }

    private void sortTasks() {
        if (taskViews.size() == 0) {
            return;
        }
        for (int i = 0; i < taskViews.size(); i++) {
            taskViews.get(i).tv_mission_no.setText("第" + (i + 1) + "次任务");
        }
    }
}
