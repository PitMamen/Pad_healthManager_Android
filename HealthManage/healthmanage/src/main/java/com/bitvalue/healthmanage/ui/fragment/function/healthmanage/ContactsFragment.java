package com.bitvalue.healthmanage.ui.fragment.function.healthmanage;

import static com.bitvalue.healthmanage.util.Constants.GET_CONVERSATION_COUNT;
import static com.bitvalue.healthmanage.util.ClickUtils.isFastClick;
import static com.bitvalue.healthmanage.util.ProcessUtils.getTotalMessage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.healthmanage.util.ClientResponse;
import com.bitvalue.healthmanage.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.healthmanage.ui.adapter.ClientsRecyclerAdapter;
import com.bitvalue.healthmanage.base.AppFragment;
import com.bitvalue.healthmanage.http.bean.ClientsResultBean;
import com.bitvalue.healthmanage.http.bean.MainRefreshObj;
import com.bitvalue.healthmanage.http.bean.MsgRemindObj;
import com.bitvalue.healthmanage.http.model.ApiResult;
import com.bitvalue.healthmanage.http.api.ClientsApi;
import com.bitvalue.healthmanage.ui.activity.main.HomeActivity;
import com.bitvalue.healthmanage.ui.activity.main.LoginHealthActivity;
import com.bitvalue.healthmanage.ui.fragment.common.ChatFragment;
import com.bitvalue.healthmanage.util.ProcessUtils;
import com.bitvalue.healthmanage.widget.popupwindow.MPopupWindow;
import com.bitvalue.healthmanage.widget.popupwindow.TypeGravity;
import com.bitvalue.healthmanage.callback.ViewCallback;
import com.bitvalue.healthmanage.widget.popupwindow.CommonPopupWindow;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IMEventListener;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayoutUI;
import com.bitvalue.sdk.collab.utils.TUIKitLog;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.tencent.imsdk.message.Message;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 健康管理联系人列表界面
 */
public class ContactsFragment extends AppFragment {

    @BindView(R.id.layout_choose)
    RelativeLayout layout_choose;

    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    @BindView(R.id.cb_choose)
    CheckBox cb_choose;

    @BindView(R.id.contact_list)
    RecyclerView contact_list;


    @BindView(R.id.layout_nav)
    LinearLayout layout_nav;

    @BindView(R.id.img_nav)
    ImageView img_nav;


    //    二级折叠Adapter
    private ClientsRecyclerAdapter adapter;


    private static final String TAG = "ContactsFragment";

    private boolean is_need_toast;
    private HomeActivity homeActivity;
    private CommonPopupWindow popupWindow;
    private MPopupWindow mPopupWindow;
    private ArrayList<String> mIds = new ArrayList<>();
    private ArrayList<ClientsResultBean> clientsResultBeans = new ArrayList<>();
    private ArrayList<ClientsResultBean> clientsProcessBeans = new ArrayList<>();
    private int newCount = 0;
    private List<V2TIMConversation> v2TIMConversationList;
    private int totalNum = 0;

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
        TUIKit.addIMEventListener(mIMEventListener);
        homeActivity = (HomeActivity) getActivity();

        is_need_toast = getArguments().getBoolean("is_need_toast");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contact_list.setLayoutManager(layoutManager);

        getMyClients(false);
        adapter = new ClientsRecyclerAdapter(getActivity(), clientsProcessBeans);
        adapter.setOnChildItemClickListener((child, group, childIndex, flatPosition) -> {
            if (isFastClick()) return;

            // ytpe = 100 健康管理  ytpe = 101 云门诊
            child.chatType = InputLayoutUI.CHAT_TYPE_HEALTH;
//            点击患者 切换至聊天界面
            homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, child);
            totalNum = getTotalMessage(clientsProcessBeans, child.goodsName, child.userId);
            EventBus.getDefault().post(new MsgRemindObj(1, totalNum));//通知主界面图标显示未读取消息
            adapter.notifyDataSetChanged();
        });

        adapter.setOnChildCheckListener((isCheck, childIndex, child) -> {
            if (isCheck) {
                mIds.add(child.groupID + "");
            } else {
                if (mIds.contains(child.groupID + "")) {
                    mIds.remove(child.groupID + "");
                }
            }
        });
        adapter.setOnGroupClickListener(flatPos -> false);
        adapter.setOnGroupExpandCollapseListener(new GroupExpandCollapseListener() {
            @Override
            public void onGroupExpanded(ExpandableGroup group) {
                ClientsResultBean clientsResultBean = (ClientsResultBean) group;
                totalNum = 0;
                for (int i = 0; i < clientsProcessBeans.size(); i++) {
                    if (clientsProcessBeans.get(i).group.equals(clientsResultBean.group)) {
                        clientsProcessBeans.get(i).newMsgNum = 0;
                    }
                    totalNum = totalNum + clientsProcessBeans.get(i).newMsgNum;
                }
                EventBus.getDefault().post(new MsgRemindObj(1, totalNum));//通知主界面图标显示未读取消息

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onGroupCollapsed(ExpandableGroup group) {

            }
        });
        contact_list.setAdapter(adapter);

//        getMyClients(false);

        cb_choose.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {//全选
                mIds.clear();
                for (int i = 0; i < clientsProcessBeans.size(); i++) {
                    for (int j = 0; j < clientsProcessBeans.get(i).userInfo.size(); j++) {
                        clientsProcessBeans.get(i).userInfo.get(j).isChecked = true;
                        clientsProcessBeans.get(i).userInfo.get(j).isShowCheck = true;
                        if (!mIds.contains(clientsProcessBeans.get(i).userInfo.get(j).groupID)) {
                            mIds.add(clientsProcessBeans.get(i).userInfo.get(j).groupID + "");
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
//                            clientsProcessBeans.get(i).userInfo.get(j).isShowCheck = false;
                    }
                }
            }
            adapter.notifyDataSetChanged();
        });
    }


