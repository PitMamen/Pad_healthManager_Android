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
import com.bitvalue.healthmanage.widget.tasks.bean.SavePlanApi;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayoutUI;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MissionViewQuestion extends LinearLayout implements DataInterface{

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
    private PaperQuickAdapter paperAdapter;
    private List<ArticleBean> articleBeans = new ArrayList<>();
    private List<PaperBean> mPapers = new ArrayList<>();
    private List<String> articles = new ArrayList<>();

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
        homeActivity = (HomeActivity) context;
        View.inflate(context, R.layout.layout_misson_question, this);
        ButterKnife.bind(this);
//        layout_after_end.getLayoutParams().height = Constants.screenHeight / 3;

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

    @OnClick({R.id.layout_add_item,R.id.img_type})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.layout_add_item:
                if (articleBeans.size() == 1) {
                    ToastUtil.toastShortMessage("仅限添加一个问卷");
                    return;
                }
                homeActivity.switchSecondFragment(Constants.FRAGMENT_ADD_PAPER, "");//TODO 添加问卷
                break;
            case R.id.img_type:
                if (null != missionViewCallBack){
                    missionViewCallBack.onDeleteMission();
                }
                break;
        }
    }

    public void setMissionViewCallBack(MissionViewCallBack missionViewCallBack){
        this.missionViewCallBack = missionViewCallBack;
    }

    @Override
    public SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO getData() {
        //写死的类型数据
        templateTaskContentDTO.taskType = "Quest";
//        templateTaskContentDTO.contentDetail.remindName = "健康问卷";
        templateTaskContentDTO.contentDetail = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO.ContentDetailDTO();
        //填入的数据
        templateTaskContentDTO.contentDetail.questName = "";//TODO
        templateTaskContentDTO.contentDetail.questId = "";//TODO
        return templateTaskContentDTO;
    }

    public interface MissionViewCallBack {
        void onDeleteMission();

        void onGetMissionData();
    }

}
