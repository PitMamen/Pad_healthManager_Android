package com.bitvalue.healthmanage.ui.fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.myhttp.FileUploadUtils;
import com.bitvalue.healthmanage.http.request.UpdateImageApi;
import com.bitvalue.healthmanage.http.response.AudioUploadResultBean;
import com.bitvalue.healthmanage.http.response.PlanDetailResult;
import com.bitvalue.healthmanage.http.response.RefreshPlansObj;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.media.ImagePreviewActivity;
import com.bitvalue.healthmanage.ui.media.ImageSelectActivity;
import com.bitvalue.healthmanage.util.TimeUtils;
import com.bitvalue.healthmanage.util.UiUtil;
import com.bitvalue.healthmanage.util.Utils;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import okhttp3.Call;

public class NewHealthPlanFragment extends AppFragment {

    @BindView(R.id.task_first)
    TaskView task_first;

    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.et_intro)
    EditText et_intro;

    @BindView(R.id.et_plan_price)
    EditText et_plan_price;

    @BindView(R.id.et_chat_num)
    EditText et_chat_num;

    @BindView(R.id.et_video_num)
    EditText et_video_num;

    @BindView(R.id.npl_cover)
    BGANinePhotoLayout npl_cover;

    @BindView(R.id.npl_intro)
    BGANinePhotoLayout npl_intro;

    @BindView(R.id.npl_detail)
    BGANinePhotoLayout npl_detail;

    @BindView(R.id.layout_tasks_wrap)
    LinearLayout layout_tasks_wrap;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private static final int MAX_COVER = 1;
    private static final int MAX_INTRO = 9;
    private static final int MAX_DETAIL = 9;
    private TextView tv_base_time;
    private SwitchButton switch_button;
    private HomeActivity homeActivity;
    private List<TaskView> taskViews = new ArrayList<>();
    private SavePlanApi savePlanApi = new SavePlanApi();
    private boolean isChecked;
    private CommonPopupWindow popupWindow;
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
    private int taskSize;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_health_plan;
    }

    @Override
    protected void initView() {
        tv_title.setText("新增健康管理计划");
        tv_base_time = getView().findViewById(R.id.tv_base_time);
        switch_button = getView().findViewById(R.id.switch_button);
        homeActivity = (HomeActivity) getActivity();
        initSwitchButton();
        initChoosePhotos();
        taskViews.add(task_first);
        sortTasks();
        task_first.setTaskNo(0);
        taskSize = 1;
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

    @OnClick({R.id.layout_base_time, R.id.layout_add_task, R.id.tv_save, R.id.layout_back,
            R.id.img_add_cover, R.id.img_add_intro, R.id.img_add_detail, R.id.tv_preview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_base_time:
//                pvTime.show();
//                showFullPop(R.layout.layout_choose_time);
//                InputMethodUtils.hideSoftInput(getActivity());
                break;
            case R.id.layout_add_task:
                addTaskView();
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
            case R.id.tv_save:
                checkAllDataAndSave();
                break;

            case R.id.tv_preview:
                PlanDetailResult planDetailResult = assemblePreviewData();
                if (null == planDetailResult || planDetailResult.userPlanDetails.size() == 0) {
                    return;
                }
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN_PREVIEW, planDetailResult);

                break;
//            case R.id.layout_add_paper:
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_PAPER, "");
//                break;
            case R.id.layout_back:
                if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    homeActivity.getSupportFragmentManager().popBackStack();
                }
                break;
        }
    }

    private PlanDetailResult assemblePreviewData() {
        PlanDetailResult planDetailResult = new PlanDetailResult();
        planDetailResult.startDate = TimeUtils.getTime(System.currentTimeMillis(), TimeUtils.YY_MM_DD_FORMAT_3);
        if (et_name.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入健康管理计划名称");
            return null;
        }
        planDetailResult.planName = et_name.getText().toString();

        if (taskViews.size() == 0) {
            ToastUtil.toastShortMessage("请添加计划任务");
            return null;
        }
        List<PlanDetailResult.UserPlanDetailsDTO> assembleDataTotal = new ArrayList<>();
        //任务列表
        for (int i = 0; i < taskViews.size(); i++) {
            List<PlanDetailResult.UserPlanDetailsDTO> assembleData = taskViews.get(i).getAssembleData();
            if (assembleData.size() != 0) {
                assembleDataTotal.addAll(assembleData);
            }
        }
        if (assembleDataTotal.size() == 0) {
            ToastUtil.toastShortMessage("请添加计划任务项目");
        }
        planDetailResult.userPlanDetails = assembleDataTotal;
        return planDetailResult;
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
//        if (tv_base_time.getText().toString().isEmpty()) {
//            ToastUtil.toastShortMessage("请选择基准时间");
//            return;
//        } else {
//            savePlanApi.basetimeType = mDayCount + "";
//        }

        savePlanApi.basetimeType = mDayCount + "";

        if (taskViews.size() == 0) {
            ToastUtil.toastShortMessage("请添加至少一个健康任务");
            return;
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
            ToastUtil.toastShortMessage("请输入健康管理计划套餐介绍长度超过5个字");
            return;
        }

        if (et_plan_price.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入套餐价格");
            return;
        }

        if (et_chat_num.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入图文咨询次数");//云门诊复诊次数
            return;
        }

        if (et_video_num.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("云门诊复诊次数");
            return;
        }

        if (coverPhotos.size() == 0) {
            ToastUtil.toastShortMessage("请选择上传套餐封面");
            return;
        } else {
            for (int i = 0; i < coverPhotos.size(); i++) {
                UpdateImageApi updateImageApi = new UpdateImageApi();
                File file = new File(coverPhotos.get(i));
                if (!file.isDirectory() && file.exists()) {
                    updateImageApi.file = file;
                    coverImages.add(updateImageApi);
                }
            }
        }

        if (introPhotos.size() == 0) {
            ToastUtil.toastShortMessage("请选择上传套餐介绍图片");
            return;
        } else {
            for (int i = 0; i < introPhotos.size(); i++) {
                UpdateImageApi updateImageApi = new UpdateImageApi();
                File file = new File(introPhotos.get(i));
                if (!file.isDirectory() && file.exists()) {
                    updateImageApi.file = file;
                    introImages.add(updateImageApi);
                }
            }
        }

        if (detailPhotos.size() == 0) {
            ToastUtil.toastShortMessage("请选择上传套餐详情图片");
            return;
        } else {
            for (int i = 0; i < detailPhotos.size(); i++) {
                UpdateImageApi updateImageApi = new UpdateImageApi();
                File file = new File(detailPhotos.get(i));
                if (!file.isDirectory() && file.exists()) {
                    updateImageApi.file = file;
                    detailImages.add(updateImageApi);
                }
            }
        }

        SavePlanApi.GoodsInfoDTO goodsInfoDTO = new SavePlanApi.GoodsInfoDTO();
        goodsInfoDTO.goodsDescribe = et_intro.getText().toString();
        goodsInfoDTO.pirce = Integer.parseInt(et_plan_price.getText().toString());
        goodsInfoDTO.numberInquiries = Integer.parseInt(et_chat_num.getText().toString());
        goodsInfoDTO.medicalFreeNum = Integer.parseInt(et_video_num.getText().toString());
        goodsInfoDTO.goodsName = et_name.getText().toString();
        goodsInfoDTO.theLastTime = "12";//TODO 套餐服务时长（单位月）,现在写死1年
        goodsInfoDTO.status = isChecked ? "1" : "3";//1启用  0停用

        savePlanApi.goodsInfo = goodsInfoDTO;

        showDialog();
        uploadedCoverPic();
//        commitTotal();
    }

    private void commitTotal() {
        savePlanApi.goodsInfo.bannerList = getProcessString(introFinal);
        savePlanApi.goodsInfo.imgList = getProcessString(detailFinal);
        savePlanApi.goodsInfo.previewList = getProcessString(coverFinal);
        EasyHttp.post(this).api(savePlanApi).request(new HttpCallback<HttpData<SavePlanApi>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<SavePlanApi> result) {
                super.onSucceed(result);
                //增加判空
                if (result == null) {
                    return;
                }
                hideDialog();
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

    int[] j = {0};

    private void uploadedCoverPic() {
        FileUploadUtils.INSTANCE.uploadPic(NewHealthPlanFragment.this, coverImages.get(j[0]), new FileUploadUtils.OnAudioUploadCallback() {
            @Override
            public void onSuccess(HttpData<AudioUploadResultBean> result) {
                if (result.getCode() != 0) {
                    ToastUtil.toastLongMessage(result.getMessage());
                    return;
                }

                coverFinal.add(result.getData().fileLinkUrl);

                j[0]++;
                if (j[0] < coverImages.size()) {
                    uploadedCoverPic();
                } else {
                    introFinal.clear();
                    k = new int[]{0};

                    uploadedIntroPic();
                }
            }

            @Override
            public void onFail() {
                ToastUtil.toastShortMessage("请求失败");
                hideDialog();
            }
        });
    }

    int[] k = {0};

    private void uploadedIntroPic() {
        FileUploadUtils.INSTANCE.uploadPic(NewHealthPlanFragment.this, introImages.get(k[0]), new FileUploadUtils.OnAudioUploadCallback() {
            @Override
            public void onSuccess(HttpData<AudioUploadResultBean> result) {
                if (result.getCode() != 0) {
                    ToastUtil.toastLongMessage(result.getMessage());
                    return;
                }

                introFinal.add(result.getData().fileLinkUrl);

                k[0]++;
                if (k[0] < introImages.size()) {
                    uploadedIntroPic();
                } else {
                    detailFinal.clear();
                    m = new int[]{0};

                    uploadedDetailPic();
                }
            }

            @Override
            public void onFail() {
                hideDialog();
                ToastUtil.toastShortMessage("请求失败");
            }
        });
    }

    int[] m = {0};

    private void uploadedDetailPic() {
        FileUploadUtils.INSTANCE.uploadPic(NewHealthPlanFragment.this, detailImages.get(m[0]), new FileUploadUtils.OnAudioUploadCallback() {
            @Override
            public void onSuccess(HttpData<AudioUploadResultBean> result) {
                if (result.getCode() != 0) {
                    ToastUtil.toastLongMessage(result.getMessage());
                    return;
                }

                detailFinal.add(result.getData().fileLinkUrl);

                m[0]++;
                if (m[0] < detailImages.size()) {
                    uploadedDetailPic();
                } else {
                    commitTotal();
                }
            }

            @Override
            public void onFail() {
                hideDialog();
                ToastUtil.toastShortMessage("请求失败");
            }
        });
    }

    private String getProcessString(ArrayList<String> photosFinal) {
        String total = "";
        for (int i = 0; i < photosFinal.size(); i++) {
            if (i != photosFinal.size() - 1) {
                total = total + photosFinal.get(i) + ",";
            } else {
                total = total + photosFinal.get(i);
            }
        }
        return total;
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
                        taskSize--;
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
        taskView.setTaskNo(taskSize - 1);
        taskViews.add(taskView);
        taskSize++;
        sortTasks();
        layout_tasks_wrap.addView(taskView, layoutParams);
    }

    private void sortTasks() {
        if (taskViews.size() == 0) {
            return;
        }
        for (int i = 0; i < taskViews.size(); i++) {
            taskViews.get(i).tv_mission_no.setText("第" + (i + 1) + "次任务");
            taskViews.get(i).setTaskNo(i);
        }
    }
}
