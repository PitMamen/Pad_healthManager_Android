package com.bitvalue.healthmanage.ui.activity.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bitvalue.healthmanage.util.ClickUtils;
import com.bitvalue.healthmanage.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.base.AppActivity;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.base.AppFragment;
import com.bitvalue.healthmanage.http.bean.ArticleBean;
import com.bitvalue.healthmanage.http.bean.ClientsResultBean;
import com.bitvalue.healthmanage.http.bean.LoginBean;
import com.bitvalue.healthmanage.http.bean.PlanDetailResult;
import com.bitvalue.healthmanage.http.bean.PlanListBean;
import com.bitvalue.healthmanage.http.bean.QuestionResultBean;
import com.bitvalue.healthmanage.http.bean.TaskPlanDetailBean;
import com.bitvalue.healthmanage.http.bean.AddVideoObject;
import com.bitvalue.healthmanage.widget.manager.ActivityManager;
import com.bitvalue.healthmanage.http.bean.MainRefreshObj;
import com.bitvalue.healthmanage.http.bean.MsgRemindObj;
import com.bitvalue.healthmanage.http.bean.VideoRefreshObj;
import com.bitvalue.healthmanage.ui.fragment.common.ChatFragment;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.AddArticleFragment;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.AddVideoFragment;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.ArticleDetailFragment;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.ContactsFragment;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.HealthAnalyseFragment;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.HealthAnalyseFragmentDisplay;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.HealthPlanDetailFragment;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.HealthUploadDataFragment;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.NewMsgFragment;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.NewMsgFragmentDisplay;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.PlanMsgFragment;
import com.bitvalue.healthmanage.ui.fragment.function.healthmanage.QuestionDetailFragment;
import com.bitvalue.healthmanage.ui.fragment.function.setting.AddQuestionFragment;
import com.bitvalue.healthmanage.ui.fragment.function.setting.ChatLogFragment;
import com.bitvalue.healthmanage.ui.fragment.function.setting.HealthPlanFragment;
import com.bitvalue.healthmanage.ui.fragment.MedicineGuideFragment;
import com.bitvalue.healthmanage.ui.fragment.function.setting.HealthPlanPreviewFragment;
import com.bitvalue.healthmanage.ui.fragment.function.setting.NewHealthPlanFragment;
import com.bitvalue.healthmanage.ui.fragment.function.setting.NewHealthPlanFragmentModify;
import com.bitvalue.healthmanage.ui.fragment.function.setting.PersonalDataFragment;
import com.bitvalue.healthmanage.ui.fragment.function.setting.SettingsFragment;
import com.bitvalue.healthmanage.ui.fragment.function.virtualclinic.DocContactsFragment;
import com.bitvalue.healthmanage.ui.fragment.function.virtualclinic.HealthHistoryPreFragment;
import com.bitvalue.healthmanage.ui.fragment.function.virtualclinic.VideoContactsFragment;
import com.bitvalue.healthmanage.ui.fragment.function.virtualclinic.VideoPatientDataFragment;
import com.bitvalue.healthmanage.ui.fragment.function.virtualclinic.WriteHealthFragment;
import com.bitvalue.healthmanage.util.DemoLog;
import com.bitvalue.healthmanage.widget.manager.SharedPreManager;
import com.bitvalue.healthmanage.widget.tasks.bean.GetMissionObj;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.helper.CustomPatientDataMessage;
import com.bitvalue.sdk.collab.modules.chat.base.ChatInfo;
import com.tencent.imsdk.v2.V2TIMConversation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends AppActivity {

    @BindView(R.id.layout_person)
    RelativeLayout layout_person;

    @BindView(R.id.layout_settings)
    LinearLayout layout_settings;

    @BindView(R.id.layout_group)
    RelativeLayout layout_group;

    @BindView(R.id.tv_group)
    TextView tv_group;

    @BindView(R.id.img_group)
    ImageView img_group;

    @BindView(R.id.layout_send)
    LinearLayout layout_send;

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

    public static final String TAG = HomeActivity.class.getSimpleName();
    private static final int chat_index = 0;
    private static final int settings = 1;
    private static final int video_index = 2;
    private static final int doc_index = 3;
    private AppFragment[] fragments;
    private int tabPosition = -1;
    //主界面存放多个子Fragment的集合
    private Map<String, Fragment> mapFragments = new HashMap<>();
    //健康管理联系人列表界面
    private ContactsFragment contactsFragment;
    //虚拟诊间 视频问诊联系人列表
    private VideoContactsFragment videoContactsFragment;
    //医生好友 视频问诊联系人列表
    private DocContactsFragment docContactsFragment;
    //设置Fragment
    private SettingsFragment settingsFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        //adb logcat -v time >D:\log.txt
        //初始化各个Fragment  默认第一个(健康管理客户)
        initFragments(0);
        //登录腾讯IM
        loginIM();
    }

    private void loginIM() {
        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, this);
        if (loginBean == null) {
            return;
        }
        TUIKit.login(loginBean.getAccount().user.userId + "", loginBean.getAccount().user.userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(() -> DemoLog.e("TUIKit.login", "登录聊天失败" + ", errCode = " + code + ", errInfo = " + desc));
            }

            @Override
            public void onSuccess(Object data) {
                // 跳转到首页
                // HomeActivity.start(getContext(), MeFragment.class);
                SharedPreManager.putBoolean(Constants.KEY_IM_AUTO_LOGIN, true, AppApplication.instance());
            }
        });
    }

