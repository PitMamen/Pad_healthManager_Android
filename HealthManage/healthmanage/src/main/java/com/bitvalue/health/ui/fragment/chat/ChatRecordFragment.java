package com.bitvalue.health.ui.fragment.chat;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.bitvalue.health.Application;
import com.bitvalue.health.api.responsebean.NewLeaveBean;
import com.bitvalue.health.base.BaseFragment;
import com.bitvalue.health.base.presenter.BasePresenter;
import com.bitvalue.health.ui.activity.HomeActivity;
import com.bitvalue.health.util.Constants;
import com.bitvalue.healthmanage.R;
import com.bitvalue.sdk.collab.component.TitleBarLayout;
import com.bitvalue.sdk.collab.modules.chat.base.ChatInfo;
import com.bitvalue.sdk.collab.modules.chat.layout.input.ChatLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.forward.ForwardSelectActivity;
import com.bitvalue.sdk.collab.modules.group.info.GroupInfo;
import com.bitvalue.sdk.collab.modules.group.info.StartGroupMemberSelectActivity;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.bitvalue.sdk.collab.utils.TUIKitConstants;

import butterknife.BindView;

/**
 * @author created by bitvalue
 * @data : 04/07
 * 聊天记录界面
 */
public class ChatRecordFragment extends BaseFragment {
    public static final String TAG = ChatRecordFragment.class.getSimpleName();
    private HomeActivity homeActivity;

    @BindView(R.id.chat_record_layout)
    ChatLayout mChatLayout;  // 从布局文件中获取聊天面板组件
    private TitleBarLayout mTitleBar;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.chat_record_layout;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity = (HomeActivity) context;
    }


    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        ChatInfo chatInfo = (ChatInfo) getArguments().getSerializable(Constants.CHAT_INFO);
        if (chatInfo == null) {
            return;
        }
        mChatLayout.initDefault();
        initTitleBar();
        initChatLayout(chatInfo);
        leftIconClickLisentener(Integer.valueOf(chatInfo.getId()));
    }


    private void initTitleBar() {
        //获取单聊面板的标题栏
        mTitleBar = mChatLayout.getTitleBar();
        mTitleBar.getLeftGroup().setVisibility(VISIBLE);//bug 381，隐藏返回键
        mTitleBar.getRightIcon().setVisibility(GONE);//沒有好友详情页面，隐藏
        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        mTitleBar.setOnLeftClickListener(v -> {
            if (homeActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                homeActivity.getSupportFragmentManager().popBackStack();
            }
        });
    }


    private void initChatLayout(ChatInfo mChatInfo) {
        /*
         * 需要聊天的基本信息
         */
        mChatLayout.setChatInfo(mChatInfo);
        mChatInfo.setChatName(mChatInfo.getChatName());
        mChatLayout.getFlayout_tipmessage().setVisibility(GONE);
        mChatLayout.getInputLayout().ll_shortCutlayout.setVisibility(GONE); //底部快捷回复布局

        mChatLayout.getInputLayout().setVisibility(GONE);
        mChatLayout.getInputLayout().setChatType(mChatInfo.chatType);

    }

    /**
     * 左边头像事件点击处理
     */
    private void leftIconClickLisentener(int user_id) {
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
             * @param isLeftIconClick  点击对方的头像
             */
            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo, boolean isLeftIconClick) {
                if (isLeftIconClick) {
                    homeActivity.switchSecondFragment(Constants.FRAGMENT_DETAIL, Integer.valueOf(user_id));
                }
            }
        });
    }


}
