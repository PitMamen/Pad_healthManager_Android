package com.bitvalue.healthmanage.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppActivity;
import com.bitvalue.healthmanage.app.AppApplication;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.TestApi;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.ClientsResultBean;
import com.bitvalue.healthmanage.http.response.LoginBean;
import com.bitvalue.healthmanage.http.response.PlanListBean;
import com.bitvalue.healthmanage.http.response.QuestionResultBean;
import com.bitvalue.healthmanage.http.response.msg.AddVideoObject;
import com.bitvalue.healthmanage.manager.ActivityManager;
import com.bitvalue.healthmanage.other.DoubleClickHelper;
import com.bitvalue.healthmanage.ui.fragment.AddArticleFragment;
import com.bitvalue.healthmanage.ui.fragment.AddQuestionFragment;
import com.bitvalue.healthmanage.ui.fragment.AddVideoFragment;
import com.bitvalue.healthmanage.ui.fragment.ArticleDetailFragment;
import com.bitvalue.healthmanage.ui.fragment.ChatFragment;
import com.bitvalue.healthmanage.ui.fragment.ContactsFragment;
import com.bitvalue.healthmanage.ui.fragment.HealthAnalyseFragment;
import com.bitvalue.healthmanage.ui.fragment.HealthAnalyseFragmentDisplay;
import com.bitvalue.healthmanage.ui.fragment.HealthPlanDetailFragment;
import com.bitvalue.healthmanage.ui.fragment.HealthPlanFragment;
import com.bitvalue.healthmanage.ui.fragment.HealthUploadDataFragment;
import com.bitvalue.healthmanage.ui.fragment.NewHealthPlanFragment;
import com.bitvalue.healthmanage.ui.fragment.NewHealthPlanFragmentModify;
import com.bitvalue.healthmanage.ui.fragment.NewMsgFragment;
import com.bitvalue.healthmanage.ui.fragment.NewMsgFragmentDisplay;
import com.bitvalue.healthmanage.ui.fragment.QuestionDetailFragment;
import com.bitvalue.healthmanage.ui.settings.fragment.SettingsFragment;
import com.bitvalue.healthmanage.util.DemoLog;
import com.bitvalue.healthmanage.util.SharedPreManager;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.modules.chat.base.ChatInfo;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.tencent.imsdk.v2.V2TIMConversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.OnClick;
import okhttp3.Call;

public class HomeActivity extends AppActivity {