//    初始化各个Fragment 并按索引显示
    private void initFragments(int index) {
        contactsFragment = ContactsFragment.getInstance(false);
//        newsFragment = NewsFragment.getInstance(false);
        settingsFragment = SettingsFragment.getInstance(false);
        videoContactsFragment = VideoContactsFragment.getInstance(false);
        docContactsFragment = DocContactsFragment.getInstance(false);
        fragments = new AppFragment[]{contactsFragment, settingsFragment, videoContactsFragment, docContactsFragment};
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_fragment, fragments[0]);
        transaction.add(R.id.layout_fragment, fragments[1]);
        transaction.add(R.id.layout_fragment, fragments[2]);
        transaction.add(R.id.layout_fragment, fragments[3]);
        transaction.commitAllowingStateLoss();
        afterTabSelect(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppApplication.instance().setHomeActivity(this);
    }

    @Override
    protected void initData() {
    }


    /**
     * 左侧导航栏点击后的跳转
     */
    private void afterTabSelect(int index) {
        if (index == tabPosition) {
            return;
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragments.length; i++) {
            if (i != index) {
                supportFragmentManager.beginTransaction().hide(fragments[i]).commit();
            }
        }
        if (!fragments[index].isAdded()) {
            supportFragmentManager.beginTransaction().add(R.id.layout_fragment, fragments[index]);
        }
        supportFragmentManager.beginTransaction().show(fragments[index]).commit();

        tabPosition = index;
        setBottomUi(index);
    }

//    点击左侧模块按钮 切换背景及颜色
    private void setBottomUi(int index) {
        initNaviUI();

        switch (index) {
            case chat_index:
                img_person.setImageResource(R.drawable.tab_icon_ct);
                tv_person.setTextColor(getResources().getColor(R.color.white));
                layout_person.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;

            case settings:
                img_settings.setImageResource(R.drawable.tab_icon_set);
                tv_settings.setTextColor(getResources().getColor(R.color.white));
                layout_settings.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;

            case video_index:
                img_group.setImageResource(R.drawable.tab_icon_gr);
                tv_group.setTextColor(getResources().getColor(R.color.white));
                layout_group.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;

            case doc_index:
                img_send.setImageResource(R.drawable.tab_icon_of);
                tv_send.setTextColor(getResources().getColor(R.color.white));
                layout_send.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;
        }
    }

