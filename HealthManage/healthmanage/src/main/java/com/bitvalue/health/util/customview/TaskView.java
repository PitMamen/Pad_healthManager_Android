package com.bitvalue.health.util.customview;

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

import com.bitvalue.health.Application;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.requestbean.DeleteMissionApi;
import com.bitvalue.health.api.requestbean.SavePlanApi;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.InputMethodUtils;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.UiUtil;
import com.bitvalue.health.util.customview.task.MissionViewAnalyse;
import com.bitvalue.health.util.customview.task.MissionViewArticle;
import com.bitvalue.health.util.customview.task.MissionViewQuestion;
import com.bitvalue.health.util.customview.task.MissionViewRemind;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/***
 * 创建任务 自定义布局
 */
public class TaskView extends LinearLayout {

    @BindView(R.id.et_first_mission)
    EditText et_first_mission;

    @BindView(R.id.layout_mission_time)
    RelativeLayout layout_mission_time;

    @BindView(R.id.layout_add_mission)
    LinearLayout layout_add_mission;

    @BindView(R.id.layout_mission_wrap)
    LinearLayout layout_mission_wrap;

    @BindView(R.id.et_task_intro)
    EditText et_task_intro;

    @BindView(R.id.tv_mission_no)
    public TextView tv_mission_no;

    @BindView(R.id.tv_mission_time_choose)
    public TextView tv_mission_time_choose;

    private int TaskNo;

    public int getTaskNo() {
        return TaskNo;
    }

    //任务编号，从0开始
    public void setTaskNo(int taskNo) {
        TaskNo = taskNo;
    }

