package com.bitvalue.healthmanage.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.bitvalue.healthmanage.util.DemoLog;
import com.bitvalue.healthmanage.util.UiUtil;
import com.bitvalue.healthmanage.widget.mpopupwindow.MPopupWindow;
import com.bitvalue.healthmanage.widget.mpopupwindow.TypeGravity;
import com.bitvalue.healthmanage.widget.mpopupwindow.ViewCallback;
import com.bitvalue.healthmanage.widget.popupwindow.CommonPopupWindow;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.tencent.imsdk.BaseConstants;
import com.tencent.imsdk.v2.V2TIMFriendAddApplication;
import com.tencent.imsdk.v2.V2TIMFriendOperationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

//import butterknife.OnClick;
import butterknife.OnClick;
import okhttp3.Call;

public class ContactsFragment extends AppFragment implements CommonPopupWindow.ViewInterface {
    private boolean is_need_toast;
    private RecyclerView contact_list;
    private RecyclerAdapter adapter;
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
        Bundle bundle = new Bundle();//TODO 参数可改
        bundle.putBoolean("is_need_toast", is_need_toast);
        contactsFragment.setArguments(bundle);
        return contactsFragment;
    }

    @Override
    protected void initView() {
        homeActivity = (HomeActivity) getActivity();
//        setOnClickListener(R.id.layout_nav);

        is_need_toast = getArguments().getBoolean("is_need_toast");
        contact_list = getView().findViewById(R.id.contact_list);
        layout_nav = getView().findViewById(R.id.layout_nav);
        img_nav = getView().findViewById(R.id.img_nav);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contact_list.setLayoutManager(layoutManager);

//        adapter = new RecyclerAdapter(getActivity(), contactsGroupBeans);
        adapter = new RecyclerAdapter(getActivity(), clientsProcessBeans);
        adapter.setOnChildItemClickListener(new RecyclerAdapter.OnChildItemClickListener() {
            @Override
            public void onChildItemClick(ClientsResultBean.UserInfoDTO child, ExpandableGroup group, int childIndex, int flatPosition) {
                ClientsResultBean clientsResultBean = (ClientsResultBean) group;
                ToastUtils.show("父级是" + clientsResultBean.getTitle() + "###当前条目是" + child.userName);

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

        geMyClients();
    }


    /**
     * 添加好友
     *
     * @param id
     */
    public void addFriend(String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }

        V2TIMFriendAddApplication v2TIMFriendAddApplication = new V2TIMFriendAddApplication(id);
        v2TIMFriendAddApplication.setAddWording("添加好友哦");
        v2TIMFriendAddApplication.setAddSource("android");
        V2TIMManager.getFriendshipManager().addFriend(v2TIMFriendAddApplication, new V2TIMValueCallback<V2TIMFriendOperationResult>() {
            @Override
            public void onError(int code, String desc) {
                DemoLog.e("addFriend", "addFriend err code = " + code + ", desc = " + desc);
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(V2TIMFriendOperationResult v2TIMFriendOperationResult) {
                DemoLog.i("addFriend", "addFriend success");
                switch (v2TIMFriendOperationResult.getResultCode()) {
                    case BaseConstants.ERR_SUCC:
                        ToastUtil.toastShortMessage(getString(R.string.success));
                        break;
                    case BaseConstants.ERR_SVR_FRIENDSHIP_INVALID_PARAMETERS:
                        if (TextUtils.equals(v2TIMFriendOperationResult.getResultInfo(), "Err_SNS_FriendAdd_Friend_Exist")) {
                            ToastUtil.toastShortMessage(getString(R.string.have_be_friend));
                            break;
                        }
                    case BaseConstants.ERR_SVR_FRIENDSHIP_COUNT_LIMIT:
                        ToastUtil.toastShortMessage(getString(R.string.friend_limit));
                        break;
                    case BaseConstants.ERR_SVR_FRIENDSHIP_PEER_FRIEND_LIMIT:
                        ToastUtil.toastShortMessage(getString(R.string.other_friend_limit));
                        break;
                    case BaseConstants.ERR_SVR_FRIENDSHIP_IN_SELF_BLACKLIST:
                        ToastUtil.toastShortMessage(getString(R.string.in_blacklist));
                        break;
                    case BaseConstants.ERR_SVR_FRIENDSHIP_ALLOW_TYPE_DENY_ANY:
                        ToastUtil.toastShortMessage(getString(R.string.forbid_add_friend));
                        break;
                    case BaseConstants.ERR_SVR_FRIENDSHIP_IN_PEER_BLACKLIST:
                        ToastUtil.toastShortMessage(getString(R.string.set_in_blacklist));
                        break;
                    case BaseConstants.ERR_SVR_FRIENDSHIP_ALLOW_TYPE_NEED_CONFIRM:
                        ToastUtil.toastShortMessage(getString(R.string.wait_agree_friend));
                        break;
                    default:
                        ToastUtil.toastLongMessage(v2TIMFriendOperationResult.getResultCode() + " " + v2TIMFriendOperationResult.getResultInfo());
                        break;
                }
                finish();
            }
        });
    }

    @OnClick({R.id.layout_nav})
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
                            if (mIds.size() == 0) {
                                ToastUtil.toastShortMessage("请选择群发用户");
                                return;
                            }
                            ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                            msgData.msgType = Constants.MSG_MULTI;
                            msgData.userIds = mIds;
                            homeActivity.switchSecondFragment(Constants.FRAGMENT_SEND_MSG, msgData);
                            mPopupWindow.dismiss();
                            mPopupWindow = null;
                        }
                    });
                    break;
            }
        }
    };

    private void geMyClients() {
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
                    return;
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
