package com.bitvalue.health.ui.fragment.chat;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.bitvalue.health.util.Constants.CHAT_TYPE;
import static com.bitvalue.health.util.Constants.FRAGMENT_ADD_PAPER;
import static com.bitvalue.health.util.Constants.FRAGMENT_ADD_QUESTION;
import static com.bitvalue.health.util.Constants.FRAGMENT_QUICKREPLY;
import static com.bitvalue.health.util.Constants.FRAGMENT_SEND_REMIND;
import static com.bitvalue.health.util.Constants.PLAN_ID;
import static com.bitvalue.health.util.Constants.SINGLECHAT;
import static com.bitvalue.health.util.Constants.USER_IDS;
import static com.tencent.imsdk.v2.V2TIMConversation.V2TIM_C2C;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.eventbusbean.MainRefreshObj;
import com.bitvalue.health.api.eventbusbean.NotifycationAlardyObj;
import com.bitvalue.health.api.eventbusbean.RefreshDataViewObj;
import com.bitvalue.health.api.requestbean.SaveRightsUseBean;
import com.bitvalue.health.api.responsebean.LoginBean;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.api.responsebean.SaveCaseApi;
import com.bitvalue.health.api.responsebean.TaskDeatailBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.contract.healthmanagercontract.InterestsUseApplyByDocContract;
import com.bitvalue.health.presenter.healthmanager.InterestsUseApplyByDocPresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.health.util.DataUtil;
import com.bitvalue.health.util.SharedPreManager;
import com.bitvalue.health.util.TimeUtils;
import com.bitvalue.health.util.chatUtil.CustomVideoCallMessageController;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.bitvalue.sdk.collab.component.TitleBarLayout;
import com.bitvalue.sdk.collab.helper.ChatLayoutHelper;
import com.bitvalue.sdk.collab.helper.CustomMessage;
import com.bitvalue.sdk.collab.helper.CustomVideoCallMessage;
import com.bitvalue.sdk.collab.modules.chat.base.ChatInfo;
import com.bitvalue.sdk.collab.modules.chat.layout.input.ChatLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.forward.ForwardSelectActivity;
import com.bitvalue.sdk.collab.modules.group.info.GroupInfo;
import com.bitvalue.sdk.collab.modules.group.info.StartGroupMemberSelectActivity;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.bitvalue.sdk.collab.modules.message.MessageInfoUtil;
import com.bitvalue.sdk.collab.utils.TUIKitConstants;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;

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
public class ChatFragment extends BaseFragment<InterestsUseApplyByDocPresenter> implements InterestsUseApplyByDocContract.View {

    @BindView(R.id.chat_layout)
    ChatLayout mChatLayout;  // 从布局文件中获取聊天面板组件
    private HomeActivity homeActivity;
    private TitleBarLayout mTitleBar;
    private ChatInfo mChatInfo;
    private NewLeaveBean.RowsDTO patientinfo;
    private List<MessageInfo> mForwardSelectMsgInfos = null;
    private int mForwardMode;

