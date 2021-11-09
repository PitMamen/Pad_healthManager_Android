package com.bitvalue.health.ui.fragment.healthmanage;

import static com.bitvalue.health.util.ClickUtils.isFastClick;
import static com.bitvalue.health.util.Constants.EVENTBUS_MES_HEALTHMANAGER;
import static com.bitvalue.health.util.Constants.GET_CONVERSATION_COUNT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.health.api.eventbusbean.MainRefreshObj;
import com.bitvalue.health.api.eventbusbean.MsgRemindObj;
import com.bitvalue.health.api.responsebean.ClientsResultBean;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.callback.OnTouchListener;
import com.bitvalue.health.callback.ViewCallback;
import com.bitvalue.health.contract.healthmanagercontract.HealthManageContract;
import com.bitvalue.health.presenter.healthmanager.HealthManagePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.ui.activity.LoginHealthActivity;
import com.bitvalue.health.ui.adapter.ClientsRecyclerAdapter;
import com.bitvalue.health.ui.fragment.chat.ChatFragment;
import com.bitvalue.health.util.ClickUtils;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.customview.MPopupWindow;
import com.bitvalue.health.util.customview.TypeGravity;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayoutUI;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.toast.ToastUtils;
import com.tencent.imsdk.message.Message;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author created by bitvalue
 * @data : 10/27
 * 健康计划管理 界面
 */
public class ContactsFragment extends BaseFragment<HealthManagePresenter> implements HealthManageContract.View, OnTouchListener {

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

    @BindView(R.id.layout_cons)
    ConstraintLayout constraintLayout;


    private ClientsRecyclerAdapter adapter;
    private HomeActivity homeActivity;


    private ArrayList<String> mIds = new ArrayList<>();
    private ArrayList<ClientsResultBean> clientsResultBeans = new ArrayList<>();
    private ArrayList<ClientsResultBean> clientsProcessBeans = new ArrayList<>();
    private ArrayList<ClientsResultBean> clientsTempProcessBeans = new ArrayList<>();
    private List<V2TIMConversation> v2TIMConversationList;
    private int totalNum = 0;
    private MPopupWindow mPopupWindow;
    private String lastChildUserID = "";  //上一次点击的患者userID
    private int lastPosition = 0;   //上一次点击的position

    @Override
    protected HealthManagePresenter createPresenter() {
        return new HealthManagePresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_contacts;
    }

    public static ContactsFragment getInstance(boolean is_need_toast) {
        ContactsFragment contactsFragment = new ContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_need_toast", is_need_toast);
        contactsFragment.setArguments(bundle);
        return contactsFragment;
    }

    /**
     * 接收来住Homeactivity消息 获取健康计划客户
     *
     * @param mainRefreshObj
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MainRefreshObj mainRefreshObj) {
        if (clientsProcessBeans.size() > 0 && clientsTempProcessBeans.size() == clientsProcessBeans.size()) {
            Log.e(TAG, "clientsProcessBeans size == clientsTempProcessBeans size  ");
            adapter.notifyDataSetChanged();
            mPresenter.getMyPatients();
            return;
        }
        clientsResultBeans.clear();
        clientsProcessBeans.clear();
        clientsTempProcessBeans.clear();
        mPresenter.getMyPatients();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
        homeActivity.setOnTouchListener(this);
    }


    /**
     * 初始化 各个控件 及IM
     *
     * @param rootView
     */
    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        EventBus.getDefault().register(this);
        mPresenter.getConversationList(0, GET_CONVERSATION_COUNT);  // 获取聊天对话框
        mPresenter.listennerIMNewMessage();//监听IM新消息
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contact_list.setLayoutManager(layoutManager);

