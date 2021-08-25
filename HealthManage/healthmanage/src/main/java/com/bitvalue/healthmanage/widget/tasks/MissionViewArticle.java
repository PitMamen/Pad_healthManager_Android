package com.bitvalue.healthmanage.widget.tasks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.http.glide.GlideApp;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.PaperBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.adapter.PaperQuickAdapter;
import com.bitvalue.healthmanage.util.DensityUtil;
import com.bitvalue.healthmanage.util.MUtils;
import com.bitvalue.healthmanage.widget.DataUtil;
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

public class MissionViewArticle extends LinearLayout implements DataInterface {

    @BindView(R.id.list_items)
    RecyclerView list_articles;

    @BindView(R.id.img_type)
    ImageView img_type;

    @BindView(R.id.tv_mission_name)
    TextView tv_mission_name;

    @BindView(R.id.layout_add_item)
    LinearLayout layout_add_item;

    private HomeActivity homeActivity;
    private MissionViewCallBack missionViewCallBack;
    private SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO();
    private List<ArticleBean> articleBeans = new ArrayList<>();
    private List<String> articles = new ArrayList<>();
    private PaperQuickAdapter paperAdapter;
    private List<PaperBean> mPapers = new ArrayList<>();

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
                PaperBean uploadFileApi = mPapers.get(position);
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
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_PAPER, "");
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
        articleBeans.add(articleBean);
//        paperAdapter.notifyDataSetChanged();//TODO 刷新数据
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
        templateTaskContentDTO.contentDetail = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO.ContentDetailDTO();
        //填入的数据
//        templateTaskContentDTO.contentDetail.knowUrl = articleBeans.get(0).content;
        templateTaskContentDTO.contentDetail.knowContent = articleBeans.get(0).content;
        templateTaskContentDTO.contentDetail.articleId = articleBeans.get(0).articleId + "";
        return templateTaskContentDTO;
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
        articleBean.title = templateTaskContentDTO.contentDetail.title;
        articleBeans.add(articleBean);
        paperAdapter.setNewData(articleBeans);
    }

    public interface MissionViewCallBack {
        void onDeleteMission();

        void onGetMissionData();
    }

}
