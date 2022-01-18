package com.bitvalue.health.ui.activity;


import static com.bitvalue.health.util.Constants.EVENT_MES_TYPE_CLOUDCLINC;
import static com.bitvalue.health.util.Constants.FRAGMENT_DETAIL;
import static com.bitvalue.health.util.Constants.FRAGMENT_NEW_LYDISCHARGED_PATIENT;
import static com.bitvalue.health.util.Constants.LISTBEAN;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.eventbusbean.MainRefreshObj;
import com.bitvalue.health.api.eventbusbean.MsgRemindObj;
import com.bitvalue.health.api.eventbusbean.VideoRefreshObj;
import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.responsebean.ArticleBean;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.api.responsebean.PlanListBean;
import com.bitvalue.health.api.responsebean.TaskPlanDetailBean;
import com.bitvalue.health.api.responsebean.message.AddVideoObject;
import com.bitvalue.health.api.responsebean.message.GetMissionObj;
import com.bitvalue.health.base.BaseActivity;
import com.bitvalue.health.callback.OnTouchListener;
import com.bitvalue.health.contract.homecontract.HomeContract;
import com.bitvalue.health.presenter.homepersenter.HomePersenter;
import com.bitvalue.health.ui.fragment.chat.ChatFragment;
import com.bitvalue.health.ui.fragment.cloudclinic.ConsultingServiceFragment;
import com.bitvalue.health.ui.fragment.healthmanage.AddArticleFragment;
import com.bitvalue.health.ui.fragment.healthmanage.AddRemindFragment;
import com.bitvalue.health.ui.fragment.healthmanage.AddVideoFragment;
import com.bitvalue.health.ui.fragment.healthmanage.ArticleDetailFragment;
import com.bitvalue.health.ui.fragment.healthmanage.FollowUpPlanFragment;
import com.bitvalue.health.ui.fragment.healthmanage.NewDischargedFragment;
import com.bitvalue.health.ui.fragment.healthmanage.PatientDetailFragment;
import com.bitvalue.health.ui.fragment.healthmanage.PatientReportFragment;
import com.bitvalue.health.ui.fragment.cloudclinic.HealthHistoryPreFragment;
import com.bitvalue.health.ui.fragment.cloudclinic.WriteHealthFragment;
import com.bitvalue.health.ui.fragment.docfriend.DocFriendsFragment;
import com.bitvalue.health.ui.fragment.healthmanage.HealthAnalyseDisplayFragment;
import com.bitvalue.health.ui.fragment.healthmanage.HealthAnalyseFragment;
import com.bitvalue.health.ui.fragment.healthmanage.HealthMessageFragment;
import com.bitvalue.health.ui.fragment.healthmanage.HealthPlanDetailFragment;
import com.bitvalue.health.ui.fragment.healthmanage.HealthPlanPreviewFragment;
import com.bitvalue.health.ui.fragment.healthmanage.HealthUploadDataFragment;
import com.bitvalue.health.ui.fragment.healthmanage.NewMsgFragmentDisplay;
import com.bitvalue.health.ui.fragment.healthmanage.PlanMsgFragment;
import com.bitvalue.health.ui.fragment.healthmanage.QuestionDetailFragment;
import com.bitvalue.health.ui.fragment.healthmanage.QuickReplyFragment;
import com.bitvalue.health.ui.fragment.schedule.ScheduleFragment;
import com.bitvalue.health.ui.fragment.healthmanage.AddQuestionFragment;
import com.bitvalue.health.ui.fragment.setting.CreateNewHealthPlanFragment;
import com.bitvalue.health.ui.fragment.setting.HealthPlanFragment;
import com.bitvalue.health.ui.fragment.setting.MedicalRecordsFragment;
import com.bitvalue.health.ui.fragment.setting.NewHealthPlanFragmentModify;
import com.bitvalue.health.ui.fragment.setting.PersonalDataFragment;
import com.bitvalue.health.ui.fragment.setting.SettingsFragment;
import com.bitvalue.health.ui.fragment.workbench.NeedDealtWithFragment;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.modules.chat.base.ChatInfo;
import com.hjq.http.listener.OnHttpListener;
import com.tencent.imsdk.v2.V2TIMConversation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;


/***
 * 主界面  Home
 */
