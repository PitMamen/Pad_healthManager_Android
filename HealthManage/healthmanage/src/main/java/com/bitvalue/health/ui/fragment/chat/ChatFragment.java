package com.bitvalue.health.ui.fragment.chat;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.ApiResult;
import com.bitvalue.health.api.eventbusbean.VideoRefreshObj;
import com.bitvalue.health.api.requestbean.ReportStatusApi;
import com.bitvalue.health.api.requestbean.ReportStatusBean;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.api.responsebean.VideoClientsResultBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.contract.chattract.Chatcontract;
import com.bitvalue.health.presenter.chatpersenter.ChatPersenter;
import com.bitvalue.health.ui.activity.HealthFilesActivity;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.bitvalue.sdk.collab.component.AudioPlayer;
import com.bitvalue.sdk.collab.component.TitleBarLayout;
import com.bitvalue.sdk.collab.helper.ChatLayoutHelper;
import com.bitvalue.sdk.collab.helper.CustomMessage;
import com.bitvalue.sdk.collab.helper.CustomVideoCallMessage;
import com.bitvalue.sdk.collab.modules.chat.base.AbsChatLayout;
import com.bitvalue.sdk.collab.modules.chat.base.ChatInfo;
import com.bitvalue.sdk.collab.modules.chat.base.ChatManagerKit;
import com.bitvalue.sdk.collab.modules.chat.layout.input.ChatLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.forward.ForwardSelectActivity;
import com.bitvalue.sdk.collab.modules.forward.base.ConversationBean;
import com.bitvalue.sdk.collab.modules.group.info.GroupInfo;
import com.bitvalue.sdk.collab.modules.group.info.StartGroupMemberSelectActivity;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.bitvalue.sdk.collab.modules.message.MessageInfoUtil;
import com.bitvalue.sdk.collab.utils.TUIKitConstants;
import com.bitvalue.sdk.collab.utils.ToastUtil;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMergerElem;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.meeting.model.MeetingEndEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/***
 * 聊天界面
 */
public class ChatFragment extends BaseFragment<ChatPersenter> implements Chatcontract.ChatIvew {

    @BindView(R.id.chat_layout)
    ChatLayout mChatLayout;  // 从布局文件中获取聊天面板组件
    private TitleBarLayout mTitleBar;
    private ChatInfo mChatInfo;

    private List<MessageInfo> mForwardSelectMsgInfos = null;
    private int mForwardMode;

    private static final String TAG = ChatFragment.class.getSimpleName();
    private HomeActivity homeActivity;
    private  ReportStatusBean reportStatusBean = new ReportStatusBean();
    private String planId;


