package com.bitvalue.health.ui.activity;


import static com.bitvalue.health.util.Constants.EVENT_MES_TYPE_CLOUDCLINC;

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

import com.bitvalue.health.Application;
import com.bitvalue.health.api.eventbusbean.MainRefreshObj;
import com.bitvalue.health.api.eventbusbean.MsgRemindObj;
import com.bitvalue.health.api.eventbusbean.VideoRefreshObj;
import com.bitvalue.health.api.requestbean.QuestionResultBean;
import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.PlanDetailResult;
import com.bitvalue.health.base.BaseActivity;
import com.bitvalue.health.contract.homecontract.HomeContract;
import com.bitvalue.health.presenter.homepersenter.HomePersenter;
import com.bitvalue.health.ui.fragment.function.chat.ChatFragment;
import com.bitvalue.health.ui.fragment.function.healthmanage.ContactsFragment;
import com.bitvalue.health.ui.fragment.function.cloudclinic.HealthHistoryPreFragment;
import com.bitvalue.health.ui.fragment.function.cloudclinic.WriteHealthFragment;
import com.bitvalue.health.ui.fragment.function.docfriend.DocFriendsFragment;
import com.bitvalue.health.ui.fragment.function.healthmanage.HealthAnalyseDisplayFragment;
import com.bitvalue.health.ui.fragment.function.healthmanage.HealthAnalyseFragment;
import com.bitvalue.health.ui.fragment.function.healthmanage.HealthPlanDetailFragment;
import com.bitvalue.health.ui.fragment.function.healthmanage.HealthPlanPreviewFragment;
import com.bitvalue.health.ui.fragment.function.healthmanage.HealthUploadDataFragment;
import com.bitvalue.health.ui.fragment.function.healthmanage.QuestionDetailFragment;
import com.bitvalue.health.ui.fragment.function.setting.PersonalDataFragment;
import com.bitvalue.health.ui.fragment.function.setting.SettingsFragment;
import com.bitvalue.health.ui.fragment.function.cloudclinic.CloudClinicFragment;
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
    private static final int chat_index = 0;
    private static final int settings = 1;
    private static final int video_index = 2;
    private static final int doc_index = 3;
    private int tabPosition = -1;
    private Fragment[] fragmentArrays;
    private Map<String, Fragment> mapFragments = new HashMap<>();
    private SettingsFragment settingsFragment;
    private ContactsFragment contactsFragment;
    private CloudClinicFragment cloudClinicFragment;
    private DocFriendsFragment docFriendsFragment;


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
        initFragments(chat_index);
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
        contactsFragment = ContactsFragment.getInstance(false);
        settingsFragment = SettingsFragment.getInstance(false);
        cloudClinicFragment = CloudClinicFragment.getInstance(false);
        docFriendsFragment = DocFriendsFragment.getInstance(false);
        fragmentArrays = new Fragment[]{contactsFragment, settingsFragment, cloudClinicFragment, docFriendsFragment};
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_fragment, fragmentArrays[0]);
        transaction.add(R.id.layout_fragment, fragmentArrays[1]);
        transaction.add(R.id.layout_fragment, fragmentArrays[2]);
        transaction.add(R.id.layout_fragment, fragmentArrays[3]);
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
            supportFragmentManager.beginTransaction().add(R.id.layout_fragment, fragmentArrays[index]);
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
//            健康计划
            case chat_index:
                img_person.setImageResource(R.drawable.tab_icon_ct);
                tv_person.setTextColor(getResources().getColor(R.color.white));
                layout_person.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;

//                云门诊
            case video_index:
                img_group.setImageResource(R.drawable.tab_icon_gr);
                tv_group.setTextColor(getResources().getColor(R.color.white));
                layout_group.setBackgroundColor(getResources().getColor(R.color.home_blue_dark));
                break;