//    homeActivity中点击健康管理联系人按钮 发送过来的监听
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainRefreshObj mainRefreshObj) {
        clientsResultBeans.clear();
        clientsProcessBeans.clear();
        getMyClients(false);
    }

    // 监听做成静态可以让每个子类重写时都注册相同的一份。
    private IMEventListener mIMEventListener = new IMEventListener() {
        @Override
        public void onNewMessage(V2TIMMessage v2TIMMessage) {
            super.onNewMessage(v2TIMMessage);
            String s = new Gson().toJson(v2TIMMessage);
            Log.d("http", s);
            if (null != v2TIMMessage) {
                Message message = v2TIMMessage.getMessage();
                totalNum = 0;
                for (int i = 0; i < clientsProcessBeans.size(); i++) {
                    for (int j = 0; j < clientsProcessBeans.get(i).userInfo.size(); j++) {
                        if (clientsProcessBeans.get(i).userInfo.get(j).groupID.equals(message.getGroupID())) {
                            clientsProcessBeans.get(i).userInfo.get(j).hasNew = true;
                            clientsProcessBeans.get(i).userInfo.get(j).newMsgNum = clientsProcessBeans.get(i).userInfo.get(j).newMsgNum + 1;
                            clientsProcessBeans.get(i).newMsgNum++;
                        }

                    }
                    totalNum = totalNum + clientsProcessBeans.get(i).newMsgNum;
                }
                adapter.notifyDataSetChanged();
                EventBus.getDefault().post(new MsgRemindObj(1, totalNum));//通知主界面图标显示未读取消息
            }
        }
    };

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.layout_nav, R.id.tv_send_msg, R.id.tv_no_data})
    public void onClick(View view) {
        switch (view.getId()) {
//            点击右上角三点图片展示 popwind
            case R.id.layout_nav:
                showPop(R.layout.pop_contacts);
                break;

//                当前界面无数据 点击刷新数据
            case R.id.tv_no_data:
                getMyClients(true);
                break;

//                点击确定  群发消息
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
                    homeActivity.switchSecondFragment(Constants.FRAGMENT_SEND_MSG, msgData);  //跳转至群发消息界面

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

//    展示 popwind
    private void showPop(int layoutId) {
        mPopupWindow = MPopupWindow.create(getActivity())
                .setLayoutId(layoutId)
                .setAnimationStyle(R.style.AnimDown)
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

                    view.findViewById(R.id.tv_project).setOnClickListener(view1 -> {
                        startActivity(new Intent(getActivity(), LoginHealthActivity.class));
                    });

                    view.findViewById(R.id.tv_mul_msg).setOnClickListener(v -> {
//                            if (mIds.size() == 0) {
//                                homeActivity.startActivity(new Intent(homeActivity, NewMsgActivity.class));
//                                return;
//                            }
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
                    });
                    break;
            }
        }
    };


    /***
     * 网络请求，获取患者客户+获取会话列表
     * @param needToast
     */
    private void getMyClients(boolean needToast) {

        V2TIMManager.getConversationManager().getConversationList(0, GET_CONVERSATION_COUNT, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.v("getConversationList", "loadConversation getConversationList error, code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                v2TIMConversationList = v2TIMConversationResult.getConversationList();  //获取会话列表


                EasyHttp.post(ContactsFragment.this).api(new ClientsApi()).request(new HttpCallback<ApiResult<ArrayList<ClientsResultBean>>>(ContactsFragment.this) {
                    @Override
                    public void onStart(Call call) {
                        super.onStart(call);
                    }

                    @Override
                    public void onSucceed(ApiResult<ArrayList<ClientsResultBean>> result) {
                        super.onSucceed(result);
                        clientsResultBeans = result.getData();
                        if (null == clientsResultBeans || clientsResultBeans.size() == 0) {
                            if (needToast) {
                                ToastUtil.toastShortMessage("暂无客户数据");
                            }
                            contact_list.setVisibility(View.GONE);
                            tv_no_data.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            contact_list.setVisibility(View.VISIBLE);
                            tv_no_data.setVisibility(View.GONE);
                        }
                        totalNum = ProcessUtils.dataProcessing(clientsProcessBeans, clientsResultBeans, homeActivity, v2TIMConversationList);
                        EventBus.getDefault().post(new MsgRemindObj(1, totalNum)); //通知主界面图标显示未读取消息

                        //检测数据，无数据显示刷新按钮
                        contact_list.setVisibility(clientsProcessBeans.size() == 0 ? View.GONE : View.VISIBLE);
                        tv_no_data.setVisibility(clientsProcessBeans.size() == 0 ? View.VISIBLE : View.GONE);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

    }
}