    private static final String TAG = ChatFragment.class.getSimpleName();
    private String planId;
    private ArrayList<String> userIDList = new ArrayList<>();
    private String userID;
    private TaskDeatailBean taskDeatailBean;
    private boolean isVideoRights; //是否是视频问诊

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }

    @Override
    protected InterestsUseApplyByDocPresenter createPresenter() {
        return new InterestsUseApplyByDocPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.chat_fragment;
    }


    @Override
    public void initView(View view) {
        Bundle bundle = getArguments();
        mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
        patientinfo = (NewLeaveBean.RowsDTO) bundle.getSerializable(Constants.USERINFO);
        if (mChatInfo == null || patientinfo == null) {
            return;
        }
        taskDeatailBean = patientinfo.taskDeatailBean;

        planId = patientinfo.getUserId();
        if (mChatInfo.getType() == V2TIM_C2C) {
            if (mChatInfo.isShowShortCut) {
                userIDList.clear();
                userIDList.add(mChatInfo.userId);
            }
            userID = mChatInfo.userId;
        } else {
            userIDList = mChatInfo.userIdList;
        }

        EventBus.getDefault().register(this);
        //单聊组件的默认UI和交互初始化
        mChatLayout.initDefault();

        /*
         * 需要聊天的基本信息
         */
        mChatLayout.setChatInfo(mChatInfo);
        mChatInfo.setChatName(mChatInfo.getChatName());

        //获取单聊面板的标题栏
        mTitleBar = mChatLayout.getTitleBar();
        if (patientinfo.isConsultation) {
            mTitleBar.getEndVisitText().setVisibility(VISIBLE);
        }


        mTitleBar.getLeftGroup().setVisibility(VISIBLE);//bug 381，隐藏返回键
        mTitleBar.getRightIcon().setVisibility(GONE);//沒有好友详情页面，隐藏
        mChatLayout.getInputLayout().ll_shortCutlayout.setVisibility(mChatInfo.isShowShortCut ? VISIBLE : GONE); //底部快捷回复布局
//        mChatLayout.getInputLayout().setUserIDs(userIDList);
//        mChatLayout.getInputLayout().setIsMass(mChatInfo.getChatName().equals("群发消息") ? true : false);

        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        mTitleBar.setOnLeftClickListener(v -> {
            if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                homeActivity.getSupportFragmentManager().popBackStack();
            }
        });
        mChatLayout.setForwardSelectActivityListener((mode, msgIds) -> {
            mForwardMode = mode;
            mForwardSelectMsgInfos = msgIds;

            Intent intent = new Intent(Application.instance(), ForwardSelectActivity.class);
            intent.putExtra(ForwardSelectActivity.FORWARD_MODE, mode);
            startActivityForResult(intent, TUIKitConstants.FORWARD_SELECT_ACTIVTY_CODE);
        });

        mChatLayout.getMessageLayout().setOnItemClickListener(new MessageLayout.OnItemLongClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //因为adapter中第一条为加载条目，位置需减1
                mChatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            /**
             * 点击左边头像 进入详情界面
             * @param view
             * @param position
             * @param messageInfo
             * @param isLeftIconClick
             */
            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo, boolean isLeftIconClick) {
                if (isLeftIconClick) {
                    homeActivity.switchSecondFragment(Constants.FRAGMENT_DETAIL, Integer.valueOf(patientinfo.getUserId()));
                }
            }
        });


        //结束问诊
        mTitleBar.getEndVisitText().setOnClickListener(v -> {

            DataUtil.showNormalDialog(homeActivity, "温馨提示", "确定结束问诊吗？", "确定", "取消", new DataUtil.OnNormalDialogClicker() {
                @Override
                public void onPositive() {
                    LoginBean loginBean = SharedPreManager.getObject(Constants.KYE_USER_BEAN, LoginBean.class, homeActivity);
                    SaveRightsUseBean saveRightsUseBean = new SaveRightsUseBean();
                    saveRightsUseBean.deptName = loginBean.getUser().user.departmentName;
                    saveRightsUseBean.execDept = String.valueOf(loginBean.getUser().user.departmentId); //这里传科室代码
                    saveRightsUseBean.execFlag = 1;
                    saveRightsUseBean.execUser = String.valueOf(loginBean.getUser().user.userId);
                    saveRightsUseBean.statusDescribe = "结束问诊";
                    saveRightsUseBean.execTime = TimeUtils.getTime_tosecond(taskDeatailBean.getExecTime());
                    saveRightsUseBean.id = taskDeatailBean.getTaskDetail().getId();
                    saveRightsUseBean.rightsId = taskDeatailBean.getTaskDetail().getRightsId();
                    saveRightsUseBean.rightsName = taskDeatailBean.getTaskDetail().getRightsName();
                    saveRightsUseBean.rightsType = taskDeatailBean.getTaskDetail().getRightsType();
                    saveRightsUseBean.tradeId = taskDeatailBean.getTaskDetail().getTradeId();
                    saveRightsUseBean.userId = taskDeatailBean.getTaskDetail().getUserId();
                    saveRightsUseBean.taskId = String.valueOf(taskDeatailBean.getId());
                    mPresenter.saveRightsUseRecord(saveRightsUseBean);   //请求接口 结束问诊
                }

                @Override
                public void onNegative() {

                }
            });




        });


        mChatLayout.getInputLayout().tv_sendremind.setOnClickListener(v -> {
            homeActivity.switchSecondFragment(FRAGMENT_SEND_REMIND, "");
        });

        mChatLayout.getInputLayout().tv_sendquestion.setOnClickListener(v -> {

            homeActivity.switchSecondFragment(FRAGMENT_ADD_QUESTION, "");

        });

        mChatLayout.getInputLayout().tv_sendarticle.setOnClickListener(v -> {
            homeActivity.switchSecondFragment(FRAGMENT_ADD_PAPER, "");
        });
        mChatLayout.getInputLayout().tv_sendshortcut.setOnClickListener(v -> {
            homeActivity.switchSecondFragment(FRAGMENT_QUICKREPLY, "");
        });


        mChatLayout.getInputLayout().setStartActivityListener(() -> {
            Intent intent = new Intent(Application.instance(), StartGroupMemberSelectActivity.class);
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setId(mChatInfo.getId());
            groupInfo.setChatName(mChatInfo.getChatName());
            intent.putExtra(TUIKitConstants.Group.GROUP_INFO, groupInfo);
            startActivityForResult(intent, 1);
        });

        mChatLayout.getInputLayout().setVisibility(VISIBLE);
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

