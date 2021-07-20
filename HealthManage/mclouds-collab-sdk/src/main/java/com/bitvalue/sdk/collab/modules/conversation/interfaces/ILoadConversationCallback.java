package com.bitvalue.sdk.collab.modules.conversation.interfaces;


import com.bitvalue.sdk.collab.modules.conversation.ConversationProvider;

public interface ILoadConversationCallback {
    void onSuccess(ConversationProvider provider, boolean isFinished, long nextSeq);

    void onError(String module, int errCode, String errMsg);
}