        adapter = new ClientsRecyclerAdapter(getActivity(), clientsProcessBeans);
        adapter.setOnChildItemClickListener((child, group, childIndex, flatPosition) -> {
            //这里防止用户连续点击同一个患者
            if (child.userId.equals(lastChildUserID) && lastPosition == flatPosition) {
                Log.e(TAG, "同一个ID---");
                return;
            }
            if (isFastClick()) {
                return;
            }

            lastChildUserID = child.userId;
            lastPosition = flatPosition;
            child.chatType = InputLayoutUI.CHAT_TYPE_HEALTH;
            homeActivity.switchSecondFragment(Constants.FRAGMENT_CHAT, child);  //跳转至聊天界面

            totalNum = 0;
            for (int i = 0; i < clientsProcessBeans.size(); i++) {
                for (int j = 0; j < clientsProcessBeans.get(i).userInfo.size(); j++) {
                    clientsProcessBeans.get(i).userInfo.get(j).isClicked = false;
                    if (clientsProcessBeans.get(i).group.equals(child.goodsName) && clientsProcessBeans.get(i).userInfo.get(j).userId == child.userId) {
                        clientsProcessBeans.get(i).userInfo.get(j).isClicked = true;

                        //找到点击的子项目，如果有新信息，置为已查阅;并把父级的消息数减去子消息数
                        if (clientsProcessBeans.get(i).userInfo.get(j).hasNew) {
                            clientsProcessBeans.get(i).userInfo.get(j).hasNew = false;
                            clientsProcessBeans.get(i).newMsgNum = clientsProcessBeans.get(i).newMsgNum - clientsProcessBeans.get(i).userInfo.get(j).newMsgNum;
                            if (clientsProcessBeans.get(i).newMsgNum <= 0) {
                                clientsProcessBeans.get(i).newMsgNum = 0;
                            }
                            clientsProcessBeans.get(i).userInfo.get(j).newMsgNum = 0;
                        }
                    }
                }
                totalNum = totalNum + clientsProcessBeans.get(i).newMsgNum;
            }
            EventBus.getDefault().post(new MsgRemindObj(EVENTBUS_MES_HEALTHMANAGER, totalNum));
            adapter.notifyDataSetChanged();
        });
        adapter.setOnChildCheckListener((isCheck, childIndex, child) -> {
            if (isCheck) {
                mIds.add(String.valueOf(child.groupID));
            } else {
                if (mIds.contains(String.valueOf(child.groupID))) {
                    mIds.remove(String.valueOf(child.groupID));
                    if (layout_choose.getVisibility() == View.VISIBLE && cb_choose.isChecked()) {
                        cb_choose.setOnCheckedChangeListener(null);  //这里CheckBox 一点要设置监听为null，不然会影响整个全选状态
                        cb_choose.setChecked(false);
                        cb_choose.setOnCheckedChangeListener(onCheckedChangeListener);  //重新设置监听
                    }
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
                EventBus.getDefault().post(new MsgRemindObj(EVENTBUS_MES_HEALTHMANAGER, totalNum));

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onGroupCollapsed(ExpandableGroup group) {

            }
        });
        contact_list.setAdapter(adapter);
        cb_choose.setOnCheckedChangeListener(onCheckedChangeListener);

    }


    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    };


    /**
     * 各个按钮的回调点击事件
     *
     * @param view
     */
    @OnClick({R.id.layout_nav, R.id.tv_send_msg, R.id.tv_no_data, R.id.layout_cons})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_nav:
                showPop(R.layout.pop_contacts);
                break;

            //获取数据
            case R.id.tv_no_data:
                if (ClickUtils.isFastClickThree()) {
                    return;
                }
                mPresenter.getMyPatients();
                break;


            //点击确定发送消息
            case R.id.tv_send_msg:
                if (mIds.size() == 0) {
                    ToastUtil.toastShortMessage("请选择群发用户");
                    return;
                } else {
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
                    clearIDHidechooseLayout();
                }

            case R.id.layout_cons:
                //还要清除选择记录
                clearIDHidechooseLayout();
                break;