    private HomeActivity homeActivity;
    private TaskViewCallBack taskViewCallBack;
    private CommonPopupWindow popupWindow;
    private List<View> missionViews = new ArrayList<>();
    private int missionSize;
    public SavePlanApi.TemplateTaskDTO templateTaskDTO = new SavePlanApi.TemplateTaskDTO();
    private boolean isModify;
    private String mDayCount = "0";

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
                showFullPop(R.layout.layout_choose_time);
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
                            case R.layout.layout_choose_time:
                                TimePeriodView timePeriodView = view.findViewById(R.id.tp_view);
                                timePeriodView.setGetTimeCallBack(new TimePeriodView.GetTimeCallBack() {
                                    @Override
                                    public void onGetTime(String day, String str) {
                                        tv_mission_time_choose.setText(day + "天后");
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
        popupWindow.showAtLocation(Application.instance().getHomeActivity().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
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
        addMissionViewQuestion(null);
    }

    private void addMissionViewQuestion(SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) homeActivity.getResources().getDimension(R.dimen.qb_px_10);
        MissionViewQuestion missionViewQuestion = new MissionViewQuestion(homeActivity);
        if (null != templateTaskContentDTO) {
            missionViewQuestion.setMissionData(templateTaskContentDTO);
        }
        missionViewQuestion.setMissionViewCallBack(new MissionViewQuestion.MissionViewCallBack() {
            @Override
            public void onDeleteMission() {
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定删除项目吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        if (null != templateTaskContentDTO) {
                            deleteMissionData(templateTaskContentDTO.id, missionViewQuestion);
                        } else {//修改的taskview需要调接口，新增的不需要
                            layout_mission_wrap.removeView(missionViewQuestion);
                            missionViews.remove(missionViewQuestion);
                            missionSize--;
                            sortMissionViews();
                        }
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
        //项目编号，从0开始
        missionViewQuestion.setMissionNo(missionSize);
        missionViewQuestion.setTaskNo(getTaskNo());
        missionViews.add(missionViewQuestion);
        missionSize++;
        layout_mission_wrap.addView(missionViewQuestion, layoutParams);
    }

    private void sortMissionViews() {
        for (int i = 0; i < missionViews.size(); i++) {
            View view = missionViews.get(i);
            if (view instanceof MissionViewRemind) {
                MissionViewRemind missionViewRemind = (MissionViewRemind) view;
                missionViewRemind.setMissionNo(i);
            } else if (view instanceof MissionViewArticle) {
                MissionViewArticle missionViewArticle = (MissionViewArticle) view;
                missionViewArticle.setMissionNo(i);
            } else {
                MissionViewQuestion missionViewQuestion = (MissionViewQuestion) view;
                missionViewQuestion.setMissionNo(i);
            }
        }
    }

    private void deleteMissionData(String id, View view) {
        //templateTaskContentDTO.id,templateTaskContentDTO.taskId,
        //templateTaskDTO
        DeleteMissionApi deleteMissionApi = new DeleteMissionApi();
        deleteMissionApi.templateId = templateTaskDTO.templateId;
        deleteMissionApi.taskId = templateTaskDTO.taskId;
        deleteMissionApi.id = id;
        EasyHttp.post(homeActivity).api(deleteMissionApi).request(new HttpCallback<ApiResult<SavePlanApi>>(homeActivity) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(ApiResult<SavePlanApi> result) {
                super.onSucceed(result);
                if (result.getCode() == 0) {
                    ToastUtil.toastShortMessage("删除项目成功");
                    layout_mission_wrap.removeView(view);
                    missionViews.remove(view);
                    missionSize--;
                    sortMissionViews();
                } else {
                    ToastUtil.toastShortMessage("删除项目失败");
                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    private void addMissionViewArticle() {
        addMissionViewArticle(null);
    }

    private void addMissionViewArticle(SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) homeActivity.getResources().getDimension(R.dimen.qb_px_10);
        MissionViewArticle missionViewArticle = new MissionViewArticle(homeActivity);
        if (templateTaskContentDTO != null) {
            missionViewArticle.setMissionData(templateTaskContentDTO);
        }
        missionViewArticle.setMissionViewCallBack(new MissionViewArticle.MissionViewCallBack() {
            @Override
            public void onDeleteMission() {
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定删除项目吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        if (null != templateTaskContentDTO) {
                            deleteMissionData(templateTaskContentDTO.id, missionViewArticle);
                        } else {//修改的taskview需要调接口，新增的不需要
                            layout_mission_wrap.removeView(missionViewArticle);
                            missionViews.remove(missionViewArticle);
                            missionSize--;
                            sortMissionViews();
                        }
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
        missionViewArticle.setMissionNo(missionSize);
        missionViewArticle.setTaskNo(getTaskNo());
        missionViews.add(missionViewArticle);
        missionSize++;
        layout_mission_wrap.addView(missionViewArticle, layoutParams);
    }

    private void addMissionViewRemind() {
        addMissionViewRemind(null);
    }

    private void addMissionViewRemind(SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) homeActivity.getResources().getDimension(R.dimen.qb_px_10);
        MissionViewRemind missionViewRemind = new MissionViewRemind(homeActivity);
        if (templateTaskContentDTO != null) {
            missionViewRemind.setMissionData(templateTaskContentDTO);
        }
        missionViewRemind.setMissionViewCallBack(new MissionViewRemind.MissionViewCallBack() {
            @Override
            public void onDeleteMission() {
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定删除项目吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        if (null != templateTaskContentDTO) {
                            deleteMissionData(templateTaskContentDTO.id, missionViewRemind);
                        } else {//修改的taskview需要调接口，新增的不需要
                            layout_mission_wrap.removeView(missionViewRemind);
                            missionViews.remove(missionViewRemind);
                            missionSize--;
                            sortMissionViews();
                        }
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
        missionViewRemind.setMissionNo(missionSize);
        missionViewRemind.setTaskNo(getTaskNo());
        missionViews.add(missionViewRemind);
        missionSize++;
        layout_mission_wrap.addView(missionViewRemind, layoutParams);
    }

    private void addMissionViewAnalyse() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) homeActivity.getResources().getDimension(R.dimen.qb_px_10);
        MissionViewAnalyse missionViewAnalyse = new MissionViewAnalyse(homeActivity);
        missionViewAnalyse.setMissionViewCallBack(new MissionViewAnalyse.MissionViewCallBack() {
            @Override
            public void onDeleteMission() {
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定删除项目吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
//                        if (isModify) {
//                            deleteMissionData(templateTaskContentDTO.id, missionViewRemind);
//                        } else {//修改的taskview需要调接口，新增的不需要
//                            layout_mission_wrap.removeView(missionViewRemind);
//                        }
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
        missionViewAnalyse.setMissionNo(missionSize);
        missionViewAnalyse.setTaskNo(getTaskNo());
        missionViews.add(missionViewAnalyse);
        missionSize++;
        layout_mission_wrap.addView(missionViewAnalyse, layoutParams);
    }

    public void setTaskViewCallBack(TaskViewCallBack taskViewCallBack) {
        this.taskViewCallBack = taskViewCallBack;
    }

    /**
     * 获取单个任务的所有项目的数据
     * <p>
     * 先判断单个控件的数据是否为空，任一个控件的数据不完整，都返回null；
     *
     * @return
     */
    public List<SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO> getMissionData() {
        List<SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO> templateTaskContentDTOS = new ArrayList<>();
        for (int i = 0; i < missionViews.size(); i++) {
            View view = missionViews.get(i);
            SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO data = null;
            if (view instanceof MissionViewRemind) {
                MissionViewRemind missionViewRemind = (MissionViewRemind) view;
                if (missionViewRemind.isDataReady()) {
                    data = missionViewRemind.getData();
                } else {
                    return null;
                }
            } else if (view instanceof MissionViewArticle) {
                MissionViewArticle missionViewArticle = (MissionViewArticle) view;
                if (missionViewArticle.isDataReady()) {
                    data = missionViewArticle.getData();
                } else {
                    return null;
                }
            } else {
                MissionViewQuestion missionViewQuestion = (MissionViewQuestion) view;
                if (missionViewQuestion.isDataReady()) {
                    data = missionViewQuestion.getData();
                } else {
                    return null;
                }
            }
            templateTaskContentDTOS.add(data);
        }
        return templateTaskContentDTOS;
    }

    public SavePlanApi.TemplateTaskDTO getTaskData() {
        if (et_first_mission.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入任务名称");
            return null;
        } else if (et_first_mission.getText().toString().length() < 5) {
            ToastUtil.toastShortMessage("请输入任务名称长度超过4个字");
            return null;
        }

        if (et_task_intro.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请输入任务描述");
            return null;
        } else if (et_task_intro.getText().toString().length() < 5) {
            ToastUtil.toastShortMessage("请输入任务描述长度超过4个字");
            return null;
        }

        templateTaskDTO.taskName = et_first_mission.getText().toString();
        templateTaskDTO.taskDescribe = et_task_intro.getText().toString();

        if (tv_mission_time_choose.getText().toString().isEmpty()) {
            ToastUtil.toastShortMessage("请选择任务执行时间");
            return null;
        }
        templateTaskDTO.execTime = mDayCount + "";

        List<SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO> missionData = getMissionData();
        if (null == missionData || missionData.size() == 0) {
            ToastUtils.show("请至少添加一个任务项目");
            return null;
        } else {
            templateTaskDTO.templateTaskContent = missionData;
        }
        return templateTaskDTO;
    }

    public List<PlanDetailResult.UserPlanDetailsDTO> getAssembleData() {
        List<PlanDetailResult.UserPlanDetailsDTO> userPlanDetailsDTOS = new ArrayList<>();
        for (int i = 0; i < missionViews.size(); i++) {
            View view = missionViews.get(i);
            PlanDetailResult.UserPlanDetailsDTO data = null;
            if (view instanceof MissionViewRemind) {
                MissionViewRemind missionViewRemind = (MissionViewRemind) view;
                data = missionViewRemind.getAssembleData();
                if (null != data) {
                    data.planDescribe = et_task_intro.getText().toString();
//                    data.execTime = TimeUtils.getTime((System.currentTimeMillis() + Integer.parseInt(mDayCount) * 24 * 60 * 60 * 1000), TimeUtils.YY_MM_DD_FORMAT_3);
                    userPlanDetailsDTOS.add(data);
                }
            } else if (view instanceof MissionViewArticle) {
                MissionViewArticle missionViewArticle = (MissionViewArticle) view;
                data = missionViewArticle.getAssembleData();
                if (null != data) {
                    data.planDescribe = et_task_intro.getText().toString();
//                    data.execTime = TimeUtils.getTime((System.currentTimeMillis() + Integer.parseInt(mDayCount) * 24 * 60 * 60 * 1000), TimeUtils.YY_MM_DD_FORMAT_3);
                    userPlanDetailsDTOS.add(data);
                }
            } else {
                MissionViewQuestion missionViewQuestion = (MissionViewQuestion) view;
                data = missionViewQuestion.getAssembleData();
                if (null != data) {
                    data.planDescribe = et_task_intro.getText().toString();
//                    data.execTime = TimeUtils.getTime((System.currentTimeMillis() + Integer.parseInt(mDayCount) * 24 * 60 * 60 * 1000), TimeUtils.YY_MM_DD_FORMAT_3);
//                    data.execTime = mDayCount + "天后";
                    userPlanDetailsDTOS.add(data);
                }
            }
        }
        return userPlanDetailsDTOS;
    }

    public void setTaskData(SavePlanApi.TemplateTaskDTO templateTaskDTO) {
        this.templateTaskDTO = templateTaskDTO;

        et_first_mission.setText(templateTaskDTO.taskName);
        et_task_intro.setText(templateTaskDTO.taskDescribe);
        mDayCount = templateTaskDTO.execTime;
        tv_mission_time_choose.setText(templateTaskDTO.execTime + "天后");
        setMissionData();
    }

    /**
     * 获取单个任务的所有项目的数据
     * <p>
     * 先判断单个控件的数据是否为空，任一个控件的数据不完整，都返回null；
     *
     * @return
     */
    public void setMissionData() {
        if (templateTaskDTO.templateTaskContent.size() == 0) {
            return;
        }

        //planType   Quest   Remind  Knowledge   DrugGuide
        for (int i = 0; i < templateTaskDTO.templateTaskContent.size(); i++) {
            if (templateTaskDTO.templateTaskContent.get(i).taskType.equals("Knowledge")) {
                addMissionViewArticle(templateTaskDTO.templateTaskContent.get(i));
            } else if (templateTaskDTO.templateTaskContent.get(i).taskType.equals("Quest")) {
                addMissionViewQuestion(templateTaskDTO.templateTaskContent.get(i));
            } else if (templateTaskDTO.templateTaskContent.get(i).taskType.equals("Remind")) {
                addMissionViewRemind(templateTaskDTO.templateTaskContent.get(i));
            }
        }
    }

    public String getTaskId() {
        return templateTaskDTO.taskId;
    }

    public void setIsModify(boolean isModify) {
        this.isModify = isModify;
    }

    public boolean getIsModify() {
        return isModify;
    }

    public interface TaskViewCallBack {
        void onDeleteTask();

        void onGetTaskData();
    }
}