//    点击不同图标的颜色背景转换
    private void initNaviUI() {
        img_person.setImageResource(R.drawable.tab_icon_ct);
        tv_person.setTextColor(getResources().getColor(R.color.gray));
        layout_person.setBackgroundColor(getResources().getColor(R.color.home_blue));

        img_settings.setImageResource(R.drawable.tab_icon_set);
        tv_settings.setTextColor(getResources().getColor(R.color.gray));
        layout_settings.setBackgroundColor(getResources().getColor(R.color.home_blue));

        img_group.setImageResource(R.drawable.tab_icon_gr);
        tv_group.setTextColor(getResources().getColor(R.color.gray));
        layout_group.setBackgroundColor(getResources().getColor(R.color.home_blue));

        img_send.setImageResource(R.drawable.tab_icon_of);
        tv_send.setTextColor(getResources().getColor(R.color.gray));
        layout_send.setBackgroundColor(getResources().getColor(R.color.home_blue));
    }

    @OnClick({R.id.layout_person, R.id.layout_settings, R.id.layout_group, R.id.layout_send})
    public void onClick(View view) {
        switch (view.getId()) {
//            健康管理客户联系人
            case R.id.layout_person:
                if (tabPosition != 0) {
                    backAllThirdAct();
                }
                EventBus.getDefault().post(new MainRefreshObj());  //这里通知ContactFragment 请求数据
                afterTabSelect(0);
                break;
//                个人设置
            case R.id.layout_settings:
                if (tabPosition != 1) {
                    backAllThirdAct();
                }
                afterTabSelect(1);
                break;
//                  虚拟诊间
            case R.id.layout_group:
                EventBus.getDefault().post(new VideoRefreshObj());
                if (tabPosition != 2) {
                    backAllThirdAct();
                }
                Log.e("FSB", "-------------------" );
                afterTabSelect(2);

                //点击之后隐藏红点
                layout_pot_video.setVisibility(View.GONE);
                tv_new_count_video.setText("0");
                break;

//                医生好友
            case R.id.layout_send:
//                EventBus.getDefault().post(new VideoRefreshObj());
                if (tabPosition != 3) {
                    backAllThirdAct();
                }
                afterTabSelect(3);
                break;
        }
    }

//    各Fragment发送过来的 未读取消息  type = 1 健康管理,type = 2  云门诊
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MsgRemindObj msgRemindObj) {
        if (msgRemindObj.type == 2) {
            tv_new_count_video.setText(msgRemindObj.num > 99 ? (msgRemindObj.num + "+") : (msgRemindObj.num + ""));
            layout_pot_video.setVisibility(msgRemindObj.num > 0 ? View.VISIBLE : View.GONE);
        } else {
            tv_new_count_health.setText(msgRemindObj.num > 99 ? (msgRemindObj.num + "+") : (msgRemindObj.num + ""));
            layout_pot_health.setVisibility(msgRemindObj.num > 0 ? View.VISIBLE : View.GONE);
        }
    }

    //切换tab时回退所有的三级页面
    private void backAllThirdAct() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackEntryCount; i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        //堆栈有fragment则不退出app
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }

        if (!ClickUtils.isOnDoubleClick()) {
            toast(R.string.home_exit_hint);
            return;
        }

        // 移动到上一个任务栈，避免侧滑引起的不良反应
        moveTaskToBack(false);
        postDelayed(() -> {
            // 进行内存优化，销毁掉所有的界面
            ActivityManager.getInstance().finishAllActivities();
            // 销毁进程（注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
            // System.exit(0);
        }, 300);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
