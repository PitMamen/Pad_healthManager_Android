package com.bitvalue.health.util.chatUtil;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.TUIKit;
import com.bitvalue.sdk.collab.base.IBaseAction;
import com.bitvalue.sdk.collab.base.IBaseInfo;
import com.bitvalue.sdk.collab.base.IBaseViewHolder;
import com.bitvalue.sdk.collab.base.TUIChatControllerListener;
import com.bitvalue.sdk.collab.base.TUIConversationControllerListener;
import com.bitvalue.sdk.collab.helper.CustomHealthDataMessage;
import com.bitvalue.sdk.collab.helper.CustomHealthMessage;
import com.bitvalue.sdk.collab.helper.CustomVideoCallMessage;
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
//                return TUIKitImpl.getAppContext().getString(R.string.welcome_tip);
                return "[文件]";
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
        @RequiresApi(api = Build.VERSION_CODES.N)
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
                Log.e(TAG, "helle_onDraw: " + type);
                switch (type) {
                    //健康消息
                    case "CustomHealthMessage":
                        CustomHealthMessage healthMessage = new Gson().fromJson(dataJson, CustomHealthMessage.class);
                        if (healthMessage != null) {
                            if (parent instanceof MessageBaseHolder) {
                                //TODO  CustomHelloTIMUIController 改成 CustomHealthMessageController
                                CustomHealthMessageController.onDraw(parent, healthMessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                            }
                        }
                        break;
                    //问诊小结
                    case "CustomAnalyseMessage":
                        CustomAnalyseMessage customAnalyseMessage = new Gson().fromJson(dataJson, CustomAnalyseMessage.class);
                        if (customAnalyseMessage != null) {
                            if (parent instanceof MessageBaseHolder) {
                                CustomAnalyseMessageController.onDraw(parent, customAnalyseMessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                            }
                        }
                        break;
                    //暂定义为 电话预约
                    case "CustomAppointmentTimeMessage":
                        CustomHealthPlanMessage customHealthPlanMessage = new Gson().fromJson(dataJson, CustomHealthPlanMessage.class);
                        if (customHealthPlanMessage != null) {
                            if (parent instanceof MessageBaseHolder) {
                                CustomHealthPlanMessageController.onDraw(parent, customHealthPlanMessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                            }
                        }
                        break;


                    //  病情概述
                    case "CustomIllnessMessage":
                        CustomIllnessMessage customillnessmessage = new Gson().fromJson(dataJson, CustomIllnessMessage.class);
                        if (customillnessmessage != null) {
                            if (parent instanceof MessageBaseHolder) {
                                CustomlllnessMessageController.onDraw(parent, customillnessmessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                            }
                        }
                        break;


                    case "CustomUploadMessage":
                        CustomHealthDataMessage customHealthDataMessage = new Gson().fromJson(dataJson, CustomHealthDataMessage.class);
                        if (customHealthDataMessage != null) {
                            if (parent instanceof MessageBaseHolder) {
                                CustomHealthDataMessageController.onDraw(parent, customHealthDataMessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                            }
                        }
                        break;

                    //视频问诊
                    case "CustomVideoCallMessage":
                        CustomVideoCallMessage customVideoCallMessage = new Gson().fromJson(dataJson, CustomVideoCallMessage.class);
                        if (customVideoCallMessage != null) {
                            if (parent instanceof MessageBaseHolder) {
                                CustomVideoCallMessageController.onDraw(parent, customVideoCallMessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                            }
                        }
                        break;
                    //文章
                    case "CustomArticleMessage":
                        CustomCaseHistoryMessage customCaseHistoryMessage = new Gson().fromJson(dataJson, CustomCaseHistoryMessage.class);
                        if (customCaseHistoryMessage != null) {
                            if (parent instanceof MessageBaseHolder) {
                                CustomCaseHistoryMessageController.onDraw(parent, customCaseHistoryMessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                            }
                        }
                        break;
                    //问卷
                    case "CustomYuWenZhenMessage":
                    case "CustomWenJuanMessage":
                        CustomWenJuanMessage customWenJuanMessage = new Gson().fromJson(dataJson, CustomWenJuanMessage.class);
                        if (customWenJuanMessage != null) {
                            if (parent instanceof MessageBaseHolder) {
                                CustomQuestionMessageController.onDraw(parent, customWenJuanMessage, position, ((MessageBaseHolder) parent).getOnItemClickListener(), info);
                            }
                        }
                        break;


                }

            } catch (JSONException e) {
                TUIKitLog.w(TAG, "invalid json: " + new String(elem.getData()) + " " + e.getMessage());
            }

        }
    }

}

