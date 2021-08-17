package com.bitvalue.healthmanage.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.TUIKitImpl;
import com.bitvalue.sdk.collab.base.IBaseAction;
import com.bitvalue.sdk.collab.base.IBaseInfo;
import com.bitvalue.sdk.collab.base.IBaseViewHolder;
import com.bitvalue.sdk.collab.base.TUIChatControllerListener;
import com.bitvalue.sdk.collab.base.TUIConversationControllerListener;
import com.bitvalue.sdk.collab.helper.CustomAnalyseMessage;
import com.bitvalue.sdk.collab.helper.CustomHealthMessage;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.MessageBaseHolder;
import com.bitvalue.sdk.collab.modules.chat.layout.message.holder.MessageCustomHolder;
import com.bitvalue.sdk.collab.modules.message.MessageInfo;
import com.bitvalue.sdk.collab.modules.message.MessageInfoUtil;
import com.bitvalue.sdk.collab.utils.TUIKitLog;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMCustomElem;
import com.tencent.imsdk.v2.V2TIMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HelloChatController implements TUIChatControllerListener {
    private static final String TAG = HelloChatController.class.getSimpleName();

    @Override
    public List<IBaseAction> onRegisterMoreActions() {
        return null;
    }

    @Override
    public IBaseInfo createCommonInfoFromTimMessage(V2TIMMessage timMessage) {
        if (timMessage.getElemType() == V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {
            V2TIMCustomElem customElem = timMessage.getCustomElem();
            if (customElem == null || customElem.getData() == null) {
                return null;
            }

//            if (null != customElem.getExtension()) {
//                switch (new String(customElem.getExtension())) {
//                    case "CustomHealthMessage":
//                        CustomHealthMessage healthMessage = null;
//                        try {
//                            healthMessage = new Gson().fromJson(new String(customElem.getData()), CustomHealthMessage.class);
//                        } catch (Exception e) {
////                Logger.w(TAG, "invalid json: " + new String(customElem.getData()) + " " + e.getMessage());
//                            TUIKitLog.e(TAG, "invalid json: " + new String(customElem.getData()) + " " + e.getMessage());
//                        }
//                        break;
//                }
//            }

//            String dataJson = new String(customElem.getData());
//            try {
//                JSONObject jsonObject = new JSONObject(dataJson);
//                String type = jsonObject.optString("type");
//                switch (type) {
//                    case "CustomHealthMessage":
//                        CustomHealthMessage healthMessage = new Gson().fromJson(jsonObject.optString("data"), CustomHealthMessage.class);
//                        if (healthMessage != null){
//                            MessageInfo messageInfo = new HelloMessageInfo();
//                            messageInfo.setMsgType(HelloMessageInfo.MSG_TYPE_HELLO);
//                            MessageInfoUtil.setMessageInfoCommonAttributes(messageInfo, timMessage);
//                            Context context = TUIKit.getAppContext();
//                            if (context != null) {
//                                messageInfo.setExtra(context.getString(R.string.custom_msg));
//                            }
//                            return messageInfo;
//                        }
//                        break;
//                }
//
//            } catch (JSONException e) {
//            }

            String dataFather = new String(customElem.getData());
            if (null != dataFather && !dataFather.isEmpty()) {
                MessageInfo messageInfo = new HelloMessageInfo();
                messageInfo.setMsgType(HelloMessageInfo.MSG_TYPE_HELLO);
                MessageInfoUtil.setMessageInfoCommonAttributes(messageInfo, timMessage);
                Context context = TUIKit.getAppContext();
                if (context != null) {
                    messageInfo.setExtra(context.getString(R.string.custom_msg));
                }
                return messageInfo;
            }


            //demo原本的代码，上面三块都是加的
//            CustomHelloMessage helloMessage = null;
//            try {
//                helloMessage = new Gson().fromJson(new String(customElem.getData()), CustomHelloMessage.class);
//            } catch (Exception e) {
////                Logger.w(TAG, "invalid json: " + new String(customElem.getData()) + " " + e.getMessage());
//                TUIKitLog.e(TAG, "invalid json: " + new String(customElem.getData()) + " " + e.getMessage());
//            }
//            if (helloMessage != null && TextUtils.equals(helloMessage.businessID, TUIKitConstants.BUSINESS_ID_CUSTOM_HELLO)) {
//                MessageInfo messageInfo = new HelloMessageInfo();
//                messageInfo.setMsgType(HelloMessageInfo.MSG_TYPE_HELLO);
//                MessageInfoUtil.setMessageInfoCommonAttributes(messageInfo, timMessage);
//                Context context = TUIKit.getAppContext();
//                if (context != null) {
//                    messageInfo.setExtra(context.getString(R.string.custom_msg));
//                }
//                return messageInfo;
//            }
        }
        return null;
    }

    @Override
    public IBaseViewHolder createCommonViewHolder(ViewGroup parent, int viewType) {
        if (viewType != HelloMessageInfo.MSG_TYPE_HELLO) {
            return null;
        }
        if (parent == null) {
            return null;
        }
        LayoutInflater inflater = LayoutInflater.from(TUIKit.getAppContext());
        View contentView = inflater.inflate(R.layout.message_adapter_item_content, parent, false);
        return new HelloViewHolder(contentView);
    }

    @Override
    public boolean bindCommonViewHolder(IBaseViewHolder baseViewHolder, IBaseInfo baseInfo, int position) {
        if (baseViewHolder instanceof ICustomMessageViewGroup && baseInfo instanceof HelloMessageInfo) {
            ICustomMessageViewGroup customHolder = (ICustomMessageViewGroup) baseViewHolder;
            MessageInfo msg = (MessageInfo) baseInfo;
            new CustomMessageDraw().onDraw(customHolder, msg, position);
            return true;
        }
        return false;
    }

    static class HelloMessageInfo extends MessageInfo {
        public static final int MSG_TYPE_HELLO = 100002;
    }

    static class HelloViewHolder extends MessageCustomHolder {

        public HelloViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class HelloConversationController implements TUIConversationControllerListener {

        @Override
        public CharSequence getConversationDisplayString(IBaseInfo baseInfo) {
            if (baseInfo instanceof HelloMessageInfo) {
                return TUIKitImpl.getAppContext().getString(R.string.welcome_tip);
            }
            return null;
        }
    }

    public static class CustomMessageDraw implements IOnCustomMessageDrawListener {

        /**
         * 自定义消息渲染时，会调用该方法，本方法实现了自定义消息的创建，以及交互逻辑
         *
         * @param parent 自定义消息显示的父View，需要把创建的自定义消息view添加到parent里
         * @param info   消息的具体信息
         */
        @Override
        public void onDraw(ICustomMessageViewGroup parent, MessageInfo info, int position) {
            // 获取到自定义消息的json数据
            if (info.getTimMessage().getElemType() != V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {
                return;
            }
            V2TIMCustomElem elem = info.getTimMessage().getCustomElem();

            String dataJson = new String(elem.getData());
            try {
                JSONObject jsonObject = new JSONObject(dataJson);
                String type = jsonObject.optString("type");
                switch (type) {
                    case "CustomHealthMessage":
                        CustomHealthMessage healthMessage = new Gson().fromJson(dataJson, CustomHealthMessage.class);
                        if (healthMessage != null) {
                            if (parent instanceof MessageBaseHolder) {
                                //TODO  CustomHelloTIMUIController 改成 CustomHealthMessageController
//                                CustomHelloTIMUIController.onDraw(parent, healthMessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                                CustomHealthMessageController.onDraw(parent, healthMessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                            }
                        }
                        break;
                    case "CustomAnalyseMessage":
                        CustomAnalyseMessage customAnalyseMessage = new Gson().fromJson(dataJson, CustomAnalyseMessage.class);
                        if (customAnalyseMessage != null) {
                            if (parent instanceof MessageBaseHolder) {
                                //TODO  CustomHelloTIMUIController 改成 CustomHealthMessageController
//                                CustomHelloTIMUIController.onDraw(parent, healthMessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                                CustomAnalyseMessageController.onDraw(parent, customAnalyseMessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                            }
                        }
                        break;
                }

            } catch (JSONException e) {
                TUIKitLog.w(TAG, "invalid json: " + new String(elem.getData()) + " " + e.getMessage());
            }


//            // 自定义的json数据，需要解析成bean实例
//            CustomHelloMessage data = null;
//            try {
//                data = new Gson().fromJson(new String(elem.getData()), CustomHelloMessage.class);
//            } catch (Exception e) {
//                TUIKitLog.w(TAG, "invalid json: " + new String(elem.getData()) + " " + e.getMessage());
//            }
//            if (data == null) {
//                TUIKitLog.e(TAG, "No Custom Data: " + new String(elem.getData()));
//            } else if (data.version == TUIKitConstants.JSON_VERSION_1 || (data.version == TUIKitConstants.JSON_VERSION_4 && data.businessID.equals(TUIKitConstants.BUSINESS_ID_CUSTOM_HELLO))) {
//                if (parent instanceof MessageBaseHolder) {
//                    CustomHelloTIMUIController.onDraw(parent, data, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
//                }
//            } else {
//                TUIKitLog.w(TAG, "unsupported version: " + data);
//            }
        }
    }

}