//            聊天界面
            case Constants.FRAGMENT_CHAT:
                ClientsResultBean.UserInfoDTO child = (ClientsResultBean.UserInfoDTO) object;
                ChatFragment chatFragment;
                chatFragment = new ChatFragment();

                Bundle bundle = new Bundle();
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.chatType = child.chatType;
                chatInfo.noInput = child.noInput;
                //健康管理群组聊天，云门诊单聊
                if (chatInfo.chatType == 100) {//健康管理
                    chatInfo.setType(V2TIMConversation.V2TIM_GROUP);
                    LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, AppApplication.instance());
                    if (null == loginBean) {
                        return;
                    }
                    //群组聊天用goodsId+医生ID+患者ID拼接groupID
                    chatInfo.setId(child.goodsId + loginBean.getUser().user.userId + child.userId);
                    chatInfo.userId = child.userId + "";
                } else {
                    chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                    chatInfo.setId(child.userId + "");
                }
                chatInfo.setChatName(child.userName);

                bundle.putString(Constants.PLAN_ID, child.planId);
                bundle.putSerializable(Constants.CHAT_INFO, chatInfo);
                chatFragment.setArguments(bundle);
                mapFragments.put(Constants.FRAGMENT_CHAT, chatFragment);
                break;

//                个人信息 套餐配置
            case Constants.FRAGMENT_HEALTH_PLAN:
                HealthPlanFragment healthPlanFragment;
                healthPlanFragment = new HealthPlanFragment();
                mapFragments.put(Constants.FRAGMENT_HEALTH_PLAN, healthPlanFragment);
                break;

//                新增健康管理计划 （套餐配置）
            case Constants.FRAGMENT_HEALTH_NEW:
                NewHealthPlanFragment newHealthPlanFragment;
                newHealthPlanFragment = new NewHealthPlanFragment();
                mapFragments.put(Constants.FRAGMENT_HEALTH_NEW, newHealthPlanFragment);
                break;

//                修改健康管理计划 （套餐配置）
            case Constants.FRAGMENT_HEALTH_MODIFY:
                PlanListBean planListBean = (PlanListBean) object;
                NewHealthPlanFragmentModify newHealthPlanFragmentModify;
                newHealthPlanFragmentModify = new NewHealthPlanFragmentModify();
                Bundle bundleModify = new Bundle();
                bundleModify.putSerializable(Constants.PLAN_LIST_BEAN, planListBean);
                newHealthPlanFragmentModify.setArguments(bundleModify);
                mapFragments.put(Constants.FRAGMENT_HEALTH_MODIFY, newHealthPlanFragmentModify);
                break;
                //健康消息--添加文章
            case Constants.FRAGMENT_ADD_PAPER:
                GetMissionObj getMissionPaper = (GetMissionObj) object;
                AddArticleFragment addArticleFragment;
                addArticleFragment = new AddArticleFragment();
                Bundle bundlePaper = new Bundle();
                bundlePaper.putSerializable(Constants.GET_MISSION_OBJ, getMissionPaper);
                addArticleFragment.setArguments(bundlePaper);
                mapFragments.put(Constants.FRAGMENT_ADD_PAPER, addArticleFragment);
                break;

//                设置 -套餐配置中 修改健康管理计划的 添加问卷
            case Constants.FRAGMENT_ADD_QUESTION:
                GetMissionObj getMissionObj = (GetMissionObj) object;
                AddQuestionFragment addQuestionFragment;
                addQuestionFragment = new AddQuestionFragment();
                Bundle bundleQue = new Bundle();
                bundleQue.putSerializable(Constants.GET_MISSION_OBJ, getMissionObj);
                addQuestionFragment.setArguments(bundleQue);
                mapFragments.put(Constants.FRAGMENT_ADD_QUESTION, addQuestionFragment);
                break;
//                群发消息、健康消息 界面
            case Constants.FRAGMENT_SEND_MSG:
                ChatFragment.NewMsgData msgData = (ChatFragment.NewMsgData) object;
                NewMsgFragment newMsgFragment;
                newMsgFragment = new NewMsgFragment();
                Bundle msgBundle = new Bundle();
                msgBundle.putString(Constants.MSG_TYPE, msgData.msgType);
                msgBundle.putStringArrayList(Constants.MSG_IDS, msgData.userIds);
                newMsgFragment.setArguments(msgBundle);
                mapFragments.put(Constants.FRAGMENT_SEND_MSG, newMsgFragment);
                break;

//                健康消息
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

//                健康评估意见发送界面
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