    /**
     * init View and control
     * @param view
     */
    @Override
    public void initView(View view) {
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        planId = bundle.getString(Constants.PLAN_ID);
        mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
        homeActivity = (HomeActivity) getActivity();

        //单聊组件的默认UI和交互初始化
        mChatLayout.initDefault();

        /*
         * 需要聊天的基本信息
         */
        mChatLayout.setChatInfo(mChatInfo);

        //获取单聊面板的标题栏
        mTitleBar = mChatLayout.getTitleBar();
        mTitleBar.getLeftGroup().setVisibility(GONE);//bug 381，隐藏返回键
        mTitleBar.getRightIcon().setVisibility(GONE);//沒有好友详情页面，隐藏

        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPress();
            }
        });
        if (mChatInfo.getType() == V2TIMConversation.V2TIM_C2C) {
            mTitleBar.setOnRightClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//右侧头像原本查看好友详情的先屏蔽
//                    Intent intent = new Intent(AppApplication.instance(), FriendProfileActivity.class);
//                    Intent intent = new Intent(AppApplication.instance(), LoginHealthActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, mChatInfo);
//                    AppApplication.instance().startActivity(intent);
                }
            });
        }
        mChatLayout.setForwardSelectActivityListener(new AbsChatLayout.onForwardSelectActivityListener() {
            @Override
            public void onStartForwardSelectActivity(int mode, List<MessageInfo> msgIds) {
                mForwardMode = mode;
                mForwardSelectMsgInfos = msgIds;

                Intent intent = new Intent(Application.instance(), ForwardSelectActivity.class);
                intent.putExtra(ForwardSelectActivity.FORWARD_MODE, mode);
                startActivityForResult(intent, TUIKitConstants.FORWARD_SELECT_ACTIVTY_CODE);
            }
        });

        mChatLayout.getMessageLayout().setOnItemClickListener(new MessageLayout.OnItemLongClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //因为adapter中第一条为加载条目，位置需减1
                mChatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
                if (null == messageInfo) {
                    return;
                }

                V2TIMMergerElem mergerElem = messageInfo.getTimMessage().getMergerElem();
//                if (mergerElem != null) {
////                    Intent intent = new Intent(AppApplication.instance(), ForwardChatActivity.class);
//                    Intent intent = new Intent(AppApplication.instance(), LoginHealthActivity.class);//TODO
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(TUIKitConstants.FORWARD_MERGE_MESSAGE_KEY, messageInfo);
//                    intent.putExtras(bundle);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    AppApplication.instance().startActivity(intent);
//                } else {
//                    ChatInfo info = new ChatInfo();
//                    info.setId(messageInfo.getFromUser());
////                    Intent intent = new Intent(AppApplication.instance(), FriendProfileActivity.class);
//                    Intent intent = new Intent(AppApplication.instance(), LoginHealthActivity.class);//TODO
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, info);
//                    AppApplication.instance().startActivity(intent);
//                }
            }
        });

        mChatLayout.getInputLayout().setStartActivityListener(new InputLayout.OnStartActivityListener() {
            @Override
            public void onStartGroupMemberSelectActivity() {
                Intent intent = new Intent(Application.instance(), StartGroupMemberSelectActivity.class);
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.setId(mChatInfo.getId());
                groupInfo.setChatName(mChatInfo.getChatName());
                intent.putExtra(TUIKitConstants.Group.GROUP_INFO, groupInfo);
                startActivityForResult(intent, 1);
            }
        });

        if (mChatInfo.noInput) {
            mChatLayout.getInputLayout().setVisibility(GONE);
        }

        mChatLayout.getInputLayout().setChatType(mChatInfo.chatType);

        if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
            V2TIMManager.getConversationManager().getConversation(mChatInfo.getId(), new V2TIMValueCallback<V2TIMConversation>() {
                @Override
                public void onError(int code, String desc) {
                    Log.e(TAG, "getConversation error:" + code + ", desc:" + desc);
                }

                @Override
                public void onSuccess(V2TIMConversation v2TIMConversation) {
                    if (v2TIMConversation == null) {
                        return;
                    }
                    mChatInfo.setAtInfoList(v2TIMConversation.getGroupAtInfoList());

                    final V2TIMMessage lastMessage = v2TIMConversation.getLastMessage();

                    updateAtInfoLayout();
                    mChatLayout.getAtInfoLayout().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final List<V2TIMGroupAtInfo> atInfoList = mChatInfo.getAtInfoList();
                            if (atInfoList == null || atInfoList.isEmpty()) {
                                mChatLayout.getAtInfoLayout().setVisibility(GONE);
                                return;
                            } else {
                                mChatLayout.getChatManager().getAtInfoChatMessages(atInfoList.get(atInfoList.size() - 1).getSeq(), lastMessage, new IUIKitCallBack() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        mChatLayout.getMessageLayout().scrollToPosition((int) atInfoList.get(atInfoList.size() - 1).getSeq());
                                        LinearLayoutManager mLayoutManager = (LinearLayoutManager) mChatLayout.getMessageLayout().getLayoutManager();
                                        mLayoutManager.scrollToPositionWithOffset((int) atInfoList.get(atInfoList.size() - 1).getSeq(), 0);

                                        atInfoList.remove(atInfoList.size() - 1);
                                        mChatInfo.setAtInfoList(atInfoList);

                                        updateAtInfoLayout();
                                    }

                                    @Override
                                    public void onError(String module, int errCode, String errMsg) {
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }

        // TODO 通过api设置ChatLayout各种属性的样例
        ChatLayoutHelper helper = new ChatLayoutHelper(getActivity());
        helper.setGroupId(mChatInfo.getId());
        helper.customizeChatLayout(mChatLayout);

        //新增的自定义控件点击回调
        mChatLayout.setOnCustomClickListener(new InputLayout.OnCustomClickListener() {

            //健康计划
            @Override
            public void onHealthPlanClick() {
                NewMsgData msgData = new NewMsgData();
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.userId);//mChatInfo.getId()是健康管理群组聊天的groupId，userId才是每个页面需要的传参
                msgData.id = planId + "";//这里id设置为 planId
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN_DETAIL, msgData);  //切换至健康计划界面
            }

            //健康评估
            @Override
            public void onHealthAnalyseClick() {
                NewMsgData msgData = new NewMsgData();
                msgData.msgType = Constants.MSG_SINGLE;
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.userId);
                msgData.id = planId + "";//这里id设置为 planId
                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_ANALYSE, msgData); //切换至健康评估界面
            }

            //健康消息
            @Override
            public void onHealthMsgClick() {
                NewMsgData msgData = new NewMsgData();
                msgData.msgType = Constants.MSG_SINGLE;
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.userId);
                homeActivity.switchSecondFragment(Constants.FRAGMENT_SEND_MSG, msgData);//切换至健康消息界面
            }

            //健康档案
            @Override
            public void onHealthFilesClick() {
                Intent intent = new Intent(homeActivity, HealthFilesActivity.class);
                intent.putExtra(Constants.USER_ID, mChatInfo.userId);
                homeActivity.startActivity(intent);
            }


            //视频问诊
            @Override
            public void onVideoCommunicate() {
                CustomVideoCallMessage message = new CustomVideoCallMessage();
                message.title = "视频看诊";
                long currentTimeMillis = System.currentTimeMillis();
                String rooId = currentTimeMillis + "";
                message.msgDetailId = rooId.substring(rooId.length() - 7, rooId.length());
//                message.userId = mIds;
                message.content = "点击接入视频看诊";
                message.timeStamp = currentTimeMillis;
                //这个属性区分消息类型 HelloChatController中onDraw方法去绘制布局
                message.setType("CustomVideoCallMessage");
                message.userId = new ArrayList<>();
                message.userId.add(mChatInfo.getId());//传入userid
                message.setDescription("视频看诊");
                message.id = planId + "";//这里id设置为视频看诊的预约id
                MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(message), message.description, null);
                mChatLayout.sendMessage(info, false);
            }

            //书写病历
            @Override
            public void onWriteConsultConclusion() {
                NewMsgData msgData = new NewMsgData();
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.getId());
                msgData.id = planId + "";
                homeActivity.switchSecondFragment(Constants.FRAGMENT_WRITE_HEALTH, msgData);
            }

            //    结束就诊
            @Override
            public void onEndVideoConsult() {
                // TODO: 2021/10/27 弹出对话框
                DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定结束看诊吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                    @Override
                    public void onPositive() {
                        updateStatus("4");
                    }

                    @Override
                    public void onNegative() {

                    }
                });

            }
        });
    }



    //结束看诊接口请求
    private void  updateStatus(String statsus){
        reportStatusBean.id = String.valueOf(planId);
        reportStatusBean.attendanceStatus = statsus;
        mPresenter.updateAttendanceStatus(reportStatusBean);
    }








    //回退界面
    private void backPress() {
        if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            homeActivity.getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * 处理订阅消息
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CustomMessage message) {//不用区分类型，全部直接转换成json发送消息出去
        MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(message), message.description, null);
        mChatLayout.sendMessage(info, false);
    }


    /**
     * EventBus 消息订阅处理事件
     * @param meetingEndEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MeetingEndEvent meetingEndEvent) {//不用区分类型，全部直接转换成json发送消息出去
        Log.d(TAG, "onEvent: video call ended.");
       updateStatus("3");

    }


    @Override
    public void initData() {

    }

    @Override
    protected ChatPersenter createPresenter() {
        return new ChatPersenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.chat_fragment;
    }

    private void updateAtInfoLayout() {
        int atInfoType = getAtInfoType(mChatInfo.getAtInfoList());
        switch (atInfoType) {
            case V2TIMGroupAtInfo.TIM_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(Application.instance().getString(R.string.ui_at_me));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(Application.instance().getString(R.string.ui_at_all));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(Application.instance().getString(R.string.ui_at_all_me));
                break;
            default:
                mChatLayout.getAtInfoLayout().setVisibility(GONE);
                break;

        }
    }

    private int getAtInfoType(List<V2TIMGroupAtInfo> atInfoList) {
        int atInfoType = 0;
        boolean atMe = false;
        boolean atAll = false;

        if (atInfoList == null || atInfoList.isEmpty()) {
            return V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        for (V2TIMGroupAtInfo atInfo : atInfoList) {
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ME) {
                atMe = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL) {
                atAll = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME) {
                atMe = true;
                atAll = true;
                continue;
            }
        }

        if (atAll && atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME;
        } else if (atAll) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL;
        } else if (atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ME;
        } else {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        return atInfoType;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 3) {
            String result_id = data.getStringExtra(TUIKitConstants.Selection.USER_ID_SELECT);
            String result_name = data.getStringExtra(TUIKitConstants.Selection.USER_NAMECARD_SELECT);
            mChatLayout.getInputLayout().updateInputText(result_name, result_id);
        } else if (requestCode == TUIKitConstants.FORWARD_SELECT_ACTIVTY_CODE && resultCode == TUIKitConstants.FORWARD_SELECT_ACTIVTY_CODE) {
            if (data != null) {
                if (mForwardSelectMsgInfos == null || mForwardSelectMsgInfos.isEmpty()) {
                    return;
                }

                ArrayList<ConversationBean> conversationBeans = data.getParcelableArrayListExtra(TUIKitConstants.FORWARD_SELECT_CONVERSATION_KEY);
                if (conversationBeans == null || conversationBeans.isEmpty()) {
                    return;
                }

                for (int i = 0; i < conversationBeans.size(); i++) {//遍历发送对象会话
                    boolean isGroup = conversationBeans.get(i).getIsGroup() == 1;
                    String id = conversationBeans.get(i).getConversationID();
                    String title = "";
                    if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
                        title = mChatInfo.getId() + getString(R.string.forward_chats);
                    } else {
                        title = V2TIMManager.getInstance().getLoginUser() + getString(R.string.and_text) + mChatInfo.getId() + getString(R.string.forward_chats_c2c);
                    }

                    boolean selfConversation = false;
                    if (id != null && id.equals(mChatInfo.getId())) {
                        selfConversation = true;
                    }

                    ChatManagerKit chatManagerKit = mChatLayout.getChatManager();
                    chatManagerKit.forwardMessage(mForwardSelectMsgInfos, isGroup, id, title, mForwardMode, selfConversation, false, new IUIKitCallBack() {
                        @Override
                        public void onSuccess(Object data) {

                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {

                        }
                    });
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mChatLayout != null && mChatLayout.getChatManager() != null) {
            mChatLayout.getChatManager().setChatFragmentShow(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mChatLayout != null) {
            if (mChatLayout.getInputLayout() != null) {
                mChatLayout.getInputLayout().setDraft();
            }

            if (mChatLayout.getChatManager() != null) {
                mChatLayout.getChatManager().setChatFragmentShow(false);
            }
        }
        AudioPlayer.getInstance().stopPlay();
    }

    @Override
    public void onDestroy() {
        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * 更新就诊状态 成功
     *
     * @param
     */
    @Override
    public void updateAttendanceStausSuccess() {
        getActivity().runOnUiThread(() -> {
            EventBus.getDefault().post(new VideoRefreshObj());
            if (reportStatusBean.attendanceStatus.equals("4")) {
                ToastUtil.toastShortMessage("已结束看诊");
                backPress();
            }
        });
    }


    /**
     * 更新就诊状态失败
     *
     * @param
     */
    @Override
    public void updateFail(String FailMessage) {

    }


    public static class NewMsgData implements Serializable {
        public ArrayList<String> userIds;
        public String msgType;
        public String id;
        public String planId;
        public String groupID;
        public String appointmentId;

        public SaveCaseApi saveCaseApi;
    }

}