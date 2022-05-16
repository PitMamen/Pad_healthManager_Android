package com.bitvalue.sdk.collab.modules.chat.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.component.NoticeLayout;
import com.bitvalue.sdk.collab.component.TitleBarLayout;
import com.bitvalue.sdk.collab.modules.chat.interfaces.IChatLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;

public abstract class ChatLayoutUI extends LinearLayout implements IChatLayout {

    protected NoticeLayout mGroupApplyLayout;
    protected View mRecordingGroup;
    protected ImageView mRecordingIcon;
    protected TextView mRecordingTips;
    private TitleBarLayout mTitleBar;
    private MessageLayout mMessageLayout;
    private InputLayout mInputLayout;
    private NoticeLayout mNoticeLayout;
    protected ChatInfo mChatInfo;
    private TextView mChatAtInfoLayout;
    public TextView tv_numberofarticles;  //剩余条数

    private LinearLayout mForwardLayout;
    private Button mForwardButton;
    private Button mDeleteButton;
    private FrameLayout flayout_tipmessage;

    public ChatLayoutUI(Context context) {
        super(context);
        initViews();
    }

    public ChatLayoutUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ChatLayoutUI(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.chat_layout, this);

        mTitleBar = findViewById(R.id.chat_title_bar);
        tv_numberofarticles = findViewById(R.id.tv_numberofarticles);
        flayout_tipmessage = findViewById(R.id.framelayout_tipmessage);
        mMessageLayout = findViewById(R.id.chat_message_layout);
        mInputLayout = findViewById(R.id.chat_input_layout);
        mInputLayout.setChatLayout(this);
        mRecordingGroup = findViewById(R.id.voice_recording_view);
        mRecordingIcon = findViewById(R.id.recording_icon);
        mRecordingTips = findViewById(R.id.recording_tips);
        mGroupApplyLayout = findViewById(R.id.chat_group_apply_layout);
        mNoticeLayout = findViewById(R.id.chat_notice_layout);
        mChatAtInfoLayout = findViewById(R.id.chat_at_text_view);

        mForwardLayout = findViewById(R.id.forward_layout);
        mForwardButton = findViewById(R.id.forward_button);
        mDeleteButton = findViewById(R.id.delete_button);

        init();
    }


    public TextView gettv_numberofarticles() {
        return tv_numberofarticles;
    }

    protected void init() {

    }

    public LinearLayout getForwardLayout() {
        return mForwardLayout;
    }

    public Button getForwardButton() {
        return mForwardButton;
    }

    public Button getDeleteButton() {
        return mDeleteButton;
    }


    public FrameLayout getFlayout_tipmessage() {
        return flayout_tipmessage;
    }


    @Override
    public InputLayout getInputLayout() {
        return mInputLayout;
    }

    @Override
    public MessageLayout getMessageLayout() {
        return mMessageLayout;
    }

    @Override
    public NoticeLayout getNoticeLayout() {
        return mNoticeLayout;
    }

    @Override
    public ChatInfo getChatInfo() {
        return mChatInfo;
    }

    @Override
    public TextView getAtInfoLayout() {
        return mChatAtInfoLayout;
    }

    @Override
    public void setChatInfo(ChatInfo chatInfo) {
        mChatInfo = chatInfo;
        mInputLayout.setChatInfo(chatInfo);
        if (chatInfo == null) {
            return;
        }
        String chatTitle = chatInfo.getChatName();
        getTitleBar().setTitle(chatTitle, TitleBarLayout.POSITION.MIDDLE);
    }

    @Override
    public void exitChat() {

    }

    @Override
    public void initDefault() {

    }

    @Override
    public void loadMessages(int type) {

    }

    @Override
    public void sendMessage(MessageInfo msg, boolean retry) {

    }

    @Override
    public TitleBarLayout getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void setParentLayout(Object parent) {

    }
}