//                上传患者病例详情界面
            case Constants.FRAGMENT_USER_DATA:
                HealthUploadDataFragment healthUploadDataFragment;
                healthUploadDataFragment = new HealthUploadDataFragment();
                Bundle bundleData = new Bundle();
                CustomHealthDataMessage customHealthDataMessage = (CustomHealthDataMessage) object;
                bundleData.putSerializable(Constants.DATA_MSG, customHealthDataMessage);
                healthUploadDataFragment.setArguments(bundleData);
                mapFragments.put(Constants.FRAGMENT_USER_DATA, healthUploadDataFragment);
                break;


//                健康评估 发送评估意见界面
            case Constants.FRAGMENT_HEALTH_ANALYSE_DISPLAY:
                HealthAnalyseFragmentDisplay healthAnalyseFragmentDisplay;
                healthAnalyseFragmentDisplay = new HealthAnalyseFragmentDisplay();

                Bundle msgBundleAnalyseDis = new Bundle();
                ChatFragment.NewMsgData msgDataAnalyseDis = (ChatFragment.NewMsgData) object;
//                msgBundleAnalyse.putString(Constants.MSG_CUSTOM_ID, msgDataAnalyse.id);//显示详情的时候才要
                msgBundleAnalyseDis.putStringArrayList(Constants.MSG_IDS, msgDataAnalyseDis.userIds);//传入消息接受者的userId
                msgBundleAnalyseDis.putString(Constants.MSG_CUSTOM_ID, msgDataAnalyseDis.id);//显示详情的时候才要
                healthAnalyseFragmentDisplay.setArguments(msgBundleAnalyseDis);
                mapFragments.put(Constants.FRAGMENT_HEALTH_ANALYSE_DISPLAY, healthAnalyseFragmentDisplay);
                break;

//                健康计划详情界面
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
                //健康消息 --添加视频
            case Constants.FRAGMENT_ADD_VIDEO:
                AddVideoObject addVideoObject = (AddVideoObject) object;
                Bundle bundleMsg = new Bundle();
                bundleMsg.putSerializable(Constants.ADD_VIDEO_DATA, addVideoObject);
                AddVideoFragment addVideoFragment;
                addVideoFragment = new AddVideoFragment();
                addVideoFragment.setArguments(bundleMsg);
                mapFragments.put(Constants.FRAGMENT_ADD_VIDEO, addVideoFragment);
                break;

//                健康管理  问卷详情界面
            case Constants.FRAGMENT_QUESTION_DETAIL:
                QuestionResultBean.ListDTO questionBean = (QuestionResultBean.ListDTO) object;
                Bundle bundleQuest = new Bundle();
                bundleQuest.putSerializable(Constants.QUESTION_DETAIL, questionBean);
                QuestionDetailFragment questionDetailFragment;
                questionDetailFragment = new QuestionDetailFragment();
                questionDetailFragment.setArguments(bundleQuest);
                mapFragments.put(Constants.FRAGMENT_QUESTION_DETAIL, questionDetailFragment);
                break;
                //健康消息--文章详情
            case Constants.FRAGMENT_ARTICLE_DETAIL:
                ArticleBean articleBean = (ArticleBean) object;
                Bundle bundleArticle = new Bundle();
                bundleArticle.putSerializable(Constants.ARTICLE_DETAIL, articleBean);
                ArticleDetailFragment articleDetailFragment;
                articleDetailFragment = new ArticleDetailFragment();
                articleDetailFragment.setArguments(bundleArticle);
                mapFragments.put(Constants.FRAGMENT_ARTICLE_DETAIL, articleDetailFragment);
                break;
//                健康提醒界面
            case Constants.FRAGMENT_PLAN_MSG:
                TaskPlanDetailBean taskPlanDetailBean = (TaskPlanDetailBean) object;
                Bundle bundleTP = new Bundle();
                bundleTP.putSerializable(Constants.PLAN_MSG, taskPlanDetailBean);
                PlanMsgFragment planMsgFragment;
                planMsgFragment = new PlanMsgFragment();
                planMsgFragment.setArguments(bundleTP);
                mapFragments.put(Constants.FRAGMENT_PLAN_MSG, planMsgFragment);
                break;