public class HomeActivity extends BaseActivity<HomePersenter> implements HomeContract.TUIKitView, OnHttpListener<Object> {
    @BindView(R.id.layout_person)
    RelativeLayout layout_person;

    @BindView(R.id.layout_settings)
    LinearLayout layout_settings;

    @BindView(R.id.layout_group)
    RelativeLayout layout_group;

    @BindView(R.id.layout_workbench)
    RelativeLayout layout_workbench;

    @BindView(R.id.layout_calendar)
    RelativeLayout layout_calender;

    @BindView(R.id.tv_group)
    TextView tv_group;

    @BindView(R.id.img_group)
    ImageView img_group;

    @BindView(R.id.layout_send)
    RelativeLayout layout_send;

    @BindView(R.id.tv_send)
    TextView tv_send;

    @BindView(R.id.img_send)
    ImageView img_send;

    @BindView(R.id.layout_pot_health)
    LinearLayout layout_pot_health;

    @BindView(R.id.tv_new_count_health)
    TextView tv_new_count_health;

    @BindView(R.id.layout_pot_video)
    LinearLayout layout_pot_video;

    @BindView(R.id.tv_new_count_video)
    TextView tv_new_count_video;

    @BindView(R.id.tv_settings)
    TextView tv_settings;

    @BindView(R.id.tv_chat)
    TextView tv_chat;
    @BindView(R.id.tv_person)
    TextView tv_person;
    @BindView(R.id.img_chat)
    ImageView img_chat;
    @BindView(R.id.img_settings)
    ImageView img_settings;
    @BindView(R.id.img_person)
    ImageView img_person;
    @BindView(R.id.img_wkbench)
    ImageView iv_wkbench;
    @BindView(R.id.img_calendar)
    ImageView iv_calendar;


    @BindView(R.id.tv_calendar)
    TextView tv_calender;
    @BindView(R.id.tv_wkbench)
    TextView tv_wkbench;


    @BindView(R.id.layout_fragment_end)
    FrameLayout frameLayout;

    @BindView(R.id.framelaout_workbench)
    FrameLayout frameLayout_full;


    @BindView(R.id.layout_fragment)
    FrameLayout frameLayout_second;


    private static final int chat_index = 0;
    private static final int settings = 1;
    private static final int video_index = 2;
    private static final int doc_index = 3;
    private static final int work_bench = 4;
    private static final int calendar = 5;
    private int tabPosition = -1;
    private Fragment[] fragmentArrays;
    private Map<String, Fragment> mapFragments = new HashMap<>();
    private SettingsFragment settingsFragment;
    private PatientReportFragment patientReportFragment;
    private ConsultingServiceFragment consultingServiceFragment;
    private DocFriendsFragment docFriendsFragment;
    private NeedDealtWithFragment workbenchFragment;
    private ScheduleFragment scheduleFragment;