//        if (mChatInfo.getType() == V2TIM_C2C) {
//            mTitleBar.setOnRightClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {//右侧头像原本查看好友详情的先屏蔽
////                    Intent intent = new Intent(AppApplication.instance(), FriendProfileActivity.class);
////                    Intent intent = new Intent(AppApplication.instance(), LoginHealthActivity.class);
////                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, mChatInfo);
////                    AppApplication.instance().startActivity(intent);
//                }
//            });
//        }


        // TODO 通过api设置ChatLayout各种属性的样例
        ChatLayoutHelper helper = new ChatLayoutHelper(homeActivity);
        helper.setGroupId(mChatInfo.getId());
        helper.customizeChatLayout(mChatLayout);

        //新增的自定义控件点击回调
        mChatLayout.setOnCustomClickListener(new InputLayout.OnCustomClickListener() {
            //
//            //健康计划
            @Override
            public void onHealthPlanClick() {
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.userId);//mChatInfo.getId()是健康管理群组聊天的groupId，userId才是每个页面需要的传参
                msgData.id = planId + "";//这里id设置为 planId
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_PLAN_DETAIL, msgData);  //切换至健康计划界面
            }

            //健康评估
            @Override
            public void onHealthAnalyseClick() {
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.msgType = Constants.MSG_SINGLE;
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.userId);
                msgData.id = planId + "";//这里id设置为 planId
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_HEALTH_ANALYSE, msgData); //切换至健康评估界面
            }

            //健康消息
            @Override
            public void onHealthMsgClick() {
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.msgType = Constants.MSG_SINGLE;
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.userId);
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_SEND_MSG, msgData);//切换至健康消息界面
            }

            //健康档案
            @Override
            public void onHealthFilesClick() {
//                Intent intent = new Intent(homeActivity, HealthFilesActivity.class);
//                intent.putExtra(Constants.USER_ID, mChatInfo.userId);
//                homeActivity.startActivity(intent);
            }

            //
//
//            //视频问诊
            @Override
            public void onVideoCommunicate() {
                CustomVideoCallMessage message = new CustomVideoCallMessage();
                message.title = getString(R.string.video_visit);
                long currentTimeMillis = System.currentTimeMillis();
                String rooId = currentTimeMillis + "";
                message.msgDetailId = rooId.substring(rooId.length() - 7, rooId.length());
//                message.userId = mIds;
                message.content = getString(R.string.click_to_access_the_video);
                message.timeStamp = currentTimeMillis;
                //这个属性区分消息类型 HelloChatController中onDraw方法去绘制布局
                message.setType("CustomVideoCallMessage");
                message.userId = new ArrayList<>();
                message.userId.add(mChatInfo.getId());//传入userid
                message.setDescription(getString(R.string.video_visit));
                message.id = planId + "";//这里id设置为视频看诊的预约id
                MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(message), message.description, null);
                mChatLayout.sendMessage(info, false);
                CustomVideoCallMessageController.getPatientAppointmentById(message, true);


            }

            //            //书写病历
//            @Override
            public void onWriteConsultConclusion() {
                ChatFragment.NewMsgData msgData = new ChatFragment.NewMsgData();
                msgData.userIds = new ArrayList<>();
                msgData.userIds.add(mChatInfo.getId());
                msgData.id = planId + "";
//                homeActivity.switchSecondFragment(Constants.FRAGMENT_WRITE_HEALTH, msgData);
            }

            //    结束就诊
            @Override
            public void onEndVideoConsult() {
                // TODO: 2021/10/27 弹出对话框

            }
        });

        mChatLayout.getInputLayout().setGoneInputMore(patientinfo.rightsName.equals("视频咨询"));  //如果是图文咨询的 聊天界面点击更多不显示视频问诊控件
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

    @Override
    public void initData() {

    }


    @Override
    public void onDestroy() {
        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 处理订阅消息
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventHandler(CustomMessage message) {//不用区分类型，全部直接转换成json发送消息出去
        Log.e(TAG, "接收消息");
        MessageInfo info = MessageInfoUtil.buildCustomMessage(new Gson().toJson(message), message.description, null);
        mChatLayout.sendMessage(info, false);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGetTaskn(MainRefreshObj bean) {
        Log.e(TAG, "接收待办实体---" + bean);
//        taskDeatailBean = bean;
    }


    /**
     * 处理订阅消息
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventHandler(MessageInfo message) {//不用区分类型，全部直接转换成json发送消息出去
        Log.e(TAG, "接收消息111");
        mChatLayout.sendMessage(message, false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 4 && resultCode == 3) {
//            String result_id = data.getStringExtra("result");
//            mChatLayout.getInputLayout().mTextInput.setText(result_id);
//        }
    }


    /**
     * 结束问诊成功回调
     *
     * @param bean
     */
    @Override
    public void saveRightsUseRecordSuccess(SaveRightsUseBean bean) {
        homeActivity.runOnUiThread(() -> {
            EventBus.getDefault().post(new NotifycationAlardyObj()); //通知更新最新数据
            EventBus.getDefault().post(new RefreshDataViewObj());  //通知医生端界面 更新 已结束 字样和 隐藏开始问诊控件
            if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                homeActivity.getSupportFragmentManager().popBackStack();
            }
        });
    }

    /**
     * 结束问诊失败回调
     *
     * @param
     */

    @Override
    public void saveRightsUseRecordFail(String failMessage) {
        homeActivity.runOnUiThread(() -> {
            Log.e(TAG, "saveRightsUseRecordFail: " + failMessage);
            ToastUtils.show(failMessage);
        });
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
