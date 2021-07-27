package com.bitvalue.healthmanage.ui.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.ClientsApi;
import com.bitvalue.healthmanage.http.request.TestApi;
import com.bitvalue.healthmanage.http.response.ClientsResultBean;
import com.bitvalue.healthmanage.ui.contacts.bean.ContactBean;
import com.bitvalue.healthmanage.ui.contacts.bean.ContactsGroupBean;
import com.bitvalue.healthmanage.ui.contacts.view.RecyclerAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class ContactsFragment extends AppFragment {
    private boolean is_need_toast;
    private RecyclerView contact_list;
    private List<ContactsGroupBean> contactsGroupBeans = new ArrayList();
    private RecyclerAdapter adapter;

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
        getDatas();
        is_need_toast = getArguments().getBoolean("is_need_toast");
        contact_list = getView().findViewById(R.id.contact_list);
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

        geMyClients();
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
                if (true){

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
        ContactsGroupBean contactsGroupBean = new ContactsGroupBean("朋友",childList);
        contactsGroupBeans.add(contactsGroupBean);

        List<ContactBean> childList2 = new ArrayList<>();
        childList2.add(new ContactBean("小宝"));
        childList2.add(new ContactBean("爷爷"));
        childList2.add(new ContactBean("奶奶"));
        ContactsGroupBean contactsGroupBean2 = new ContactsGroupBean("亲人",childList2);
        contactsGroupBeans.add(contactsGroupBean2);
    }

    @Override
    protected void initData() {

    }
}
