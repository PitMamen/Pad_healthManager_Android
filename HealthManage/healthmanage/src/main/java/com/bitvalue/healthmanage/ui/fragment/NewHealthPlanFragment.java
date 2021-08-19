package com.bitvalue.healthmanage.ui.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.util.InputMethodUtils;
import com.bitvalue.healthmanage.widget.DataUtil;
import com.bitvalue.healthmanage.widget.SwitchButton;
import com.bitvalue.healthmanage.widget.tasks.TaskView;
import com.bitvalue.healthmanage.widget.tasks.bean.SavePlanApi;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class NewHealthPlanFragment extends AppFragment {

    @BindView(R.id.task_first)
    TaskView task_first;

    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.et_intro)
    EditText et_intro;

    @BindView(R.id.layout_tasks_wrap)
    LinearLayout layout_tasks_wrap;

    private TextView tv_base_time;
    private TimePickerView pvTime;
    private SwitchButton switch_button;
    private HomeActivity homeActivity;
    private List<TaskView> taskViews = new ArrayList<>();
    private SavePlanApi savePlanApi = new SavePlanApi();
    private boolean isChecked;

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
                NewHealthPlanFragment.this.isChecked = isChecked;
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

    @OnClick({R.id.layout_base_time, R.id.layout_add_task, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_base_time:
                pvTime.show();
                InputMethodUtils.hideSoftInput(getActivity());
                break;
            case R.id.layout_add_task:
                addTaskView();
                break;
            case R.id.tv_save:
                checkAllDataAndSave();
                break;
//            case R.id.layout_add_paper:
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_PAPER, "");
//                break;
        }
    }

    private void checkAllDataAndSave() {
        //先初始化
        savePlanApi.templateTask = new ArrayList<>();

        //套餐计划名称
        if (et_name.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入健康管理计划名称");
            return;
        } else {
            savePlanApi.templateName = et_name.getText().toString();
        }

        //组装疾病类型
        SavePlanApi.DiseaseDTO diseaseDTO = new SavePlanApi.DiseaseDTO();
        diseaseDTO.diseaseCode = "S001";
        diseaseDTO.diseaseName = "通用";
        List<SavePlanApi.DiseaseDTO> diseaseDTOS = new ArrayList<>();
        diseaseDTOS.add(diseaseDTO);
        savePlanApi.disease = diseaseDTOS;

        //组装基准时间
        if (tv_base_time.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请选择基准时间");
            return;
        } else {
            savePlanApi.basetimeType = tv_base_time.getText().toString();
        }

        //组装商品类型
        SavePlanApi.GoodsInfoDTO goodsInfoDTO = new SavePlanApi.GoodsInfoDTO();
        if (et_intro.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入健康管理计划套餐介绍");
            return;
        }
        goodsInfoDTO.goodsDescribe = et_intro.getText().toString();
        goodsInfoDTO.goodsName = et_name.getText().toString();
        goodsInfoDTO.status = isChecked ? "1" : "0";//1启用  0停用
        savePlanApi.goodsInfo = goodsInfoDTO;


        //任务列表
        for (int i = 0; i < taskViews.size(); i++) {
            SavePlanApi.TemplateTaskDTO taskData = taskViews.get(i).getTaskData();
            if (null == taskData) {
                return;
            } else {
                savePlanApi.templateTask.add(taskData);
            }
        }

        EasyHttp.post(this).api(savePlanApi).request(new HttpCallback<HttpData<SavePlanApi>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<SavePlanApi> result) {
                super.onSucceed(result);
                if (result.getCode() == 0) {
                    ToastUtil.toastShortMessage("保存成功");
                    homeActivity.getSupportFragmentManager().popBackStack();
                } else {
                    ToastUtil.toastShortMessage(result.getMessage());
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
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
