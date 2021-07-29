package com.bitvalue.healthmanage.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.aop.SingleClick;
import com.bitvalue.healthmanage.app.AppActivity;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.LoginApi;
import com.bitvalue.healthmanage.http.request.TestApi;
import com.bitvalue.healthmanage.http.response.LoginBean;
import com.bitvalue.healthmanage.manager.ActivityManager;
import com.bitvalue.healthmanage.other.DoubleClickHelper;
import com.bitvalue.healthmanage.ui.contacts.bean.ContactBean;
import com.bitvalue.healthmanage.ui.fragment.ChatFragment;
import com.bitvalue.healthmanage.ui.fragment.ContactsFragment;
import com.bitvalue.healthmanage.ui.fragment.NewsFragment;
import com.bitvalue.healthmanage.util.SharedPreManager;
import com.bitvalue.sdk.collab.modules.chat.base.ChatInfo;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.tencent.imsdk.v2.V2TIMConversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

public class HomeActivity extends AppActivity {

    private static final int chat_index = 0;
    private static final int news_index = 1;
    private NewsFragment newsFragment;
    private ContactsFragment contactsFragment;
    private AppFragment[] fragments;
    private int tabPosition = -1;
    private TextView tv_news, tv_chat, tv_person;
    private ImageView img_chat, img_news, img_person;
    private Map<String, Fragment> mapFragments = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        tv_news = findViewById(R.id.tv_news);
        tv_chat = findViewById(R.id.tv_chat);
        tv_person = findViewById(R.id.tv_person);
        img_chat = findViewById(R.id.img_chat);
        img_news = findViewById(R.id.img_news);
        img_person = findViewById(R.id.img_person);

        setOnClickListener(R.id.layout_person, R.id.layout_news);
        initFragments(0);
    }

    private void initFragments(int index) {
        contactsFragment = ContactsFragment.getInstance(false);
        newsFragment = NewsFragment.getInstance(false);
        fragments = new AppFragment[]{contactsFragment, newsFragment};
        getSupportFragmentManager().beginTransaction().add(R.id.layout_fragment, fragments[0]).commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_fragment, fragments[1]).commitAllowingStateLoss();
        afterTabSelect(index);
    }

    @Override
    protected void initData() {
        testInter();
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
        for (int i = 0; i < fragments.length; i++) {
            if (i != index) {
                getSupportFragmentManager().beginTransaction().hide(fragments[i]).commit();
            }
        }
        if (!fragments[index].isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.layout_fragment, fragments[index]);
        }
        getSupportFragmentManager().beginTransaction().show(fragments[index]).commit();

        tabPosition = index;
        setBottomUi(index);
    }

    private void setBottomUi(int index) {
        initNaviUI();

        switch (index) {
            case chat_index:
                img_person.setImageResource(R.drawable.tab_icon_ct);
                tv_person.setTextColor(getResources().getColor(R.color.white));
                break;

            case news_index:
                img_news.setImageResource(R.drawable.tab_icon_set);
                tv_news.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void initNaviUI() {
        img_person.setImageResource(R.drawable.tab_icon_ct);
        tv_person.setTextColor(getResources().getColor(R.color.gray));

        img_news.setImageResource(R.drawable.tab_icon_set);
        tv_news.setTextColor(getResources().getColor(R.color.gray));
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_person:
                afterTabSelect(0);
                break;
            case R.id.layout_news:
                afterTabSelect(1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
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
     * @param child
     */
    public void switchSecondFragment(String keyFragment, ContactBean child) {
        boolean isContain = mapFragments.containsKey(keyFragment);
        switch (keyFragment) {
            case Constants.FRAGMENT_CHAT:
                ChatFragment chatFragment;
//                if (!isContain) {
//                    chatFragment = new ChatFragment();
//                } else {
//                    chatFragment = (ChatFragment) mapFragments.get(keyFragment);
//                }
                if (isContain) {
                    mapFragments.remove(keyFragment);
                }
                chatFragment = new ChatFragment();

                Bundle bundle = new Bundle();//TODO 参数可改
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                chatInfo.setId(child.getUserId());
                String chatName = child.getName();
                chatInfo.setChatName(chatName);

                bundle.putSerializable(Constants.CHAT_INFO, chatInfo);
                chatFragment.setArguments(bundle);
                mapFragments.put(Constants.FRAGMENT_CHAT, chatFragment);
                break;
        }
        Set<String> strings = mapFragments.keySet();
        List<String> strings1 = new ArrayList<>(strings);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mapFragments.size(); i++) {
            if (!strings1.get(i).equals(keyFragment)) {
                transaction.hide(fragments[i]).commit();
            }
        }
        if (!mapFragments.get(keyFragment).isAdded()) {
            transaction.add(R.id.layout_fragment_end, mapFragments.get(keyFragment));
        }
        transaction.addToBackStack(Constants.FRAGMENT_CHAT);
        transaction.show(mapFragments.get(keyFragment)).commit();

//        tabPosition = index;
    }

    public void switchSecondFragmentOther() {

    }

//    public void forward(int viewId, Fragment fragment, String name, boolean hide) {
//        if (getSupportFragmentManager() == null){
//            return;
//        }
//        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//        if (hide) {
//            trans.hide(this);
//            trans.add(viewId, fragment);
//        } else {
//            trans.replace(viewId, fragment);
//        }
//
//        trans.addToBackStack(name);
//        trans.commitAllowingStateLoss();
//    }
//
//    public void backward() {
//        if (getSupportFragmentManager() == null){
//            return;
//        }
//        getSupportFragmentManager().popBackStack();
//    }
}