//                医生好友
            case doc_index:
                img_send.setImageResource(R.drawable.tab_icon_of);
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
                    LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, Application.instance());
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
//            case Constants.FRAGMENT_HEALTH_PLAN:
//                HealthPlanFragment healthPlanFragment;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                healthPlanFragment = new HealthPlanFragment();
//                mapFragments.put(Constants.FRAGMENT_HEALTH_PLAN, healthPlanFragment);
//                break;
//
//            case Constants.FRAGMENT_HEALTH_NEW:
//                NewHealthPlanFragment newHealthPlanFragment;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                newHealthPlanFragment = new NewHealthPlanFragment();
//                mapFragments.put(Constants.FRAGMENT_HEALTH_NEW, newHealthPlanFragment);
//                break;
//            case Constants.FRAGMENT_HEALTH_MODIFY:
//                PlanListBean planListBean = (PlanListBean) object;
//                NewHealthPlanFragmentModify newHealthPlanFragmentModify;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                newHealthPlanFragmentModify = new NewHealthPlanFragmentModify();
//                Bundle bundleModify = new Bundle();
//                bundleModify.putSerializable(Constants.PLAN_LIST_BEAN, planListBean);
//                newHealthPlanFragmentModify.setArguments(bundleModify);
//                mapFragments.put(Constants.FRAGMENT_HEALTH_MODIFY, newHealthPlanFragmentModify);
//                break;
//
//            case Constants.FRAGMENT_ADD_PAPER:
//                GetMissionObj getMissionPaper = (GetMissionObj) object;
//                AddArticleFragment addArticleFragment;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                addArticleFragment = new AddArticleFragment();
//                Bundle bundlePaper = new Bundle();
//                bundlePaper.putSerializable(Constants.GET_MISSION_OBJ, getMissionPaper);
//                addArticleFragment.setArguments(bundlePaper);
//                mapFragments.put(Constants.FRAGMENT_ADD_PAPER, addArticleFragment);
//                break;
//            case Constants.FRAGMENT_ADD_QUESTION:
//                GetMissionObj getMissionObj = (GetMissionObj) object;
//                AddQuestionFragment addQuestionFragment;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                addQuestionFragment = new AddQuestionFragment();
//                Bundle bundleQue = new Bundle();
//                bundleQue.putSerializable(Constants.GET_MISSION_OBJ, getMissionObj);
//                addQuestionFragment.setArguments(bundleQue);
//                mapFragments.put(Constants.FRAGMENT_ADD_QUESTION, addQuestionFragment);
//                break;
//            case Constants.FRAGMENT_SEND_MSG:
//                ChatFragment.NewMsgData msgData = (ChatFragment.NewMsgData) object;
//                NewMsgFragment newMsgFragment;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                newMsgFragment = new NewMsgFragment();
//
//                Bundle msgBundle = new Bundle();
//                msgBundle.putString(Constants.MSG_TYPE, msgData.msgType);
//                msgBundle.putStringArrayList(Constants.MSG_IDS, msgData.userIds);
//                newMsgFragment.setArguments(msgBundle);
//                mapFragments.put(Constants.FRAGMENT_SEND_MSG, newMsgFragment);
//                break;
//            case Constants.FRAGMENT_SEND_MSG_DISPLAY:
//                ChatFragment.NewMsgData msgDataDis = (ChatFragment.NewMsgData) object;
//                NewMsgFragmentDisplay newMsgFragmentDisplay;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                newMsgFragmentDisplay = new NewMsgFragmentDisplay();
//
//                Bundle msgBundleDis = new Bundle();
//                msgBundleDis.putString(Constants.MSG_TYPE, msgDataDis.msgType);
//                msgBundleDis.putString(Constants.MSG_CUSTOM_ID, msgDataDis.id);//显示详情的时候才要
//                msgBundleDis.putStringArrayList(Constants.MSG_IDS, msgDataDis.userIds);
//                newMsgFragmentDisplay.setArguments(msgBundleDis);
//                mapFragments.put(Constants.FRAGMENT_SEND_MSG_DISPLAY, newMsgFragmentDisplay);
//                break;
//
            /***
             * 健康评估
             */
            case Constants.FRAGMENT_HEALTH_ANALYSE:
                HealthAnalyseFragment healthAnalyseFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
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
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
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
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                healthAnalyseFragmentDisplay = new HealthAnalyseDisplayFragment();

                Bundle msgBundleAnalyseDis = new Bundle();
                ChatFragment.NewMsgData msgDataAnalyseDis = (ChatFragment.NewMsgData) object;
//                msgBundleAnalyse.putString(Constants.MSG_CUSTOM_ID, msgDataAnalyse.id);//显示详情的时候才要
                msgBundleAnalyseDis.putStringArrayList(Constants.MSG_IDS, msgDataAnalyseDis.userIds);//传入消息接受者的userId
                msgBundleAnalyseDis.putString(Constants.MSG_CUSTOM_ID, msgDataAnalyseDis.id);//显示详情的时候才要
                healthAnalyseFragmentDisplay.setArguments(msgBundleAnalyseDis);
                mapFragments.put(Constants.FRAGMENT_HEALTH_ANALYSE_DISPLAY, healthAnalyseFragmentDisplay);
                break;
//
            case Constants.FRAGMENT_HEALTH_PLAN_DETAIL:
                ChatFragment.NewMsgData msgDataDetail = (ChatFragment.NewMsgData) object;
                HealthPlanDetailFragment healthPlanDetailFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                healthPlanDetailFragment = new HealthPlanDetailFragment();
                Bundle bundleDetail = new Bundle();
                bundleDetail.putStringArrayList(Constants.MSG_IDS, msgDataDetail.userIds);
                bundleDetail.putString(Constants.PLAN_ID, msgDataDetail.id);
                healthPlanDetailFragment.setArguments(bundleDetail);
                mapFragments.put(Constants.FRAGMENT_HEALTH_PLAN_DETAIL, healthPlanDetailFragment);
                break;
