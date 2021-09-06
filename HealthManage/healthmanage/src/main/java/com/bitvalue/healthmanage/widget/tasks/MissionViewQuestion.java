package com.bitvalue.healthmanage.widget.tasks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.PaperBean;
import com.bitvalue.healthmanage.http.response.PlanDetailResult;
import com.bitvalue.healthmanage.http.response.QuestionResultBean;
import com.bitvalue.healthmanage.http.response.VideoResultBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.QuestionQuickAdapter;
import com.bitvalue.healthmanage.ui.adapter.interfaz.OnItemDelete;
import com.bitvalue.healthmanage.util.DensityUtil;
import com.bitvalue.healthmanage.util.MUtils;
import com.bitvalue.healthmanage.widget.DataUtil;
import com.bitvalue.healthmanage.widget.tasks.bean.GetMissionObj;
import com.bitvalue.healthmanage.widget.tasks.bean.SavePlanApi;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MissionViewQuestion extends LinearLayout implements DataInterface {

    @BindView(R.id.list_items)
    RecyclerView list_question;

    @BindView(R.id.img_type)
    ImageView img_type;

    @BindView(R.id.tv_mission_name)
    TextView tv_mission_name;

    @BindView(R.id.layout_add_item)
    LinearLayout layout_add_item;

    private int TaskNo;
    private int MissionNo;
    public int getTaskNo() {
        return TaskNo;
    }

    public void setTaskNo(int taskNo) {
        TaskNo = taskNo;
    }

    public int getMissionNo() {
        return MissionNo;
    }

    //项目编号，从0开始
    public void setMissionNo(int missionNo) {
        MissionNo = missionNo;
    }
    private HomeActivity homeActivity;
    private MissionViewCallBack missionViewCallBack;
    private SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO();
    private QuestionQuickAdapter questionQuickAdapter;
    private List<QuestionResultBean.ListDTO> questionBeans = new ArrayList<>();
    private List<PaperBean> mPapers = new ArrayList<>();
    private List<String> questions = new ArrayList<>();
    private boolean isModify;

    public MissionViewQuestion(Context context) {
        super(context);
        initView(context);
    }

    public MissionViewQuestion(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MissionViewQuestion(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MissionViewQuestion(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        EventBus.getDefault().register(this);
        homeActivity = (HomeActivity) context;
        View.inflate(context, R.layout.layout_misson_question, this);
        ButterKnife.bind(this);
//        layout_after_end.getLayoutParams().height = Constants.screenHeight / 3;

        initQuestionListView();
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    private void initQuestionListView() {
        list_question.setLayoutManager(new LinearLayoutManager(homeActivity));
        list_question.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));
        questionQuickAdapter = new QuestionQuickAdapter(R.layout.item_paper, questionBeans);
        questionQuickAdapter.setOnItemDelete(new OnItemDelete() {
            @Override
            public void onItemDelete(int position) {
                questionBeans.remove(position);
                questions.remove(position);
                questionQuickAdapter.setNewData(questionBeans);
            }
        });
        questionQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                homeActivity.switchSecondFragment(Constants.FRAGMENT_QUESTION_DETAIL, questionBeans.get(position));
            }
        });
        list_question.setAdapter(questionQuickAdapter);
    }

    /**
     * 处理订阅消息
     *
     * @param questionBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QuestionResultBean.ListDTO questionBean) {
        if (questionBeans.size() == 1) {
            return;
        }

        //不是当前的任务和项目跳转的获取问卷，不添加
        if (questionBean.MissionNo != this.MissionNo || questionBean.TaskNo != this.TaskNo){
            return;
        }

        for (QuestionResultBean.ListDTO questionOld : questionBeans) {//去重
            if (questionOld.key.equals(questionBean.key)) {
                ToastUtil.toastShortMessage("请勿添加重复的问卷");
                return;
            }
        }

        questionBeans.add(questionBean);
        questions.add(questionBean.id);
        questionQuickAdapter.setNewData(questionBeans);
    }

    @OnClick({R.id.layout_add_item, R.id.img_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_add_item:
                if (questionBeans.size() == 1) {
                    ToastUtil.toastShortMessage("仅限添加一个问卷");
                    return;
                }
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_QUESTION, new GetMissionObj(this.TaskNo,this.MissionNo));
                break;
            case R.id.img_type:
                if (null != missionViewCallBack) {
                    missionViewCallBack.onDeleteMission();
                }
                break;
        }
    }

    public void setMissionViewCallBack(MissionViewCallBack missionViewCallBack) {
        this.missionViewCallBack = missionViewCallBack;
    }

    @Override
    public SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO getData() {
        //写死的类型数据
        templateTaskContentDTO.taskType = "Quest";
//        templateTaskContentDTO.contentDetail.remindName = "健康问卷";
        if (!isModify) {
            templateTaskContentDTO.contentDetail = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO.ContentDetailDTO();
        }
        //填入的数据
        templateTaskContentDTO.contentDetail.questName = questionBeans.get(0).name;
        templateTaskContentDTO.contentDetail.questId = questionBeans.get(0).key;//TODO 31号江南说取key
        return templateTaskContentDTO;
    }

    @Override
    public PlanDetailResult.UserPlanDetailsDTO getAssembleData() {
        if (questionBeans.size() == 0) {
            return null;
        }
        PlanDetailResult.UserPlanDetailsDTO userPlanDetailsDTO = new PlanDetailResult.UserPlanDetailsDTO();
        userPlanDetailsDTO.planType = "Quest";
        userPlanDetailsDTO.planDescribe = "专家团队对您近期的病情变化进行跟踪后，为您选择的健康问卷，请及时查阅。";
        userPlanDetailsDTO.execFlag = 0;
        userPlanDetailsDTO.questUrl = questionBeans.get(0).questUrl;//接口没有返回 questUrl
        return userPlanDetailsDTO;
    }

    public boolean isDataReady() {
        if (questionBeans.size() == 0) {
            ToastUtil.toastShortMessage("请选择问卷");
            return false;
        }
        return true;
    }

    public void setMissionData(SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO) {
        this.templateTaskContentDTO = DataUtil.getNotNullData(templateTaskContentDTO);
        QuestionResultBean.ListDTO listDTO = new QuestionResultBean.ListDTO();
        listDTO.name = templateTaskContentDTO.contentDetail.questName;
//        listDTO.id = templateTaskContentDTO.contentDetail.questId;
        listDTO.key = templateTaskContentDTO.contentDetail.questId;
        listDTO.questUrl = templateTaskContentDTO.contentDetail.questUrl;
        questionBeans.add(listDTO);
        questionQuickAdapter.setNewData(questionBeans);

        isModify = true;
    }

    public interface MissionViewCallBack {
        void onDeleteMission();

        void onGetMissionData();
    }

}