    @Override
    protected HomePersenter createPresenter() {
        return new HomePersenter();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, this);
        if (loginBean == null) {
            Log.e(TAG, "initView  loginBean is null ");
            return;
        }
        initFragments(work_bench);  //默认首页工作台界面
        mPresenter.IMLogin(loginBean.getAccount().user.userId + "", loginBean.getAccount().user.userSig);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MsgRemindObj msgRemindObj) {
        if (msgRemindObj.type == EVENT_MES_TYPE_CLOUDCLINC) {
            tv_new_count_video.setText(msgRemindObj.num > 99 ? (msgRemindObj.num + "+") : (msgRemindObj.num + ""));
            layout_pot_video.setVisibility(msgRemindObj.num > 0 ? View.VISIBLE : View.GONE);
        } else {
            tv_new_count_health.setText(msgRemindObj.num > 99 ? (msgRemindObj.num + "+") : (msgRemindObj.num + ""));
            layout_pot_health.setVisibility(msgRemindObj.num > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application.instance().setHomeActivity(this);
    }

    /**
     * 初始化各个Fragment界面
     *
     * @param index
     */
    private void initFragments(int index) {
        patientReportFragment = PatientReportFragment.getInstance(false);
        settingsFragment = SettingsFragment.getInstance(false);
        consultingServiceFragment = ConsultingServiceFragment.getInstance(false);
        docFriendsFragment = DocFriendsFragment.getInstance(false);
        workbenchFragment = NeedDealtWithFragment.getInstance(false);
        scheduleFragment = ScheduleFragment.getInstance(false);

        fragmentArrays = new Fragment[]{patientReportFragment, settingsFragment, consultingServiceFragment, docFriendsFragment, workbenchFragment, scheduleFragment};
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_fragment, fragmentArrays[0]);
        transaction.add(R.id.layout_fragment, fragmentArrays[1]);
        transaction.add(R.id.layout_fragment, fragmentArrays[2]);
        transaction.add(R.id.layout_fragment, fragmentArrays[3]);
        transaction.add(R.id.framelaout_workbench, fragmentArrays[4]);
        transaction.add(R.id.framelaout_workbench, fragmentArrays[5]);
        transaction.commitAllowingStateLoss();
        afterTabSelect(index);
    }

    /**
     * 底部导航栏点击后的跳转  根据索引切换
     */
    private void afterTabSelect(int index) {
        if (index == tabPosition) {
            return;
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragmentArrays.length; i++) {
            if (i != index) {
                supportFragmentManager.beginTransaction().hide(fragmentArrays[i]).commit();
            }
        }

        //如果点击的当前Fragment还没加入集合中 则添加
        if (!fragmentArrays[index].isAdded()) {
            if (index != work_bench && index != calendar) {
                supportFragmentManager.beginTransaction().add(R.id.layout_fragment, fragmentArrays[index]);
            } else {
                supportFragmentManager.beginTransaction().add(R.id.framelaout_workbench, fragmentArrays[index]);
            }
        }
        supportFragmentManager.beginTransaction().show(fragmentArrays[index]).commit();

        tabPosition = index;
        setBottomUi(index);
    }

    /***
     * 根据索引 切换左侧按钮图标状态
     * @param index
     */
    private void setBottomUi(int index) {
        initNaviUI();

        switch (index) {

            //工作台
            case work_bench:
                iv_wkbench.setImageResource(R.drawable.tab_icon_gzt);
                tv_wkbench.setTextColor(getResources().getColor(R.color.white));
                layout_workbench.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;

            //日程
            case calendar:
                iv_calendar.setImageResource(R.drawable.tab_icon_cy);
                tv_calender.setTextColor(getResources().getColor(R.color.white));
                layout_calender.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;



//            健康计划
            case chat_index:
                img_person.setImageResource(R.drawable.tab_icon_jkgl);
                tv_person.setTextColor(getResources().getColor(R.color.white));
                layout_person.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;

//                云门诊
            case video_index:
                img_group.setImageResource(R.drawable.tab_icon_ct);
                tv_group.setTextColor(getResources().getColor(R.color.white));
                layout_group.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;

//                医生好友
            case doc_index:
                img_send.setImageResource(R.drawable.tab_icon_gr);
                tv_send.setTextColor(getResources().getColor(R.color.white));
                layout_send.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;

            //     设置
            case settings:
                img_settings.setImageResource(R.drawable.tab_icon_set);
                tv_settings.setTextColor(getResources().getColor(R.color.white));
                layout_settings.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;
        }
    }


    /**
     * 背景选择 点击不同按钮 显示不同颜色
     */
    private void initNaviUI() {

        iv_wkbench.setImageResource(R.drawable.tab_icon_gzt);
//        tv_wkbench.setTextColor(getResources().getColor(R.color.gray));
        layout_workbench.setBackgroundColor(getResources().getColor(R.color.home_blue));


        iv_calendar.setImageResource(R.drawable.tab_icon_cy);
//        tv_calender.setTextColor(getResources().getColor(R.color.gray));
        layout_calender.setBackgroundColor(getResources().getColor(R.color.home_blue));


        img_person.setImageResource(R.drawable.tab_icon_jkgl);
//        tv_person.setTextColor(getResources().getColor(R.color.gray));
        layout_person.setBackgroundColor(getResources().getColor(R.color.home_blue));


        img_group.setImageResource(R.drawable.tab_icon_ct);
//        tv_group.setTextColor(getResources().getColor(R.color.gray));
        layout_group.setBackgroundColor(getResources().getColor(R.color.home_blue));

        img_send.setImageResource(R.drawable.tab_icon_gr);
//        tv_send.setTextColor(getResources().getColor(R.color.gray));
        layout_send.setBackgroundColor(getResources().getColor(R.color.home_blue));

        img_settings.setImageResource(R.drawable.tab_icon_set);
//        tv_settings.setTextColor(getResources().getColor(R.color.gray));
        layout_settings.setBackgroundColor(getResources().getColor(R.color.home_blue));
    }

    /**
     * HomeActivity切换从左往右第二个fragment的方法
     * 流程：
     * 判断切换到的fragment类型
     * 传参数新增fragment
     *
     * @param keyFragment
     * @param object
     */
    public void switchSecondFragment(String keyFragment, Object object) {
        boolean isContain = mapFragments.containsKey(keyFragment);
        if (isContain) {
            mapFragments.remove(keyFragment);
        }
        switch (keyFragment) {

            //聊天界面
            case Constants.FRAGMENT_CHAT:
                NewLeaveBean.RowsDTO child = (NewLeaveBean.RowsDTO) object;

                ChatFragment chatFragment = new ChatFragment();

                Bundle bundle = new Bundle();
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                chatInfo.setId(String.valueOf(child.getUserId()));
                chatInfo.setChatName(child.getUserName());

                bundle.putSerializable(Constants.CHAT_INFO, chatInfo);
                bundle.putSerializable(Constants.USERINFO, child);
                chatFragment.setArguments(bundle);
                mapFragments.put(Constants.FRAGMENT_CHAT, chatFragment);
                break;

            //套餐配置
            case Constants.FRAGMENT_HEALTH_PLAN:
                HealthPlanFragment healthPlanFragment;
                healthPlanFragment = new HealthPlanFragment();
                mapFragments.put(Constants.FRAGMENT_HEALTH_PLAN, healthPlanFragment);
                break;
//      创建健康管理计划
            case Constants.FRAGMENT_HEALTH_NEW:
                CreateNewHealthPlanFragment newHealthPlanFragment;
                newHealthPlanFragment = new CreateNewHealthPlanFragment();
                mapFragments.put(Constants.FRAGMENT_HEALTH_NEW, newHealthPlanFragment);
                break;

            //修改健康管理计划
            case Constants.FRAGMENT_HEALTH_MODIFY:
                PlanListBean planListBean = (PlanListBean) object;
                NewHealthPlanFragmentModify newHealthPlanFragmentModify;
                newHealthPlanFragmentModify = new NewHealthPlanFragmentModify();
                Bundle bundleModify = new Bundle();
                bundleModify.putSerializable(Constants.PLAN_LIST_BEAN, planListBean);
                newHealthPlanFragmentModify.setArguments(bundleModify);
                mapFragments.put(Constants.FRAGMENT_HEALTH_MODIFY, newHealthPlanFragmentModify);
                break;
//         添加文章界面
            case Constants.FRAGMENT_ADD_PAPER:
//                GetMissionObj getMissionPaper = (GetMissionObj) object;
//                AddArticleFragment addArticleFragment;
//                addArticleFragment = new AddArticleFragment();
//                Bundle bundlePaper = new Bundle();
//                bundlePaper.putSerializable(Constants.GET_MISSION_OBJ, getMissionPaper);
//                addArticleFragment.setArguments(bundlePaper);

                AddArticleFragment addArticleFragment  = new AddArticleFragment();
                mapFragments.put(Constants.FRAGMENT_ADD_PAPER, addArticleFragment);
                break;

            //添加问卷界面
            case Constants.FRAGMENT_ADD_QUESTION:
//                GetMissionObj getMissionObj = (GetMissionObj) object;
//                AddQuestionFragment addQuestionFragment;
//                addQuestionFragment = new AddQuestionFragment();
//                Bundle bundleQue = new Bundle();
//                bundleQue.putSerializable(Constants.GET_MISSION_OBJ, getMissionObj);
//                addQuestionFragment.setArguments(bundleQue);


                AddQuestionFragment addQuestionFragment = new AddQuestionFragment();

                mapFragments.put(Constants.FRAGMENT_ADD_QUESTION, addQuestionFragment);
                break;

            //健康消息
            case Constants.FRAGMENT_SEND_MSG:
                ChatFragment.NewMsgData msgData = (ChatFragment.NewMsgData) object;
                HealthMessageFragment newMsgFragment = new HealthMessageFragment();

                Bundle msgBundle = new Bundle();
                msgBundle.putString(Constants.MSG_TYPE, msgData.msgType);
                msgBundle.putStringArrayList(Constants.MSG_IDS, msgData.userIds);
                newMsgFragment.setArguments(msgBundle);
                mapFragments.put(Constants.FRAGMENT_SEND_MSG, newMsgFragment);
                break;

            //聊天界面  点击发出去的信息 展示界面
            case Constants.FRAGMENT_SEND_MSG_DISPLAY:
                ChatFragment.NewMsgData msgDataDis = (ChatFragment.NewMsgData) object;
                NewMsgFragmentDisplay newMsgFragmentDisplay;
                newMsgFragmentDisplay = new NewMsgFragmentDisplay();

                Bundle msgBundleDis = new Bundle();
                msgBundleDis.putString(Constants.MSG_TYPE, msgDataDis.msgType);
                msgBundleDis.putString(Constants.MSG_CUSTOM_ID, msgDataDis.id);//显示详情的时候才要
                msgBundleDis.putStringArrayList(Constants.MSG_IDS, msgDataDis.userIds);
                newMsgFragmentDisplay.setArguments(msgBundleDis);
                mapFragments.put(Constants.FRAGMENT_SEND_MSG_DISPLAY, newMsgFragmentDisplay);
                break;
            /***
             * 健康评估
             */
            case Constants.FRAGMENT_HEALTH_ANALYSE:
                HealthAnalyseFragment healthAnalyseFragment;
                healthAnalyseFragment = new HealthAnalyseFragment();

                Bundle msgBundleAnalyse = new Bundle();
                ChatFragment.NewMsgData msgDataAnalyse = (ChatFragment.NewMsgData) object;
//                msgBundleAnalyse.putString(Constants.MSG_CUSTOM_ID, msgDataAnalyse.id);//显示详情的时候才要
                msgBundleAnalyse.putStringArrayList(Constants.MSG_IDS, msgDataAnalyse.userIds);//传入消息接受者的userId
                msgBundleAnalyse.putString(Constants.PLAN_ID, msgDataAnalyse.id);
                healthAnalyseFragment.setArguments(msgBundleAnalyse);
                mapFragments.put(Constants.FRAGMENT_HEALTH_ANALYSE, healthAnalyseFragment);
                break;
//     上传个人信息
            case Constants.FRAGMENT_USER_DATA:
                HealthUploadDataFragment healthUploadDataFragment;
                healthUploadDataFragment = new HealthUploadDataFragment();

                Bundle bundleData = new Bundle();
                CustomHealthDataMessage customHealthDataMessage = (CustomHealthDataMessage) object;
                bundleData.putSerializable(Constants.DATA_MSG, customHealthDataMessage);
                healthUploadDataFragment.setArguments(bundleData);
                mapFragments.put(Constants.FRAGMENT_USER_DATA, healthUploadDataFragment);
                break;
            //  健康评估详情展示
            case Constants.FRAGMENT_HEALTH_ANALYSE_DISPLAY:
                HealthAnalyseDisplayFragment healthAnalyseFragmentDisplay;
                healthAnalyseFragmentDisplay = new HealthAnalyseDisplayFragment();

                Bundle msgBundleAnalyseDis = new Bundle();
                ChatFragment.NewMsgData msgDataAnalyseDis = (ChatFragment.NewMsgData) object;
//                msgBundleAnalyse.putString(Constants.MSG_CUSTOM_ID, msgDataAnalyse.id);//显示详情的时候才要
                msgBundleAnalyseDis.putStringArrayList(Constants.MSG_IDS, msgDataAnalyseDis.userIds);//传入消息接受者的userId
                msgBundleAnalyseDis.putString(Constants.MSG_CUSTOM_ID, msgDataAnalyseDis.id);//显示详情的时候才要
                healthAnalyseFragmentDisplay.setArguments(msgBundleAnalyseDis);
                mapFragments.put(Constants.FRAGMENT_HEALTH_ANALYSE_DISPLAY, healthAnalyseFragmentDisplay);
                break;
//   健康计划详细界面
            case Constants.FRAGMENT_HEALTH_PLAN_DETAIL:
                ChatFragment.NewMsgData msgDataDetail = (ChatFragment.NewMsgData) object;
                HealthPlanDetailFragment healthPlanDetailFragment;
                healthPlanDetailFragment = new HealthPlanDetailFragment();
                Bundle bundleDetail = new Bundle();
                bundleDetail.putStringArrayList(Constants.MSG_IDS, msgDataDetail.userIds);
                bundleDetail.putString(Constants.PLAN_ID, msgDataDetail.id);
                healthPlanDetailFragment.setArguments(bundleDetail);
                mapFragments.put(Constants.FRAGMENT_HEALTH_PLAN_DETAIL, healthPlanDetailFragment);
                break;

            //添加视频界面
            case Constants.FRAGMENT_ADD_VIDEO:
                AddVideoObject addVideoObject = (AddVideoObject) object;
                Bundle bundleMsg = new Bundle();
                bundleMsg.putSerializable(Constants.ADD_VIDEO_DATA, addVideoObject);
                AddVideoFragment addVideoFragment;
                addVideoFragment = new AddVideoFragment();
                addVideoFragment.setArguments(bundleMsg);
                mapFragments.put(Constants.FRAGMENT_ADD_VIDEO, addVideoFragment);
                break;
//

            /**
             * 问卷调查界面
             */
            case Constants.FRAGMENT_QUESTION_DETAIL:
                QuestionResultBean.ListDTO questionBean = (QuestionResultBean.ListDTO) object;
                Bundle bundleQuest = new Bundle();
                bundleQuest.putSerializable(Constants.QUESTION_DETAIL, questionBean);
                QuestionDetailFragment questionDetailFragment = new QuestionDetailFragment();
                questionDetailFragment.setArguments(bundleQuest);
                mapFragments.put(Constants.FRAGMENT_QUESTION_DETAIL, questionDetailFragment);
                break;
            //   文章预览界面
            case Constants.FRAGMENT_ARTICLE_DETAIL:
                ArticleBean articleBean = (ArticleBean) object;
                Bundle bundleArticle = new Bundle();
                bundleArticle.putSerializable(Constants.ARTICLE_DETAIL, articleBean);
                ArticleDetailFragment articleDetailFragment = new ArticleDetailFragment();
                articleDetailFragment.setArguments(bundleArticle);
                mapFragments.put(Constants.FRAGMENT_ARTICLE_DETAIL, articleDetailFragment);
                break;


            // 健康提醒界面
            case Constants.FRAGMENT_PLAN_MSG:
                TaskPlanDetailBean taskPlanDetailBean = (TaskPlanDetailBean) object;
                Bundle bundleTP = new Bundle();
                bundleTP.putSerializable(Constants.PLAN_MSG, taskPlanDetailBean);
                PlanMsgFragment planMsgFragment = new PlanMsgFragment();
                planMsgFragment.setArguments(bundleTP);
                mapFragments.put(Constants.FRAGMENT_PLAN_MSG, planMsgFragment);
                break;
//

            //健康套餐计划 预览
            case Constants.FRAGMENT_HEALTH_PLAN_PREVIEW:
                PlanDetailResult planDetailResult = (PlanDetailResult) object;
                Bundle bundlePre = new Bundle();
                bundlePre.putSerializable(Constants.PLAN_PREVIEW, planDetailResult);
                HealthPlanPreviewFragment healthPlanPreviewFragment;
                healthPlanPreviewFragment = new HealthPlanPreviewFragment();
                healthPlanPreviewFragment.setArguments(bundlePre);
                mapFragments.put(Constants.FRAGMENT_HEALTH_PLAN_PREVIEW, healthPlanPreviewFragment);
                break;
//
            /**
             * 书写病历 界面切换
             */
            case Constants.FRAGMENT_WRITE_HEALTH:
                ChatFragment.NewMsgData msgDataWrite = (ChatFragment.NewMsgData) object;
                WriteHealthFragment writeHealthFragment;
                writeHealthFragment = new WriteHealthFragment();
                Bundle bundleWrite = new Bundle();
                bundleWrite.putStringArrayList(Constants.MSG_IDS, msgDataWrite.userIds);
                bundleWrite.putString(Constants.PLAN_ID, msgDataWrite.id);
                writeHealthFragment.setArguments(bundleWrite);
                mapFragments.put(Constants.FRAGMENT_WRITE_HEALTH, writeHealthFragment);
                break;

            /**
             * 病历预览界面切换
             */
            case Constants.FRAGMENT_HEALTH_HISTORY_PREVIEW:
                ChatFragment.NewMsgData msgPreview = (ChatFragment.NewMsgData) object;
                HealthHistoryPreFragment healthHistoryPreFragment;
                healthHistoryPreFragment = new HealthHistoryPreFragment();
                Bundle bundlePreview = new Bundle();
                bundlePreview.putSerializable(Constants.DATA_MSG, msgPreview);
                healthHistoryPreFragment.setArguments(bundlePreview);
                mapFragments.put(Constants.FRAGMENT_HEALTH_HISTORY_PREVIEW, healthHistoryPreFragment);
                break;

            //医生个人信息界面
            case Constants.FRAGMENT_PERSONAL_DATA:
                PersonalDataFragment personalDataFragment = new PersonalDataFragment();
                mapFragments.put(Constants.FRAGMENT_PERSONAL_DATA, personalDataFragment);
                break;
            //个人设置中的看诊记录界面
            case Constants.FRAGMENT_CHAT_LOG:
                MedicalRecordsFragment chatLogFragment = new MedicalRecordsFragment();
                mapFragments.put(Constants.FRAGMENT_CHAT_LOG, chatLogFragment);
                break;

                //新出院患者界面
            case Constants.FRAGMENT_NEW_LYDISCHARGED_PATIENT:
                NewDischargedFragment dischargedFragment = new NewDischargedFragment();
                mapFragments.put(Constants.FRAGMENT_NEW_LYDISCHARGED_PATIENT, dischargedFragment);
                break;

                //发送提醒界面
            case Constants.FRAGMENT_SEND_REMIND:
                AddRemindFragment addRemindFragment = new AddRemindFragment();
                mapFragments.put(Constants.FRAGMENT_SEND_REMIND, addRemindFragment);
                break;
                //快捷回复界面
            case Constants.FRAGMENT_QUICKREPLY:
                QuickReplyFragment quickReplyFragment = new QuickReplyFragment();
                mapFragments.put(Constants.FRAGMENT_QUICKREPLY, quickReplyFragment);
                break;

                //计划列表界面
            case Constants.FRAGMENT_PLAN_LIST:
                List<NewLeaveBean.RowsDTO> listBean = (List<NewLeaveBean.RowsDTO>) object;
                FollowUpPlanFragment followUpPlanFragment = new FollowUpPlanFragment();
                Bundle bundle_followup = new Bundle();
                bundle_followup.putSerializable(LISTBEAN, (Serializable) listBean);
                followUpPlanFragment.setArguments(bundle_followup);
                mapFragments.put(Constants.FRAGMENT_PLAN_LIST, followUpPlanFragment);
                break;

                //患者详情界面
            case Constants.FRAGMENT_DETAIL:
                NewLeaveBean.RowsDTO  patientitem = (NewLeaveBean.RowsDTO) object;
                PatientDetailFragment patientDetailFragment = new PatientDetailFragment();
                Bundle bundle_detail = new Bundle();
                bundle_detail.putSerializable(FRAGMENT_DETAIL, (Serializable) patientitem);
                patientDetailFragment.setArguments(bundle_detail);
                mapFragments.put(FRAGMENT_DETAIL, patientDetailFragment);
                break;



            //患者资料详情  （暂时不用）
//            case Constants.FRAGMENT_VIDEO_PATIENT_DATA:
//                VideoPatientDataFragment videoPatientDataFragment;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                videoPatientDataFragment = new VideoPatientDataFragment();
//                CustomPatientDataMessage customPatientDataMessage = (CustomPatientDataMessage) object;
//                Bundle bundlePatient = new Bundle();
//                bundlePatient.putSerializable(Constants.DATA_MSG, customPatientDataMessage);
//                videoPatientDataFragment.setArguments(bundlePatient);
//                mapFragments.put(Constants.FRAGMENT_VIDEO_PATIENT_DATA, videoPatientDataFragment);
//                break;

            //用药指导  （暂时不用）
//            case Constants.FRAGMENT_MEDICINE_GUIDE:
//                MedicineGuideFragment medicineGuideFragment;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                medicineGuideFragment = new MedicineGuideFragment();
//                mapFragments.put(Constants.FRAGMENT_MEDICINE_GUIDE, medicineGuideFragment);
//                break;
        }
        Set<String> strings = mapFragments.keySet();
        List<String> stringList = new ArrayList<>(strings);
//
//
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        for (int i = 0; i < mapFragments.size(); i++) {
            if (!stringList.get(i).equals(keyFragment)) {
                fragmentTransaction.hide(mapFragments.get(stringList.get(i)));
            }
        }
        if (!mapFragments.get(keyFragment).isAdded()) {
            fragmentTransaction.add(R.id.layout_fragment_end, mapFragments.get(keyFragment));
        }
        supportFragmentManager.popBackStack(keyFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.addToBackStack(keyFragment);
        fragmentTransaction.show(mapFragments.get(keyFragment)).commit();
    }


    /***
     * 各个子控件的点击事件
     * @param view
     */
    @OnClick({R.id.layout_person, R.id.layout_settings, R.id.layout_group, R.id.layout_send, R.id.layout_fragment_end, R.id.layout_workbench, R.id.layout_calendar})
    public void clickleftbutton(View view) {
        switch (view.getId()) {
//            工作台点击
            case R.id.layout_workbench:
                //这里不能让一直连续点 做个限制
                if (tabPosition != 4) {
                    backAllThirdAct();
                } else {
                    return;
                }
                frameLayout_full.setVisibility(View.VISIBLE);
                afterTabSelect(4);
                break;


//                日程点击事件
            case R.id.layout_calendar:
                if (tabPosition != 5) {
                    backAllThirdAct();
                } else {
                    return;
                }
                frameLayout_full.setVisibility(View.VISIBLE);
                afterTabSelect(5);
                break;


            //患者报道按钮点击事件
            case R.id.layout_person:
                //这里不能让一直连续点 做个限制
                if (tabPosition != 0) {
                    backAllThirdAct();
                } else {
                    return;
                }
                frameLayout_full.setVisibility(View.GONE);
//                switchSecondFragment(FRAGMENT_NEW_LYDISCHARGED_PATIENT,"");
                EventBus.getDefault().post(new MainRefreshObj()); // 通知健康管理界面获取数据 请求接口
                afterTabSelect(0);
                break;

            //个人设置按钮 点击事件
            case R.id.layout_settings:
                if (tabPosition != 1) {
                    backAllThirdAct();
                } else {
                    return;
                }
                frameLayout_full.setVisibility(View.GONE);
                afterTabSelect(1);
                break;

            //咨询按钮点击事件
            case R.id.layout_group:
                if (tabPosition != 2) {
                    backAllThirdAct();
                } else {   //add
                    return;
                }
                frameLayout_full.setVisibility(View.GONE);
                EventBus.getDefault().post(new VideoRefreshObj());
                afterTabSelect(2);

                //点击之后隐藏红点
                layout_pot_video.setVisibility(View.GONE);
                tv_new_count_video.setText("0");
                break;

            //医生好友按钮点击事件
            case R.id.layout_send:
//                EventBus.getDefault().post(new VideoRefreshObj());
                if (tabPosition != 3) {
                    backAllThirdAct();
                } else {
                    return;
                }
                frameLayout_full.setVisibility(View.GONE);
                afterTabSelect(3);
                break;


            //第三个Fragment界面事件点击
            case R.id.layout_fragment_end:
                if (null != onTouchListener) {
                    onTouchListener.onthirdFragmentListenner();
                }
                frameLayout_full.setVisibility(View.GONE);
                break;

        }
    }

    //切换tab时回退所有的三级页面
    private void backAllThirdAct() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackEntryCount; i++) {
            getSupportFragmentManager().popBackStack();
        }
    }


    //腾讯IM登录成功回调
    @Override
    public void LoginSuccess(Object o) {
        Log.e(TAG, "LoginSuccess ");
    }


    //腾讯IM登录失败
    @Override
    public void LoginFail(String module, int code, String desc) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSucceed(Object result) {

    }

    @Override
    public void onFail(Exception e) {

    }


    private OnTouchListener onTouchListener;

    public void setOnTouchListener(OnTouchListener listener) {
        onTouchListener = listener;
    }


}