//            case Constants.FRAGMENT_ADD_VIDEO:
//                AddVideoObject addVideoObject = (AddVideoObject) object;
//                Bundle bundleMsg = new Bundle();
//                bundleMsg.putSerializable(Constants.ADD_VIDEO_DATA, addVideoObject);
//                AddVideoFragment addVideoFragment;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                addVideoFragment = new AddVideoFragment();
//                addVideoFragment.setArguments(bundleMsg);
//                mapFragments.put(Constants.FRAGMENT_ADD_VIDEO, addVideoFragment);
//                break;
//

            /**
             * 问卷调查界面
             */
            case Constants.FRAGMENT_QUESTION_DETAIL:
                QuestionResultBean.ListDTO questionBean = (QuestionResultBean.ListDTO) object;
                Bundle bundleQuest = new Bundle();
                bundleQuest.putSerializable(Constants.QUESTION_DETAIL, questionBean);
                QuestionDetailFragment questionDetailFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                questionDetailFragment = new QuestionDetailFragment();
                questionDetailFragment.setArguments(bundleQuest);
                mapFragments.put(Constants.FRAGMENT_QUESTION_DETAIL, questionDetailFragment);
                break;
//
//            case Constants.FRAGMENT_ARTICLE_DETAIL:
//                ArticleBean articleBean = (ArticleBean) object;
//                Bundle bundleArticle = new Bundle();
//                bundleArticle.putSerializable(Constants.ARTICLE_DETAIL, articleBean);
//                ArticleDetailFragment articleDetailFragment;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                articleDetailFragment = new ArticleDetailFragment();
//                articleDetailFragment.setArguments(bundleArticle);
//                mapFragments.put(Constants.FRAGMENT_ARTICLE_DETAIL, articleDetailFragment);
//                break;
//            case Constants.FRAGMENT_PLAN_MSG:
//                TaskPlanDetailBean taskPlanDetailBean = (TaskPlanDetailBean) object;
//                Bundle bundleTP = new Bundle();
//                bundleTP.putSerializable(Constants.PLAN_MSG, taskPlanDetailBean);
//                PlanMsgFragment planMsgFragment;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                planMsgFragment = new PlanMsgFragment();
//                planMsgFragment.setArguments(bundleTP);
//                mapFragments.put(Constants.FRAGMENT_PLAN_MSG, planMsgFragment);
//                break;
//
            case Constants.FRAGMENT_HEALTH_PLAN_PREVIEW:
                PlanDetailResult planDetailResult = (PlanDetailResult) object;
                Bundle bundlePre = new Bundle();
                bundlePre.putSerializable(Constants.PLAN_PREVIEW, planDetailResult);
                HealthPlanPreviewFragment healthPlanPreviewFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
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
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
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
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                healthHistoryPreFragment = new HealthHistoryPreFragment();
                Bundle bundlePreview = new Bundle();
                bundlePreview.putSerializable(Constants.DATA_MSG, msgPreview);
                healthHistoryPreFragment.setArguments(bundlePreview);
                mapFragments.put(Constants.FRAGMENT_HEALTH_HISTORY_PREVIEW, healthHistoryPreFragment);
                break;
//
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
            case Constants.FRAGMENT_PERSONAL_DATA:
                PersonalDataFragment personalDataFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                personalDataFragment = new PersonalDataFragment();
                mapFragments.put(Constants.FRAGMENT_PERSONAL_DATA, personalDataFragment);
                break;
//            case Constants.FRAGMENT_CHAT_LOG:
//                ChatLogFragment chatLogFragment;
//                if (isContain) {
//                    mapFragments.remove(keyFragment);
//                }
//                chatLogFragment = new ChatLogFragment();
//                mapFragments.put(Constants.FRAGMENT_CHAT_LOG, chatLogFragment);
//                break;
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


    @OnClick({R.id.layout_person, R.id.layout_settings, R.id.layout_group, R.id.layout_send})
    public void clickleftbutton(View view) {
        switch (view.getId()) {
            case R.id.layout_person:
                if (tabPosition != 0) {
                    backAllThirdAct();
                }
                EventBus.getDefault().post(new MainRefreshObj()); // 通知健康管理界面获取数据 请求接口
                afterTabSelect(0);
                break;
            case R.id.layout_settings:
                if (tabPosition != 1) {
                    backAllThirdAct();
                }
                afterTabSelect(1);
                break;

            case R.id.layout_group:
                EventBus.getDefault().post(new VideoRefreshObj());
                if (tabPosition != 2) {
                    backAllThirdAct();
                }
                afterTabSelect(2);

                //点击之后隐藏红点
                layout_pot_video.setVisibility(View.GONE);
                tv_new_count_video.setText("0");
                break;

            case R.id.layout_send:
//                EventBus.getDefault().post(new VideoRefreshObj());
                if (tabPosition != 3) {
                    backAllThirdAct();
                }
                afterTabSelect(3);
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

    @Override
    public void LoginSuccess(Object o) {
        Log.e(TAG, "LoginSuccess ");
    }

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
}
