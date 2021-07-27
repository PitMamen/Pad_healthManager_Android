package com.bitvalue.healthmanage.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.bitvalue.healthmanage.ui.fragment.ContactsFragment;
import com.bitvalue.healthmanage.ui.fragment.NewsFragment;
import com.bitvalue.healthmanage.util.SharedPreManager;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;

import okhttp3.Call;

public class HomeActivity extends AppActivity {

    private static final int chat_index = 0;
    private static final int news_index = 1;
    private NewsFragment newsFragment;
    private ContactsFragment contactsFragment;
    private AppFragment[] fragments;
    private int tabPosition = -1;
    private TextView tv_news, tv_chat,tv_person;
    private ImageView img_chat, img_news,img_person;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }

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
                testInter();
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
}
