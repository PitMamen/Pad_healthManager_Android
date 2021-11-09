package com.bitvalue.health.util.customview.task;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.requestbean.SavePlanApi;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.PaperBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.message.GetMissionObj;
import com.bitvalue.health.callback.OnItemDelete;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.adapter.PaperQuickAdapter;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.DensityUtil;
import com.bitvalue.health.util.MUtils;
import com.bitvalue.healthmanage.R;
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

public class MissionViewArticle extends LinearLayout implements DataInterface {

    @BindView(R.id.list_items)
    RecyclerView list_articles;

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

    public void setMissionNo(int missionNo) {
        MissionNo = missionNo;
    }

    private HomeActivity homeActivity;
    private MissionViewCallBack missionViewCallBack;
    private SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO();
    private List<ArticleBean> articleBeans = new ArrayList<>();
    private List<String> articles = new ArrayList<>();
    private PaperQuickAdapter paperAdapter;
    private List<PaperBean> mPapers = new ArrayList<>();
    private boolean isModify;

    public MissionViewArticle(Context context) {
        super(context);
        initView(context);
    }

    public MissionViewArticle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MissionViewArticle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public MissionViewArticle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        EventBus.getDefault().register(this);
        homeActivity = (HomeActivity) context;
        View.inflate(context, R.layout.layout_misson_articles, this);
        ButterKnife.bind(this);
//        layout_after_end.getLayoutParams().height = Constants.screenHeight / 3;

        initArticleListView();
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    private void initArticleListView() {
        list_articles.setLayoutManager(new LinearLayoutManager(homeActivity));
        list_articles.addItemDecoration(MUtils.spaceDivider(
                DensityUtil.dip2px(homeActivity, homeActivity.getResources().getDimension(R.dimen.qb_px_3)), false));
        paperAdapter = new PaperQuickAdapter(R.layout.item_paper, articleBeans);
        paperAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ARTICLE_DETAIL, articleBeans.get(position));
            }
        });
        paperAdapter.setOnItemDelete(new OnItemDelete() {
            @Override
            public void onItemDelete(int position) {
                articleBeans.remove(position);
                paperAdapter.setNewData(articleBeans);
            }
        });
        list_articles.setAdapter(paperAdapter);
    }


    @OnClick({R.id.layout_add_item, R.id.img_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_add_item:
                if (articleBeans.size() == 1) {
                    ToastUtil.toastShortMessage("仅限添加一篇文章");
                    return;
                }
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_PAPER, new GetMissionObj(this.TaskNo,this.MissionNo));
                break;
            case R.id.img_type:
                if (null != missionViewCallBack) {
                    missionViewCallBack.onDeleteMission();
                }
                break;
        }
    }

    /**
     * 处理订阅消息
     *
     * @param articleBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArticleBean articleBean) {
        if (articleBeans.size() == 1) {
            return;
        }

        //不是当前的任务和项目跳转的获取文章，不添加
        if (articleBean.MissionNo != this.MissionNo || articleBean.TaskNo != this.TaskNo){
            return;
        }

        for (ArticleBean articleBeanOld : articleBeans) {//去重
            if (articleBeanOld.articleId == articleBean.articleId) {
                ToastUtil.toastShortMessage("请勿添加重复的文章");
                return;
            }
        }

        articleBeans.add(articleBean);
        articles.add(articleBean.articleId + "");
        paperAdapter.setNewData(articleBeans);
    }

    public void setMissionViewCallBack(MissionViewCallBack missionViewCallBack) {
        this.missionViewCallBack = missionViewCallBack;
    }

    @Override
    public SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO getData() {
        //写死的类型数据
        templateTaskContentDTO.taskType = "Knowledge";
//        templateTaskContentDTO.contentDetail.remindName = "科普文章";
        if (!isModify) {
            templateTaskContentDTO.contentDetail = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO.ContentDetailDTO();
        }
        //填入的数据
//        templateTaskContentDTO.contentDetail.knowUrl = articleBeans.get(0).content;
        templateTaskContentDTO.contentDetail.knowContent = articleBeans.get(0).content;
        templateTaskContentDTO.contentDetail.articleId = articleBeans.get(0).articleId + "";
        return templateTaskContentDTO;
    }

    @Override
    public PlanDetailResult.UserPlanDetailsDTO getAssembleData() {
        if (articleBeans.size() == 0) {
            return null;
        }
        PlanDetailResult.UserPlanDetailsDTO userPlanDetailsDTO = new PlanDetailResult.UserPlanDetailsDTO();
        userPlanDetailsDTO.planType = "Knowledge";
        userPlanDetailsDTO.planDescribe = "专家团队对您近期的病情变化进行跟踪后，为您选择的科普文章，请及时查阅。";
        userPlanDetailsDTO.execFlag = 0;
        userPlanDetailsDTO.content = articleBeans.get(0).content;
        userPlanDetailsDTO.title = articleBeans.get(0).title;
        return userPlanDetailsDTO;
    }


    public boolean isDataReady() {
        if (articleBeans.size() == 0) {
            ToastUtil.toastShortMessage("请选择文章");
            return false;
        }
        return true;
    }

    public void setMissionData(SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO) {
        this.templateTaskContentDTO = DataUtil.getNotNullData(templateTaskContentDTO);
        ArticleBean articleBean = new ArticleBean();
        if (null != templateTaskContentDTO.contentDetail.articleId) {
            articleBean.articleId = Integer.parseInt(templateTaskContentDTO.contentDetail.articleId);
        }
        articleBean.content = templateTaskContentDTO.contentDetail.knowContent;
        articleBean.title = templateTaskContentDTO.contentDetail.title;
        articleBeans.add(articleBean);
        paperAdapter.setNewData(articleBeans);

        isModify = true;
    }

    public interface MissionViewCallBack {
        void onDeleteMission();

        void onGetMissionData();
    }

}