    private static final int chat_index = 0;
    private static final int settings = 1;
    private SettingsFragment settingsFragment;
    private ContactsFragment contactsFragment;
    private AppFragment[] fragments;
    private int tabPosition = -1;
    private TextView tv_settings, tv_chat, tv_person;
    private LinearLayout layout_person, layout_settings;
    private ImageView img_chat, img_settings, img_person;
    private Map<String, Fragment> mapFragments = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        //adb logcat -v time >D:\log.txt
        tv_settings = findViewById(R.id.tv_settings);
        tv_chat = findViewById(R.id.tv_chat);
        tv_person = findViewById(R.id.tv_person);
        img_chat = findViewById(R.id.img_chat);
        img_settings = findViewById(R.id.img_settings);
        img_person = findViewById(R.id.img_person);
        layout_person = findViewById(R.id.layout_person);
        layout_settings = findViewById(R.id.layout_settings);

//        setOnClickListener(R.id.layout_person, R.id.layout_settings);
        initFragments(0);
        loginIM();
    }

    private void loginIM() {
        LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, this);
        TUIKit.login(loginBean.getAccount().user.userId + "", loginBean.getAccount().user.userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        DemoLog.e("TUIKit.login", "登录聊天失败" + ", errCode = " + code + ", errInfo = " + desc);
                    }
                });
            }

            @Override
            public void onSuccess(Object data) {
                // 跳转到首页
                // HomeActivity.start(getContext(), MeFragment.class);
                SharedPreManager.putBoolean(Constants.KEY_IM_AUTO_LOGIN, true, AppApplication.instance());
            }
        });
    }

    private void initFragments(int index) {
        contactsFragment = ContactsFragment.getInstance(false);
//        newsFragment = NewsFragment.getInstance(false);
        settingsFragment = SettingsFragment.getInstance(false);
        fragments = new AppFragment[]{contactsFragment, settingsFragment};
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_fragment, fragments[0]);
        transaction.add(R.id.layout_fragment, fragments[1]);
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
//        testInter();
    }

    private void testInter() {
        EasyHttp.get(this).api(new TestApi()).request(new HttpCallback<HttpData<String>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<String> result) {
                super.onSucceed(result);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });

    }

    /**
     * 底部导航栏点击后的跳转
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
//        if (index == 0) {
//            //TODO 造的数据
//            ContactBean contactBean = new ContactBean("爷爷", "111");
//            switchSecondFragment(Constants.FRAGMENT_CHAT, contactBean);
//        } else {
//            switchSecondFragment(Constants.FRAGMENT_HEALTH, "");
//        }
    }

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
        }
    }

    private void initNaviUI() {
        img_person.setImageResource(R.drawable.tab_icon_ct);
        tv_person.setTextColor(getResources().getColor(R.color.gray));
        layout_person.setBackgroundColor(getResources().getColor(R.color.home_blue));

        img_settings.setImageResource(R.drawable.tab_icon_set);
        tv_settings.setTextColor(getResources().getColor(R.color.gray));
        layout_settings.setBackgroundColor(getResources().getColor(R.color.home_blue));
    }

//    @SingleClick
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.layout_person:
//                afterTabSelect(0);
//                break;
//            case R.id.layout_settings:
//                afterTabSelect(1);
//                break;
//        }
//    }

    @OnClick({R.id.layout_person, R.id.layout_settings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_person:
//                backAll();
                afterTabSelect(0);
                break;
            case R.id.layout_settings:
//                backAll();
                afterTabSelect(1);
                break;
        }
    }

    private void backAll() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            backAll();
        }
    }

    @Override
    public void onBackPressed() {
        //堆栈有fragment则不退出app
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }

        if (!DoubleClickHelper.isOnDoubleClick()) {
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
        switch (keyFragment) {
            case Constants.FRAGMENT_CHAT:
                ClientsResultBean.UserInfoDTO child = (ClientsResultBean.UserInfoDTO) object;
                ChatFragment chatFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                chatFragment = new ChatFragment();

                Bundle bundle = new Bundle();
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                chatInfo.setId(child.userId + "");
//                chatInfo.setId("3");
                chatInfo.setChatName(child.userName);

                bundle.putInt(Constants.PLAN_ID,child.planId);
                bundle.putSerializable(com.bitvalue.healthmanage.util.Constants.CHAT_INFO, chatInfo);
                chatFragment.setArguments(bundle);
                mapFragments.put(Constants.FRAGMENT_CHAT, chatFragment);
                break;
            case Constants.FRAGMENT_HEALTH_PLAN:
                HealthPlanFragment healthPlanFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                healthPlanFragment = new HealthPlanFragment();
                mapFragments.put(Constants.FRAGMENT_HEALTH_PLAN, healthPlanFragment);
                break;

            case Constants.FRAGMENT_HEALTH_NEW:
                NewHealthPlanFragment newHealthPlanFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                newHealthPlanFragment = new NewHealthPlanFragment();
                mapFragments.put(Constants.FRAGMENT_HEALTH_NEW, newHealthPlanFragment);
                break;
            case Constants.FRAGMENT_HEALTH_MODIFY:
                PlanListBean planListBean = (PlanListBean) object;
                NewHealthPlanFragmentModify newHealthPlanFragmentModify;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                newHealthPlanFragmentModify = new NewHealthPlanFragmentModify();
                Bundle bundleModify = new Bundle();
                bundleModify.putSerializable(Constants.PLAN_LIST_BEAN, planListBean);
                newHealthPlanFragmentModify.setArguments(bundleModify);
                mapFragments.put(Constants.FRAGMENT_HEALTH_MODIFY, newHealthPlanFragmentModify);
                break;

            case Constants.FRAGMENT_ADD_PAPER:
                AddArticleFragment addArticleFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                addArticleFragment = new AddArticleFragment();
                mapFragments.put(Constants.FRAGMENT_ADD_PAPER, addArticleFragment);
                break;
            case Constants.FRAGMENT_ADD_QUESTION:
                AddQuestionFragment addQuestionFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                addQuestionFragment = new AddQuestionFragment();
                mapFragments.put(Constants.FRAGMENT_ADD_QUESTION, addQuestionFragment);
                break;
            case Constants.FRAGMENT_SEND_MSG:
                ChatFragment.NewMsgData msgData = (ChatFragment.NewMsgData) object;
                NewMsgFragment newMsgFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                newMsgFragment = new NewMsgFragment();

                Bundle msgBundle = new Bundle();
                msgBundle.putString(Constants.MSG_TYPE, msgData.msgType);
                msgBundle.putStringArrayList(Constants.MSG_IDS, msgData.userIds);
                newMsgFragment.setArguments(msgBundle);
                mapFragments.put(Constants.FRAGMENT_SEND_MSG, newMsgFragment);
                break;
            case Constants.FRAGMENT_SEND_MSG_DISPLAY:
                ChatFragment.NewMsgData msgDataDis = (ChatFragment.NewMsgData) object;
                NewMsgFragmentDisplay newMsgFragmentDisplay;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                newMsgFragmentDisplay = new NewMsgFragmentDisplay();

                Bundle msgBundleDis = new Bundle();
                msgBundleDis.putString(Constants.MSG_TYPE, msgDataDis.msgType);
                msgBundleDis.putString(Constants.MSG_CUSTOM_ID, msgDataDis.id);//显示详情的时候才要
                msgBundleDis.putStringArrayList(Constants.MSG_IDS, msgDataDis.userIds);
                newMsgFragmentDisplay.setArguments(msgBundleDis);
                mapFragments.put(Constants.FRAGMENT_SEND_MSG_DISPLAY, newMsgFragmentDisplay);
                break;

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
                healthAnalyseFragment.setArguments(msgBundleAnalyse);
                mapFragments.put(Constants.FRAGMENT_HEALTH_ANALYSE, healthAnalyseFragment);
                break;

            case Constants.FRAGMENT_USER_DATA:
                HealthUploadDataFragment healthUploadDataFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                healthUploadDataFragment = new HealthUploadDataFragment();

                Bundle bundleData = new Bundle();
                CustomHealthDataMessage customHealthDataMessage = (CustomHealthDataMessage) object;
                bundleData.putSerializable(Constants.DATA_MSG,customHealthDataMessage);
                healthUploadDataFragment.setArguments(bundleData);
                mapFragments.put(Constants.FRAGMENT_USER_DATA, healthUploadDataFragment);
                break;

            case Constants.FRAGMENT_HEALTH_ANALYSE_DISPLAY:
                HealthAnalyseFragmentDisplay healthAnalyseFragmentDisplay;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                healthAnalyseFragmentDisplay = new HealthAnalyseFragmentDisplay();

                Bundle msgBundleAnalyseDis = new Bundle();
                ChatFragment.NewMsgData msgDataAnalyseDis = (ChatFragment.NewMsgData) object;
//                msgBundleAnalyse.putString(Constants.MSG_CUSTOM_ID, msgDataAnalyse.id);//显示详情的时候才要
                msgBundleAnalyseDis.putStringArrayList(Constants.MSG_IDS, msgDataAnalyseDis.userIds);//传入消息接受者的userId
                msgBundleAnalyseDis.putString(Constants.MSG_CUSTOM_ID, msgDataAnalyseDis.id);//显示详情的时候才要
                healthAnalyseFragmentDisplay.setArguments(msgBundleAnalyseDis);
                mapFragments.put(Constants.FRAGMENT_HEALTH_ANALYSE_DISPLAY, healthAnalyseFragmentDisplay);
                break;

            case Constants.FRAGMENT_HEALTH_PLAN_DETAIL:
                ChatFragment.NewMsgData msgDataDetail = (ChatFragment.NewMsgData) object;
                HealthPlanDetailFragment healthPlanDetailFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                healthPlanDetailFragment = new HealthPlanDetailFragment();
                Bundle bundleDetail = new Bundle();
                bundleDetail.putStringArrayList(Constants.MSG_IDS,msgDataDetail.userIds);
                bundleDetail.putString(Constants.PLAN_ID,msgDataDetail.id);
                healthPlanDetailFragment.setArguments(bundleDetail);
                mapFragments.put(Constants.FRAGMENT_HEALTH_PLAN_DETAIL, healthPlanDetailFragment);
                break;
            case Constants.FRAGMENT_ADD_VIDEO:
                AddVideoObject addVideoObject = (AddVideoObject) object;
                Bundle bundleMsg = new Bundle();
                bundleMsg.putSerializable(Constants.ADD_VIDEO_DATA, addVideoObject);
                AddVideoFragment addVideoFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                addVideoFragment = new AddVideoFragment();
                addVideoFragment.setArguments(bundleMsg);
                mapFragments.put(Constants.FRAGMENT_ADD_VIDEO, addVideoFragment);
                break;

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

            case Constants.FRAGMENT_ARTICLE_DETAIL:
                ArticleBean articleBean = (ArticleBean) object;
                Bundle bundleArticle = new Bundle();
                bundleArticle.putSerializable(Constants.ARTICLE_DETAIL, articleBean);
                ArticleDetailFragment articleDetailFragment;
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                articleDetailFragment = new ArticleDetailFragment();
                articleDetailFragment.setArguments(bundleArticle);
                mapFragments.put(Constants.FRAGMENT_ARTICLE_DETAIL, articleDetailFragment);
                break;
        }
        Set<String> strings = mapFragments.keySet();
        List<String> stringList = new ArrayList<>(strings);


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
        supportFragmentManager.popBackStack(keyFragment,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.addToBackStack(keyFragment);
        fragmentTransaction.show(mapFragments.get(keyFragment)).commit();
    }

    public void switchSecondFragmentOther() {

    }
}
