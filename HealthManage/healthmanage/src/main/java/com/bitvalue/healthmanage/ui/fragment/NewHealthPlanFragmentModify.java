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
import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.DeleteTaskApi;
import com.bitvalue.healthmanage.http.request.PlanDetailApi;
import com.bitvalue.healthmanage.http.request.UpdateImageApi;
import com.bitvalue.healthmanage.http.response.PlanListBean;
import com.bitvalue.healthmanage.http.response.RefreshPlansObj;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.media.ImagePreviewActivity;
import com.bitvalue.healthmanage.ui.media.ImageSelectActivity;
import com.bitvalue.healthmanage.util.InputMethodUtils;
import com.bitvalue.healthmanage.util.Utils;
import com.bitvalue.healthmanage.widget.DataUtil;
import com.bitvalue.healthmanage.widget.SwitchButton;
import com.bitvalue.healthmanage.widget.tasks.TaskView;
import com.bitvalue.healthmanage.widget.tasks.bean.SavePlanApi;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import okhttp3.Call;

public class NewHealthPlanFragmentModify extends AppFragment {

//    @BindView(R.id.task_first)
//    TaskView task_first;

    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.et_intro)
    EditText et_intro;

    @BindView(R.id.layout_tasks_wrap)
    LinearLayout layout_tasks_wrap;

    @BindView(R.id.npl_cover)
    BGANinePhotoLayout npl_cover;

    @BindView(R.id.npl_intro)
    BGANinePhotoLayout npl_intro;

    @BindView(R.id.npl_detail)
    BGANinePhotoLayout npl_detail;

    private static final int MAX_COVER = 1;
    private static final int MAX_INTRO = 9;
    private static final int MAX_DETAIL = 9;
    private TextView tv_base_time;
    private TimePickerView pvTime;
    private SwitchButton switch_button;
    private HomeActivity homeActivity;
    private List<TaskView> taskViews = new ArrayList<>();
    private SavePlanApi savePlanApi = new SavePlanApi();
    private boolean isChecked;
    private PlanListBean planListBean;
    private String mDayCount = "0";
    private ArrayList<String> coverPhotos = new ArrayList<>();
    private ArrayList<String> introPhotos = new ArrayList<>();
    private ArrayList<String> detailPhotos = new ArrayList<>();
    private List<UpdateImageApi> coverImages = new ArrayList<>();
    private List<UpdateImageApi> introImages = new ArrayList<>();
    private List<UpdateImageApi> detailImages = new ArrayList<>();
    private ArrayList<String> coverFinal = new ArrayList<>();
    private ArrayList<String> introFinal = new ArrayList<>();
    private ArrayList<String> detailFinal = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_health_plan_modify;
    }

    @Override
    protected void initView() {
        planListBean = (PlanListBean) getArguments().getSerializable(Constants.PLAN_LIST_BEAN);
        getPlanDetail();

        tv_base_time = getView().findViewById(R.id.tv_base_time);
        switch_button = getView().findViewById(R.id.switch_button);
        homeActivity = (HomeActivity) getActivity();
        initTimePick();
        initSwitchButton();
        initChoosePhotos();
        sortTasks();
    }

    private void getPlanDetail() {
        PlanDetailApi planDetailApi = new PlanDetailApi();
        planDetailApi.templateId = planListBean.templateId;
        EasyHttp.post(this).api(planDetailApi).request(new HttpCallback<HttpData<SavePlanApi>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<SavePlanApi> result) {
                super.onSucceed(result);
                savePlanApi = result.getData();
                if (result.getCode() == 0) {
                    initPlanData();
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    private void initChoosePhotos() {
        npl_cover.setDelegate(new BGANinePhotoLayout.Delegate() {
            @Override
            public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                ImagePreviewActivity.start(homeActivity, model);
            }

            @Override
            public void onClickExpand(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                ninePhotoLayout.setIsExpand(true);
                ninePhotoLayout.flushItems();
            }
        });
        npl_cover.setData(coverPhotos);

        npl_intro.setDelegate(new BGANinePhotoLayout.Delegate() {
            @Override
            public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                ImagePreviewActivity.start(homeActivity, model);
            }

            @Override
            public void onClickExpand(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                ninePhotoLayout.setIsExpand(true);
                ninePhotoLayout.flushItems();
            }
        });
        npl_intro.setData(introPhotos);

        npl_detail.setDelegate(new BGANinePhotoLayout.Delegate() {
            @Override
            public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                ImagePreviewActivity.start(homeActivity, model);
            }

            @Override
            public void onClickExpand(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                ninePhotoLayout.setIsExpand(true);
                ninePhotoLayout.flushItems();
            }
        });
        npl_detail.setData(detailPhotos);
    }

    private void initPlanData() {
        et_name.setText(savePlanApi.templateName);
//        tv_base_time.setText(savePlanApi.basetimeType);
        et_intro.setText(savePlanApi.goodsInfo.goodsDescribe);

        //任务列表
        for (int i = 0; i < savePlanApi.templateTask.size(); i++) {
            TaskView taskView = new TaskView(homeActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            taskView.setTaskData(savePlanApi.templateTask.get(i));
            taskView.setTaskViewCallBack(new TaskView.TaskViewCallBack() {
                @Override
                public void onDeleteTask() {
                    DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定删除任务吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                        @Override
                        public void onPositive() {

                            deleteTaskData(taskView.getTaskId(), taskView);
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
            taskView.setIsModify(true);
            layout_tasks_wrap.addView(taskView, layoutParams);
            taskViews.add(taskView);
            sortTasks();
        }

        //初始化几个相册
        processPics(savePlanApi.goodsInfo);

        isChecked = savePlanApi.goodsInfo.status.equals("1") ? true : false;
        switch_button.setChecked(isChecked);
    }

    private void processPics(SavePlanApi.GoodsInfoDTO goodsInfo) {
        if (null != goodsInfo.previewList && !goodsInfo.previewList.isEmpty()) {
            String[] split = goodsInfo.previewList.split(",");
            for (int i = 0; i < split.length; i++) {
                coverPhotos.add(split[i]);
            }
            npl_cover.setData(coverPhotos);
        }

        if (null != goodsInfo.imgList && !goodsInfo.imgList.isEmpty()) {
            String[] split = goodsInfo.imgList.split(",");
            for (int i = 0; i < split.length; i++) {
                detailPhotos.add(split[i]);
            }
            npl_detail.setData(detailPhotos);
        }

        if (null != goodsInfo.bannerList && !goodsInfo.bannerList.isEmpty()) {
            String[] split = goodsInfo.bannerList.split(",");
            for (int i = 0; i < split.length; i++) {
                introPhotos.add(split[i]);
            }
            npl_intro.setData(introPhotos);
        }
    }

    private void initSwitchButton() {
        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton buttonView, boolean isChecked) {
                NewHealthPlanFragmentModify.this.isChecked = isChecked;
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

    @OnClick({R.id.layout_base_time, R.id.layout_add_task, R.id.tv_save, R.id.img_back,
            R.id.img_add_cover, R.id.img_add_intro, R.id.img_add_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_base_time:
//                pvTime.show();
//                InputMethodUtils.hideSoftInput(getActivity());
//                showFullPop(R.layout.layout_choose_time);
//                InputMethodUtils.hideSoftInput(getActivity());
                break;
            case R.id.layout_add_task:
                addTaskView();
                break;
            case R.id.tv_save:
                checkAllDataAndSave();
                break;

            case R.id.img_add_cover:
                Utils.checkPermission(getAttachActivity(), new Utils.PermissionCallBack() {
                    @Override
                    public void onPermissionResult(boolean permit) {
                        int canSelectNun = MAX_COVER - coverPhotos.size();
                        if (canSelectNun < 1) {
                            ToastUtil.toastShortMessage("最多选择1张照片");
                            return;
                        }
                        ImageSelectActivity.start(getAttachActivity(), canSelectNun, new ImageSelectActivity.OnPhotoSelectListener() {

                            @Override
                            public void onSelected(List<String> data) {
                                if (null == data || data.size() == 0) {
                                    return;
                                }

                                //去重
//                                ArrayList<String> newList = new ArrayList<String>();
//                                for (String cd : coverPhotos) {
//                                    if (!newList.contains(cd)) {
//                                        newList.add(cd);
//                                    }
//                                }
                                coverPhotos.addAll(data);
                                npl_cover.setData(coverPhotos);
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                    }
                });
                break;

            case R.id.img_add_intro:
                Utils.checkPermission(getAttachActivity(), new Utils.PermissionCallBack() {
                    @Override
                    public void onPermissionResult(boolean permit) {
                        int canSelectNun = MAX_INTRO - introPhotos.size();
                        if (canSelectNun < 1) {
                            ToastUtil.toastShortMessage("最多选择9张照片");
                            return;
                        }
                        ImageSelectActivity.start(getAttachActivity(), canSelectNun, new ImageSelectActivity.OnPhotoSelectListener() {

                            @Override
                            public void onSelected(List<String> data) {
                                if (null == data || data.size() == 0) {
                                    return;
                                }
                                introPhotos.addAll(data);
                                npl_intro.setData(introPhotos);
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                    }
                });
                break;

            case R.id.img_add_detail:
                Utils.checkPermission(getAttachActivity(), new Utils.PermissionCallBack() {
                    @Override
                    public void onPermissionResult(boolean permit) {
                        int canSelectNun = MAX_DETAIL - detailPhotos.size();
                        if (canSelectNun < 1) {
                            ToastUtil.toastShortMessage("最多选择9张照片");
                            return;
                        }
                        ImageSelectActivity.start(getAttachActivity(), canSelectNun, new ImageSelectActivity.OnPhotoSelectListener() {

                            @Override
                            public void onSelected(List<String> data) {
                                if (null == data || data.size() == 0) {
                                    return;
                                }
                                detailPhotos.addAll(data);
                                npl_detail.setData(detailPhotos);
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                    }
                });
                break;

            case R.id.img_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
//            case R.id.layout_add_paper:
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_PAPER, "");
//                break;
        }
    }

    private void checkAllDataAndSave() {
        //先初始化
//        savePlanApi.templateTask = new ArrayList<>();

        //套餐计划名称
        if (et_name.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入健康管理计划名称");
            return;
        } else {
            savePlanApi.templateName = et_name.getText().toString();
        }

        //组装疾病类型
//        SavePlanApi.DiseaseDTO diseaseDTO = new SavePlanApi.DiseaseDTO();
//        diseaseDTO.diseaseCode = "S001";
//        diseaseDTO.diseaseName = "通用";
//        List<SavePlanApi.DiseaseDTO> diseaseDTOS = new ArrayList<>();
//        diseaseDTOS.add(diseaseDTO);
        savePlanApi.disease.get(0).diseaseName = "通用";
        savePlanApi.disease.get(0).diseaseCode = "S001";

//        //组装基准时间
//        if (tv_base_time.getText().toString().isEmpty()) {
//            ToastUtil.toastShortMessage("请选择基准时间");
//            return;
//        } else {
//            savePlanApi.basetimeType = tv_base_time.getText().toString();
//        }

        //组装基准时间
//        if (tv_base_time.getText().toString().isEmpty()) {
//            ToastUtil.toastShortMessage("请选择基准时间");
//            return;
//        } else {
//            savePlanApi.basetimeType = mDayCount + "";
//        }

        savePlanApi.basetimeType = mDayCount + "";

        //任务列表
        for (int i = 0; i < taskViews.size(); i++) {
            SavePlanApi.TemplateTaskDTO taskData = taskViews.get(i).getTaskData();
            if (null == taskData) {
                return;
            }
            savePlanApi.templateTask.add(taskData);
        }

        //组装商品类型
//        SavePlanApi.GoodsInfoDTO goodsInfoDTO = new SavePlanApi.GoodsInfoDTO();
        if (et_intro.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入健康管理计划套餐介绍");
            return;
        } else if (et_intro.getText().toString().length() < 6) {
            ToastUtil.toastShortMessage("请输入健康管理计划套餐介绍长度超过5个字");
            return;
        }

//        if (coverPhotos.size() == 0) {
//            ToastUtil.toastShortMessage("请选择上传套餐封面");
//            return;
//        } else {
//            for (int i = 0; i < coverPhotos.size(); i++) {
//                UpdateImageApi updateImageApi = new UpdateImageApi();
//                File file = new File(coverPhotos.get(i));
//                if (!file.isDirectory() && file.exists()) {
//                    updateImageApi.file = file;
//                    coverImages.add(updateImageApi);
//                }
//            }
//        }
//
//        if (introPhotos.size() == 0) {
//            ToastUtil.toastShortMessage("请选择上传套餐介绍图片");
//            return;
//        } else {
//            for (int i = 0; i < introPhotos.size(); i++) {
//                UpdateImageApi updateImageApi = new UpdateImageApi();
//                File file = new File(introPhotos.get(i));
//                if (!file.isDirectory() && file.exists()) {
//                    updateImageApi.file = file;
//                    introImages.add(updateImageApi);
//                }
//            }
//        }
//
//        if (detailPhotos.size() == 0) {
//            ToastUtil.toastShortMessage("请选择上传套餐详情图片");
//            return;
//        } else {
//            for (int i = 0; i < detailPhotos.size(); i++) {
//                UpdateImageApi updateImageApi = new UpdateImageApi();
//                File file = new File(detailPhotos.get(i));
//                if (!file.isDirectory() && file.exists()) {
//                    updateImageApi.file = file;
//                    detailImages.add(updateImageApi);
//                }
//            }
//        }

        savePlanApi.goodsInfo.goodsDescribe = et_intro.getText().toString();
        savePlanApi.goodsInfo.goodsName = et_name.getText().toString();
        savePlanApi.goodsInfo.status = isChecked ? "1" : "3";//1启用  0停用
//        savePlanApi.goodsInfo = goodsInfoDTO;

        showDialog();
        EasyHttp.post(this).api(savePlanApi).request(new HttpCallback<HttpData<SavePlanApi>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<SavePlanApi> result) {
                hideDialog();
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
                hideDialog();
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

                        //点击添加的view删除不需要调接口
//                        deleteTaskData(taskView.getTaskId(), taskView);
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

    private void deleteTaskData(String taskId, TaskView taskView) {
        DeleteTaskApi deleteTaskApi = new DeleteTaskApi();
        deleteTaskApi.templateId = savePlanApi.templateId;
        deleteTaskApi.taskId = taskId;
        EasyHttp.post(this).api(deleteTaskApi).request(new HttpCallback<HttpData<SavePlanApi>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<SavePlanApi> result) {
                super.onSucceed(result);
                if (result.getCode() == 0) {
                    ToastUtil.toastShortMessage("删除任务成功");

                    layout_tasks_wrap.removeView(taskView);
                    taskViews.remove(taskView);
                    sortTasks();
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
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
