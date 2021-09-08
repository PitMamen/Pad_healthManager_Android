package com.bitvalue.healthmanage.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.app.AppFragment;
import com.bitvalue.healthmanage.http.model.HttpData;
import com.bitvalue.healthmanage.http.request.ClientsApi;
import com.bitvalue.healthmanage.http.response.ArticleBean;
import com.bitvalue.healthmanage.http.response.ClientsResultBean;
import com.bitvalue.healthmanage.ui.activity.HomeActivity;
import com.bitvalue.healthmanage.ui.activity.LoginHealthActivity;
import com.bitvalue.healthmanage.ui.contacts.bean.MainRefreshObj;
import com.bitvalue.healthmanage.ui.contacts.view.ClientsRecyclerAdapter;
import com.bitvalue.healthmanage.widget.mpopupwindow.MPopupWindow;
import com.bitvalue.healthmanage.widget.mpopupwindow.TypeGravity;
import com.bitvalue.healthmanage.widget.mpopupwindow.ViewCallback;
import com.bitvalue.healthmanage.widget.popupwindow.CommonPopupWindow;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayoutUI;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

//import butterknife.OnClick;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 健康管理联系人列表
 */
public class ContactsFragment extends AppFragment {
    @BindView(R.id.layout_choose)
    RelativeLayout layout_choose;

    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    @BindView(R.id.cb_choose)
    CheckBox cb_choose;

    private boolean is_need_toast;
    private RecyclerView contact_list;
    private ClientsRecyclerAdapter adapter;
    private HomeActivity homeActivity;
    private CommonPopupWindow popupWindow;
    private LinearLayout layout_nav;
    private ImageView img_nav;
    private MPopupWindow mPopupWindow;
    private ArrayList<String> mIds = new ArrayList<>();
    private ArrayList<ClientsResultBean> clientsResultBeans = new ArrayList<>();
    private ArrayList<ClientsResultBean> clientsProcessBeans = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    public static ContactsFragment getInstance(boolean is_need_toast) {
        ContactsFragment contactsFragment = new ContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_toast", is_need_toast);
        contactsFragment.setArguments(bundle);
        return contactsFragment;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        homeActivity = (HomeActivity) getActivity();

        is_need_toast = getArguments().getBoolean("is_need_toast");
        contact_list = getView().findViewById(R.id.contact_list);
        layout_nav = getView().findViewById(R.id.layout_nav);
        img_nav = getView().findViewById(R.id.img_nav);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contact_list.setLayoutManager(layoutManager);

        adapter = new ClientsRecyclerAdapter(getActivity(), clientsProcessBeans);
        adapter.setOnChildItemClickListener(new ClientsRecyclerAdapter.OnChildItemClickListener() {
            @Override
            public void onChildItemClick(ClientsResultBean.UserInfoDTO child, ExpandableGroup group, int childIndex, int flatPosition) {
                ClientsResultBean clientsResultBean = (ClientsResultBean) group;
                //TODO 演示看诊数据，本来全是健康管理数据，这里本来全部赋值 100
                if (childIndex == 0) {
                    child.chatType = InputLayoutUI.CHAT_TYPE_VIDEO;
                } else {
                    child.chatType = InputLayoutUI.CHAT_TYPE_HEALTH;
                }
                homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, child);
            }
        });

