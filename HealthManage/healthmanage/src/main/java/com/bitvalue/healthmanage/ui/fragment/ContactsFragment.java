package com.bitvalue.healthmanage.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.aop.SingleClick;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.ClientsApi;
import com.bitvalue.healthmanage.http.response.ClientsResultBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.activity.LoginHealthActivity;
import com.bitvalue.healthmanage.ui.contacts.bean.ContactBean;
import com.bitvalue.healthmanage.ui.contacts.bean.ContactsGroupBean;
import com.bitvalue.healthmanage.ui.contacts.view.RecyclerAdapter;
import com.bitvalue.healthmanage.util.UiUtil;
import com.bitvalue.healthmanage.widget.mpopupwindow.MPopupWindow;
import com.bitvalue.healthmanage.widget.mpopupwindow.TypeGravity;
import com.bitvalue.healthmanage.widget.mpopupwindow.ViewCallback;
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
    private ImageView img_nav;
    private MPopupWindow mPopupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts;
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
        img_nav = getView().findViewById(R.id.img_nav);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contact_list.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(getActivity(), contactsGroupBeans);
        adapter.setOnChildItemClickListener(new RecyclerAdapter.OnChildItemClickListener() {
            @Override
            public void onChildItemClick(ContactBean child, ExpandableGroup group, int childIndex, int flatPosition) {
                ContactsGroupBean clickGroup = (ContactsGroupBean) group;
                ToastUtils.show("父级是" + clickGroup.getTitle() + "###当前条目是" + child.getName());

                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, child);
            }
        });
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
//                showPartPop(R.layout.pop_contacts);
                showPop(R.layout.pop_contacts);
                break;
        }
    }

    private void showPop(int layoutId) {
        mPopupWindow = MPopupWindow.create(getActivity())
                .setLayoutId(layoutId)
//                .setBackgroundDrawable(new ColorDrawable(Color.GREEN))
                .setAnimationStyle(R.style.AnimDown)
                .setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (mPopupWindow != null) {
                        }
                    }
                })
                .setViewCallBack(viewCallback)
                .setTarget(layout_nav)
                .setGravity(TypeGravity.BOTTOM_MSG)
                .build();
        mPopupWindow.show();
    }

    private ViewCallback viewCallback = new ViewCallback() {
        @Override
        public void onInitView(View view, int mLayoutId) {
            switch (mLayoutId) {
                case R.layout.pop_contacts:
                    view.findViewById(R.id.tv_project).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.show("点击了计划");
                            startActivity(new Intent(getActivity(), LoginHealthActivity.class));
                        }
                    });

                    view.findViewById(R.id.tv_mul_msg).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            homeActivity.switchSecondFragment(Constants.FRAGMENT_SEND_MSG,"");
//                            mPopupWindow.dismiss();
//                            mPopupWindow = null;

                            startActivity(new Intent(homeActivity, LoginHealthActivity.class));
                        }
                    });
                    break;
            }
        }
    };

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
        childList.add(new ContactBean("张三", "10002"));
        childList.add(new ContactBean("李四", "10002"));
        childList.add(new ContactBean("王二", "10002"));
        ContactsGroupBean contactsGroupBean = new ContactsGroupBean("朋友", childList);
        contactsGroupBeans.add(contactsGroupBean);

        List<ContactBean> childList2 = new ArrayList<>();
        childList2.add(new ContactBean("小宝", "10002"));
        childList2.add(new ContactBean("爷爷", "10002"));
        childList2.add(new ContactBean("奶奶", "10002"));
        ContactsGroupBean contactsGroupBean2 = new ContactsGroupBean("亲人", childList2);
        contactsGroupBeans.add(contactsGroupBean2);
    }

    //冒泡弹出
    private void showPartPop(int layoutResId) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(getActivity()).inflate(layoutResId, null);
        //测量View的宽高
        UiUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(getActivity())
                .setView(layoutResId)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setBackGroundLevel(1f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .create();
//        popupWindow.showAsDropDown(layout_nav);
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
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