//                健康管理计划预览详情
            case Constants.FRAGMENT_HEALTH_PLAN_PREVIEW:
                PlanDetailResult planDetailResult = (PlanDetailResult) object;
                Bundle bundlePre = new Bundle();
                bundlePre.putSerializable(Constants.PLAN_PREVIEW, planDetailResult);
                HealthPlanPreviewFragment healthPlanPreviewFragment;
                healthPlanPreviewFragment = new HealthPlanPreviewFragment();
                healthPlanPreviewFragment.setArguments(bundlePre);
                mapFragments.put(Constants.FRAGMENT_HEALTH_PLAN_PREVIEW, healthPlanPreviewFragment);
                break;

//               虚拟诊间  书写病例
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

//                云门诊病例
            case Constants.FRAGMENT_HEALTH_HISTORY_PREVIEW:
                ChatFragment.NewMsgData msgPreview = (ChatFragment.NewMsgData) object;
                HealthHistoryPreFragment healthHistoryPreFragment;
                healthHistoryPreFragment = new HealthHistoryPreFragment();
                Bundle bundlePreview = new Bundle();
                bundlePreview.putSerializable(Constants.DATA_MSG, msgPreview);
                healthHistoryPreFragment.setArguments(bundlePreview);
                mapFragments.put(Constants.FRAGMENT_HEALTH_HISTORY_PREVIEW, healthHistoryPreFragment);
                break;

//                视频就诊
            case Constants.FRAGMENT_VIDEO_PATIENT_DATA:
                VideoPatientDataFragment videoPatientDataFragment;
                videoPatientDataFragment = new VideoPatientDataFragment();
                CustomPatientDataMessage customPatientDataMessage = (CustomPatientDataMessage) object;
                Bundle bundlePatient = new Bundle();
                bundlePatient.putSerializable(Constants.DATA_MSG, customPatientDataMessage);
                videoPatientDataFragment.setArguments(bundlePatient);
                mapFragments.put(Constants.FRAGMENT_VIDEO_PATIENT_DATA, videoPatientDataFragment);
                break;
//                个人信息界面 个人信息详情
            case Constants.FRAGMENT_PERSONAL_DATA:
                PersonalDataFragment personalDataFragment = new PersonalDataFragment();
                mapFragments.put(Constants.FRAGMENT_PERSONAL_DATA, personalDataFragment);
                break;

//                个人信息 我的看诊记录
            case Constants.FRAGMENT_CHAT_LOG:
                ChatLogFragment chatLogFragment;
                chatLogFragment = new ChatLogFragment();
                mapFragments.put(Constants.FRAGMENT_CHAT_LOG, chatLogFragment);
                break;

//                用药指导界面
            case Constants.FRAGMENT_MEDICINE_GUIDE:
                MedicineGuideFragment medicineGuideFragment;
                medicineGuideFragment = new MedicineGuideFragment();
                mapFragments.put(Constants.FRAGMENT_MEDICINE_GUIDE, medicineGuideFragment);
                break;
        }
        Set<String> strings = mapFragments.keySet();
        List<String> stringList = new ArrayList<>(strings);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        //遍历所有子Frament集合  如果传过来的key 不是当前的子Fragment 则隐藏
        for (int i = 0; i < mapFragments.size(); i++) {
            if (!stringList.get(i).equals(keyFragment)) {
                fragmentTransaction.hide(mapFragments.get(stringList.get(i)));
            }
        }
//        如果传过来的Fragment key还未添加至集合 则 添加
        if (!mapFragments.get(keyFragment).isAdded()) {
            fragmentTransaction.add(R.id.layout_fragment_end, mapFragments.get(keyFragment));
        }
        //切换并显示 三级Fragment子界面
        supportFragmentManager.popBackStack(keyFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.addToBackStack(keyFragment);
        fragmentTransaction.show(mapFragments.get(keyFragment)).commit();
    }
}