            default:
                break;


        }
    }


    /**
     * 当每次隐藏和popwind控件时,也需要隐藏adapter的item 并且不让它再为可选状态
     */
    private void clearIDHidechooseLayout() {
        mIds.clear();
        layout_choose.setVisibility(View.GONE);
        for (int i = 0; i < clientsProcessBeans.size(); i++) {
            for (int j = 0; j < clientsProcessBeans.get(i).userInfo.size(); j++) {
                clientsProcessBeans.get(i).userInfo.get(j).isChecked = false;
                clientsProcessBeans.get(i).userInfo.get(j).isShowCheck = false;
            }
        }
        adapter.notifyDataSetChanged();
    }

    //左上角的popwind窗户的显示
    private void showPop(int layoutId) {
        mPopupWindow = MPopupWindow.create(getActivity())
                .setLayoutId(layoutId)
                .setAnimationStyle(R.style.AnimDown)
                .setOnDismissListener(() -> {
                    if (mPopupWindow != null) {
                    }
                })
                .setViewCallBack(viewCallback)
                .setTarget(layout_nav)
                .setGravity(TypeGravity.BOTTOM_MSG)
                .build();
        mPopupWindow.show();
    }


    /**
     * 点击右上角popwind的监听 隐藏和显示的逻辑在这里
     */
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
                        }
                    });
                    break;
            }
        }
    };


    /**
     * 这里如果不再当前fragment了   再跳转回去至当前界面 需要隐藏掉之前显示的popwind，需要用户重新点击触发显示
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //这里如果 当前Fragment已经隐藏掉了，PopupWindow又在显示中，再次显示当前Fragment的时候 需要重新点击popwind才能让其显示
        if (hidden) {
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
                mPopupWindow = null;
            }
            if (layout_choose.getVisibility() == View.VISIBLE) {
                layout_choose.setVisibility(View.GONE);
            }
        }

    }


    /**
     * 选择群发消息时 点击最右边的Fragment界面 的回调
     *
     * @param
     */
    @Override
    public void onthirdFragmentListenner() {
        if (layout_choose.getVisibility() == View.VISIBLE)
            clearIDHidechooseLayout();
    }


    /**
     * 获取对话框成功
     *
     * @param v2TIMConversationResult
     */
    @Override
    public void getConversationSuccess(V2TIMConversationResult v2TIMConversationResult) {
        v2TIMConversationList = v2TIMConversationResult.getConversationList();
        clientsResultBeans.clear();
        clientsProcessBeans.clear();
        clientsTempProcessBeans.clear();
        mPresenter.getMyPatients();
    }

    /**
     * 获取对话框失败
     *
     * @param
     */
    @Override
    public void getConversationfail(int code, String message) {

    }


    /**
     * 监听新消息
     *
     * @param v2TIMMessage
     */
    @Override
    public void onNewMessage(V2TIMMessage v2TIMMessage) {
//        String s = new Gson().toJson(v2TIMMessage);
//        Log.d("http", s);
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
            if (null != adapter) {
                adapter.notifyDataSetChanged();
            }
            EventBus.getDefault().post(new MsgRemindObj(EVENTBUS_MES_HEALTHMANAGER, totalNum));
        }
    }


    /**
     * 获取健康计划客户成功
     *
     * @param beanArrayList
     */
    @Override
    public void getPatientSuccess(ArrayList<ClientsResultBean> beanArrayList) {
        getActivity().runOnUiThread(() -> {
            contact_list.setVisibility(beanArrayList.size() == 0 ? View.GONE : View.VISIBLE);
            tv_no_data.setVisibility(beanArrayList.size() != 0 ? View.GONE : View.VISIBLE);
            clientsResultBeans.clear();
            clientsProcessBeans.clear();
            clientsTempProcessBeans.clear();
            clientsResultBeans = beanArrayList;
            clientsTempProcessBeans = beanArrayList;
            processData();
            adapter.notifyDataSetChanged();
        });

    }


    /**
     * 获取健康计划客户失败
     */
    @Override
    public void getPatientFail(String failmessage) {
        getActivity().runOnUiThread(() -> ToastUtils.show("获取健康管理用户失败:" + failmessage));
    }


    //这里数据转换 可以优化,等这个测试版本通过之后 优化此处
    private void processData() {
        for (int i = 0; i < clientsResultBeans.size(); i++) {
            ClientsResultBean clientsResultBean = clientsResultBeans.get(i);
            List<ClientsResultBean.UserInfoDTO> userInfo = clientsResultBean.userInfo;
            //处理数据，去掉没有集合的套餐
            if (null == userInfo || userInfo.size() == 0) {
                continue;
            }

            //用新的集合接收空套餐的数据
            ClientsResultBean newOne = new ClientsResultBean(clientsResultBean.group, userInfo);

            LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
            if (loginBean == null) {
                return;
            }

            //循环赋groupId，以及赋值未读消息数
            for (int x = 0; x < userInfo.size(); x++) {
                userInfo.get(x).groupID = userInfo.get(x).goodsId + loginBean.getUser().user.userId + userInfo.get(x).userId;

                userInfo.get(x).newMsgNum = DataUtil.getUnreadCount(false, userInfo.get(x).groupID, v2TIMConversationList);
                if (userInfo.get(x).newMsgNum > 0) {
                    userInfo.get(x).hasNew = true;
                }
            }

            //组装完数据
            newOne.userInfo = userInfo;
            newOne.num = clientsResultBean.num;
            newOne.group = clientsResultBean.group;
            clientsProcessBeans.add(newOne);
        }

        //获取每个套餐总数，以及所有套餐总数
        totalNum = 0;
        for (int i = 0; i < clientsProcessBeans.size(); i++) {
            for (int j = 0; j < clientsProcessBeans.get(i).userInfo.size(); j++) {
                //获取单个套餐的未读消息数
                clientsProcessBeans.get(i).newMsgNum = clientsProcessBeans.get(i).newMsgNum + clientsProcessBeans.get(i).userInfo.get(j).newMsgNum;
            }

            if (clientsProcessBeans.get(i).newMsgNum <= 0) {
                clientsProcessBeans.get(i).newMsgNum = 0;
            }

            //获取所有套餐的总未读消息数
            totalNum = totalNum + clientsProcessBeans.get(i).newMsgNum;
        }
        EventBus.getDefault().post(new MsgRemindObj(EVENTBUS_MES_HEALTHMANAGER, totalNum));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
