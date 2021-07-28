package com.bitvalue.healthmanage.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.aop.SingleClick;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.ClientsApi;
import com.bitvalue.healthmanage.http.request.TestApi;
import com.bitvalue.healthmanage.http.response.ClientsResultBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.contacts.bean.ContactBean;
import com.bitvalue.healthmanage.ui.contacts.bean.ContactsGroupBean;
import com.bitvalue.healthmanage.ui.contacts.view.RecyclerAdapter;
import com.bitvalue.healthmanage.util.UiUtil;
import com.bitvalue.healthmanage.widget.popupwindow.CommonPopupWindow;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

//import butterknife.OnClick;
import okhttp3.Call;

public class ContactsFragment extends AppFragment implements CommonPopupWindow.ViewInterface {
    private boolean is_need_toast;
    private RecyclerView contact_list;
    private List<ContactsGroupBean> contactsGroupBeans = new ArrayList();
    private RecyclerAdapter adapter;
    private HomeActivity homeActivity;
    private CommonPopupWindow popupWindow;
    private LinearLayout layout_nav;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    public static ContactsFragment getInstance(boolean is_need_toast) {
        ContactsFragment contactsFragment = new ContactsFragment();
        Bundle bundle = new Bundle();//TODO 参数可改
        bundle.putBoolean("is_need_toast", is_need_toast);
        contactsFragment.setArguments(bundle);
        return contactsFragment;
    }

    @Override
    protected void initView() {
        homeActivity = (HomeActivity) getActivity();
        setOnClickListener(R.id.layout_nav);

        getDatas();
        is_need_toast = getArguments().getBoolean("is_need_toast");
        contact_list = getView().findViewById(R.id.contact_list);
        layout_nav = getView().findViewById(R.id.layout_nav);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contact_list.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(getActivity(), contactsGroupBeans);
        adapter.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(int flatPos) {
                return false;
            }
        });
        adapter.setOnGroupExpandCollapseListener(new GroupExpandCollapseListener() {
            @Override
            public void onGroupExpanded(ExpandableGroup group) {

            }

            @Override
            public void onGroupCollapsed(ExpandableGroup group) {

            }
        });
        contact_list.setAdapter(adapter);

//        geMyClients();
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_nav:
                showPartPop(R.layout.pop_contacts);
                break;
        }
    }

    private void geMyClients() {
        EasyHttp.get(this).api(new ClientsApi()).request(new HttpCallback<HttpData<ClientsResultBean>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<ClientsResultBean> result) {
                super.onSucceed(result);
                if (true) {

                }
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    private void getDatas() {
        List<ContactBean> childList = new ArrayList<>();
        childList.add(new ContactBean("张三"));
        childList.add(new ContactBean("李四"));
        childList.add(new ContactBean("王二"));
        ContactsGroupBean contactsGroupBean = new ContactsGroupBean("朋友", childList);
        contactsGroupBeans.add(contactsGroupBean);

        List<ContactBean> childList2 = new ArrayList<>();
        childList2.add(new ContactBean("小宝"));
        childList2.add(new ContactBean("爷爷"));
        childList2.add(new ContactBean("奶奶"));
        ContactsGroupBean contactsGroupBean2 = new ContactsGroupBean("亲人", childList2);
        contactsGroupBeans.add(contactsGroupBean2);
    }

    //冒泡弹出
    private void showPartPop(int layoutResId) {
//        int top = Constants.screenHeight * 2 / 3 - UiUtil.getStatusBarHeight(LessonVideoPlayActivity.this);//屏幕高度减播放器高度
        //测量View的宽高

        popupWindow = new CommonPopupWindow.Builder(getActivity())
                .setView(layoutResId)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                .setAnimationStyle(R.style.AnimUp)
                .setAnimationStyle(R.style.AnimDown)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .create();
//        popupWindow.showAsDropDown(layout_nav, -200, 0);
        UiUtil.measureWidthAndHeight(popupWindow.getContentView());
        popupWindow.showAsDropDown(layout_nav, -popupWindow.getContentView().getWidth(), 0);


//        if (popupWindow != null && popupWindow.isShowing()) return;
//        View upView = LayoutInflater.from(getActivity()).inflate(layoutResId, null);
//        //测量View的宽高
//        UiUtil.measureWidthAndHeight(upView);
//        popupWindow = new CommonPopupWindow.Builder(getActivity())
//                .setView(layoutResId)
//                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
//                .setBackGroundLevel(1f)//取值范围0.0f-1.0f 值越小越暗
//                .setAnimationStyle(R.style.AnimUp)
//                .setViewOnclickListener(this)
//                .setOutsideTouchable(true)
//                .create();
//        popupWindow.showAsDropDown(layout_nav,layout_nav.getWidth(),);
//        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.pop_contacts:
                view.findViewById(R.id.tv_project).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.show("点击了计划");
                    }
                });

                view.findViewById(R.id.tv_mul_msg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.show("点击了群发");
                    }
                });
                break;
        }

    }
}
