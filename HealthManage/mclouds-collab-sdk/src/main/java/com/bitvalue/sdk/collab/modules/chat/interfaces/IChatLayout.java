package com.bitvalue.sdk.collab.modules.chat.interfaces;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.bitvalue.sdk.collab.base.ILayout;
import com.bitvalue.sdk.collab.component.NoticeLayout;
import com.bitvalue.sdk.collab.modules.chat.base.ChatInfo;
import com.bitvalue.sdk.collab.modules.chat.layout.input.InputLayout;
import com.bitvalue.sdk.collab.modules.chat.layout.message.MessageLayout;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;

/**
 * 聊天窗口 {@link ChatLayout} 提供了消息的展示与发送等功能，界面布局从上到下分为四个部分: <br>
 * <pre>    标题区 {@link TitleBarLayout}，
 *  提醒区 {@link NoticeLayout}，
 *  消息区 {@link MessageLayout}，
 *  输入区 {@link InputLayout}，</pre>
 * 每个区域提供了多样的方法以供定制使用。
 */
public interface IChatLayout extends ILayout {
    /**
     * 获取 “您已进入中南大学湘雅二医院互联网医院平台就诊室” 提示布局信息
     */

    FrameLayout getFlayout_tipmessage();


    /**
     * 获取聊天窗口 Input 区域 Layout
     *
     * @return
     */
    InputLayout getInputLayout();

    /**
     * 获取聊天窗口 Message 区域 Layout
     *
     * @return
     */
    MessageLayout getMessageLayout();

    /**
     * 获取聊天窗口 Notice 区域 Layout
     *
     * @return
     */
    NoticeLayout getNoticeLayout();

    /**
     * 获取当前的会话信息
     */
    ChatInfo getChatInfo();

    /**
     * 设置当前的会话信息
     *
     * @param chatInfo
     */
    void setChatInfo(ChatInfo chatInfo);

    /**
     * 退出聊天，释放相关资源（一般在 activity finish 时调用）
     */
    void exitChat();

    /**
     * 初始化参数
     */
    void initDefault();

    /**
     * 加载聊天消息
     */
    void loadMessages(int type);

    /**
     * 发送消息
     *
     * @param msg   消息
     * @param retry 是否重试
     */
    void sendMessage(MessageInfo msg, boolean retry);

    TextView getAtInfoLayout();
}
