package com.bitvalue.sdk.collab.modules.conversation.interfaces;


import androidx.recyclerview.widget.RecyclerView;

import com.bitvalue.sdk.collab.modules.conversation.base.ConversationInfo;

/**
 * ConversationLayout 的适配器，用户可自定义实现
 */

public abstract class IConversationAdapter extends RecyclerView.Adapter {
    /**
     * 设置适配器的数据源，该接口一般由ConversationContainer自动调用
     *
     * @param provider
     */
    public abstract void setDataProvider(IConversationProvider provider);

    /**
     * 获取适配器的条目数据，返回的是ConversationInfo对象或其子对象
     *
     * @param position
     * @return ConversationInfo
     */
    public abstract ConversationInfo getItem(int position);

}
