package com.bitvalue.sdk.collab.modules.conversation.interfaces;

import com.bitvalue.sdk.collab.base.ILayout;
import com.bitvalue.sdk.collab.base.IUIKitCallBack;
import com.bitvalue.sdk.collab.modules.conversation.ConversationListLayout;
import com.bitvalue.sdk.collab.modules.conversation.base.ConversationInfo;

/**
 * 会话列表窗口 {@link ConversationLayout} 由标题区 {@link TitleBarLayout} 与列表区 {@link ConversationListLayout}
 * <br>组成，每一部分都提供了 UI 样式以及事件注册的接口可供修改。
 */
public interface IConversationLayout extends ILayout {

    /**
     * 获取会话列表 List
     *
     * @return
     */
    ConversationListLayout getConversationList();

    /**
     * 置顶会话
     *
     * @param conversation 会话内容
     */
    void setConversationTop(ConversationInfo conversation, IUIKitCallBack callBack);

    /**
     * 删除会话
     *
     * @param position     该item在列表的索引
     * @param conversation 会话内容
     */
    void deleteConversation(int position, ConversationInfo conversation);

    /**
     * 清空会话
     *
     * @param position     该item在列表的索引
     * @param conversation 会话内容
     */
    void clearConversationMessage(int position, ConversationInfo conversation);
}