        adapter.setOnChildCheckListener(new ClientsRecyclerAdapter.OnChildCheckListener() {
            @Override
            public void onChildCheck(boolean isCheck, int childIndex, ClientsResultBean.UserInfoDTO child) {
//                ToastUtil.toastShortMessage("是否勾选：" + isCheck + "---勾选哪一个" + childIndex);
                if (isCheck) {
                    mIds.add(child.userId + "");
                } else {
                    if (mIds.contains(child.userId + "")) {
                        mIds.remove(child.userId + "");
                    }
                }
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

        getMyClients();

        cb_choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//全选
                    mIds.clear();
                    for (int i = 0; i < clientsProcessBeans.size(); i++) {
                        for (int j = 0; j < clientsProcessBeans.get(i).userInfo.size(); j++) {
                            clientsProcessBeans.get(i).userInfo.get(j).isChecked = true;
                            clientsProcessBeans.get(i).userInfo.get(j).isShowCheck = true;
                            if (!mIds.contains(clientsProcessBeans.get(i).userInfo.get(j).userId)) {
                                mIds.add(clientsProcessBeans.get(i).userInfo.get(j).userId + "");
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {//全部不选
                    //还要清除选择记录
                    mIds.clear();
                    for (int i = 0; i < clientsProcessBeans.size(); i++) {
                        for (int j = 0; j < clientsProcessBeans.get(i).userInfo.size(); j++) {
                            clientsProcessBeans.get(i).userInfo.get(j).isChecked = false;
                            clientsProcessBeans.get(i).userInfo.get(j).isShowCheck = false;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainRefreshObj mainRefreshObj) {
        clientsResultBeans.clear();
        clientsProcessBeans.clear();
        getMyClients();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.layout_nav, R.id.tv_send_msg, R.id.tv_no_data})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_nav:
                showPop(R.layout.pop_contacts);
                break;

            case R.id.tv_no_data:
                getMyClients();
                break;

            case R.id.tv_send_msg:
                if (mIds.size() == 0) {
                    ToastUtil.toastShortMessage("请选择群发用户");
                    return;
                } else {
                    layout_choose.setVisibility(View.GONE);
                    //跳转发送消息页面
                    ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                    msgData.msgType = Constants.MSG_MULTI;

                    ArrayList<String> newList = new ArrayList<String>();
                    for (String cd : mIds) {
                        if (!newList.contains(cd)) {
                            newList.add(cd);
                        }
                    }
                    msgData.userIds = newList;
                    homeActivity.switchSecondFragment(Constants.FRAGMENT_SEND_MSG, msgData);

                    //还要清除选择记录
                    mIds.clear();
                    for (int i = 0; i < clientsProcessBeans.size(); i++) {
                        for (int j = 0; j < clientsProcessBeans.get(i).userInfo.size(); j++) {
                            clientsProcessBeans.get(i).userInfo.get(j).isChecked = false;
                            clientsProcessBeans.get(i).userInfo.get(j).isShowCheck = false;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void showPop(int layoutId) {
        mPopupWindow = MPopupWindow.create(getActivity())
                .setLayoutId(layoutId)
//                .setBackgroundDrawable(new ColorDrawable(Color.GREEN))
                .setAnimationStyle(R.style.AnimDown)
//                .setOutsideTouchable(false)
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

                            //step1 先将所有用户置为可选取
                            for (int i = 0; i < clientsProcessBeans.size(); i++) {
                                for (int j = 0; j < clientsProcessBeans.get(i).userInfo.size(); j++) {
                                    clientsProcessBeans.get(i).userInfo.get(j).isChecked = false;
                                    clientsProcessBeans.get(i).userInfo.get(j).isShowCheck = true;
                                }
                            }
                            adapter.notifyDataSetChanged();

                            //step2 展示确定按钮
                            if (clientsProcessBeans.size() == 0) {
                                ToastUtil.toastShortMessage("暂无客户数据");
                                //step3 关闭弹窗
                                mPopupWindow.dismiss();
                                mPopupWindow = null;
                                return;
                            }
                            layout_choose.setVisibility(View.VISIBLE);
                            cb_choose.setChecked(false);

                            //step3 关闭弹窗
                            mPopupWindow.dismiss();
                            mPopupWindow = null;
                        }
                    });
                    break;
            }
        }
    };

    private void getMyClients() {
        EasyHttp.post(this).api(new ClientsApi()).request(new HttpCallback<HttpData<ArrayList<ClientsResultBean>>>(this) {
            @Override
            public void onStart(Call call) {
                super.onStart(call);
            }

            @Override
            public void onSucceed(HttpData<ArrayList<ClientsResultBean>> result) {
                super.onSucceed(result);
                clientsResultBeans = result.getData();
                if (null == clientsResultBeans || clientsResultBeans.size() == 0) {
                    ToastUtil.toastShortMessage("暂无客户数据");
                    contact_list.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
                    return;
                } else {
                    contact_list.setVisibility(View.VISIBLE);
                    tv_no_data.setVisibility(View.GONE);
                }
                processData();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
            }
        });
    }

    private void processData() {
        for (int i = 0; i < clientsResultBeans.size(); i++) {
            ClientsResultBean clientsResultBean = clientsResultBeans.get(i);
            List<ClientsResultBean.UserInfoDTO> userInfo = clientsResultBean.userInfo;
            if (null == userInfo || userInfo.size() == 0) {
                continue;
//                userInfo = new ArrayList<>();
//                ClientsResultBean.UserInfoDTO userInfoDTO = new ClientsResultBean.UserInfoDTO("暂无");
//                userInfo.add(userInfoDTO);
            }
            ClientsResultBean newOne = new ClientsResultBean(clientsResultBean.group, userInfo);
            newOne.userInfo = userInfo;
            newOne.num = clientsResultBean.num;
            newOne.group = clientsResultBean.group;
            clientsProcessBeans.add(newOne);
        }

        //检测数据，无数据提高刷新按钮
        if (clientsProcessBeans.size() == 0) {
            ToastUtil.toastShortMessage("暂无客户数据");
            contact_list.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
        } else {
            contact_list.setVisibility(View.VISIBLE);
            tv_no_data.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {

    }
}
