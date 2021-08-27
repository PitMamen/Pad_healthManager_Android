package com.bitvalue.healthmanage.ui.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.response.RefreshPlansObj;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.util.InputMethodUtils;
import com.bitvalue.healthmanage.util.UiUtil;
import com.bitvalue.healthmanage.widget.DataUtil;
import com.bitvalue.healthmanage.widget.SwitchButton;
import com.bitvalue.healthmanage.widget.TimePeriodView;
import com.bitvalue.healthmanage.widget.popupwindow.CommonPopupWindow;
import com.bitvalue.healthmanage.widget.tasks.TaskView;
import com.bitvalue.healthmanage.widget.tasks.bean.SavePlanApi;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import org.greenrobot.eventbus.EventBus;

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
    private SwitchButton switch_button;
    private HomeActivity homeActivity;
    private List<TaskView> taskViews = new ArrayList<>();
    private SavePlanApi savePlanApi = new SavePlanApi();
    private boolean isChecked;
    private CommonPopupWindow popupWindow;
    private String mDayCount;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_health_plan;
    }

    @Override
    protected void initView() {
        tv_base_time = getView().findViewById(R.id.tv_base_time);
        switch_button = getView().findViewById(R.id.switch_button);
        homeActivity = (HomeActivity) getActivity();
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

    @Override
    protected void initData() {

    }

    @OnClick({R.id.layout_base_time, R.id.layout_add_task, R.id.tv_save, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_base_time:
//                pvTime.show();
                showFullPop(R.layout.layout_choose_time);
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
            case R.id.img_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }

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
                            case R.layout.layout_choose_time:
                                TimePeriodView timePeriodView = view.findViewById(R.id.tp_view);
                                timePeriodView.setGetTimeCallBack(new TimePeriodView.GetTimeCallBack() {
                                    @Override
                                    public void onGetTime(String day, String str) {
                                        tv_base_time.setText(day + "天后");
                                        mDayCount = day;
                                        if (null != popupWindow) {
                                            popupWindow.dismiss();
                                        }
                                    }
                                });
                                break;
                        }
                    }
                })
                .create();
        popupWindow.showAtLocation(AppApplication.instance().getHomeActivity().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
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
            savePlanApi.basetimeType = mDayCount + "";
        }

        //任务列表
        for (int i = 0; i < taskViews.size(); i++) {
            SavePlanApi.TemplateTaskDTO taskData = taskViews.get(i).getTaskData();
            if (null == taskData) {
                return;
            } else {
                savePlanApi.templateTask.add(taskData);
            }
        }

        //组装商品类型
        if (et_intro.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入健康管理计划套餐介绍");
            return;
        } else if (et_intro.getText().toString().length() < 6) {
            ToastUtil.toastShortMessage("请输入任务名称长度超过5个字");
            return;
        }

        SavePlanApi.GoodsInfoDTO goodsInfoDTO = new SavePlanApi.GoodsInfoDTO();
        goodsInfoDTO.goodsDescribe = et_intro.getText().toString();
        goodsInfoDTO.goodsName = et_name.getText().toString();
        goodsInfoDTO.status = isChecked ? "1" : "3";//1启用  0停用
        savePlanApi.goodsInfo = goodsInfoDTO;

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
                    EventBus.getDefault().post(new RefreshPlansObj());
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
