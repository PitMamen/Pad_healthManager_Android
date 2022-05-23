package com.bitvalue.sdk.collab.modules.chat.layout.input;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitvalue.sdk.collab.R;
import com.bitvalue.sdk.collab.base.IBaseAction;
import com.bitvalue.sdk.collab.base.TUIChatControllerListener;
import com.bitvalue.sdk.collab.base.TUIKitListenerManager;
import com.bitvalue.sdk.collab.modules.chat.base.BaseInputFragment;
import com.bitvalue.sdk.collab.modules.chat.base.ChatInfo;
import com.bitvalue.sdk.collab.modules.chat.base.InputMoreActionUnit;
import com.bitvalue.sdk.collab.modules.chat.interfaces.IInputLayout;
import com.bitvalue.sdk.collab.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class InputLayoutUI extends LinearLayout implements IInputLayout {

    protected static final int CAPTURE = 1;
    protected static final int AUDIO_RECORD = 2;
    protected static final int VIDEO_RECORD = 3;
    protected static final int SEND_PHOTO = 4;
    protected static final int SEND_FILE = 5;
    private static String TAG = InputLayoutUI.class.getSimpleName();
    /**
     * 语音/文字切换输入控件
     */
    protected ImageView mAudioInputSwitchButton;
    protected boolean mAudioInputDisable;

    /**
     * 表情按钮
     */
    protected ImageView mEmojiInputButton;
    protected boolean mEmojiInputDisable;

    /**
     * 更多按钮
     */
    protected ImageView mMoreInputButton;
    protected Object mMoreInputEvent;
    protected boolean mMoreInputDisable;

    /**
     * 消息发送按钮
     */
    protected Button mSendTextButton;

    /**
     * 语音长按按钮
     */
    protected Button mSendAudioButton;

    /**
     * 文本输入框
     */
    public TIMMentionEditText mTextInput;

    /**
     * 铃铛图标 提醒
     */
    public ImageView btn_lingdang;


    /***
     * 发送快捷消息     预诊收集          发送提醒       发送问卷        发送文章       快捷回复    病历夹
     */
    public TextView tv_datacollection, tv_sendremind, tv_sendquestion, tv_sendarticle, tv_sendshortcut, tv_medicalfolder;


    protected AppCompatActivity mActivity;
    protected View mInputMoreLayout;
    //    protected ShortcutArea mShortcutArea;
    private LinearLayout linearLayout;
    public LinearLayout ll_shortCutlayout; //患者咨询中的快捷语句 聊天布局(只针对患者咨询中 显示)
    protected View mInputMoreView;
    protected ChatInfo mChatInfo;
    protected List<InputMoreActionUnit> mInputMoreActionList = new ArrayList<>();
    protected List<InputMoreActionUnit> mInputMoreCustomActionList = new ArrayList<>();
    private AlertDialog mPermissionDialog;
    private boolean mSendPhotoDisable;
    private boolean mCaptureDisable;
    private boolean mVideoRecordDisable;
    private boolean mSendFileDisable;
    private boolean mHealthPlanDisable;
    private boolean mHealthAnalyseDisable;
    private boolean mHealthMsgDisable;
    private boolean mHealthFilesDisable;
    private boolean mHealthUploadDisable;
    private boolean mHealthVideoDisable;
    private boolean mHealthplantrackDisable;
    private boolean showCall = false;
    private boolean showVedio = false;
    /**
     * 健康管理聊天类型
     */
    public static final int CHAT_TYPE_HEALTH = 100;
    /**
     * 云看诊聊天类型
     */
    public static final int CHAT_TYPE_VIDEO = 101;

    /**
     * 医生好友
     */
    public static final int CHAT_TYPE_DocFriends = 102;
    private int chatType;

    public InputLayoutUI(Context context) {
        super(context);
        initViews();
    }

    public InputLayoutUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public InputLayoutUI(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }


    public void setShowCallButton(boolean isshow) {
        showCall = isshow;
    }

    public void setShowVedioButton(boolean isShow){
        showVedio = isShow;
    }



    private void initViews() {
        mActivity = (AppCompatActivity) getContext();
        inflate(mActivity, R.layout.chat_input_layout, this);
//        mShortcutArea = findViewById(R.id.shortcut_area);
        ll_shortCutlayout = findViewById(R.id.ll_shortcut);
        tv_datacollection = findViewById(R.id.tv_datacollection);
        tv_sendremind = findViewById(R.id.tv_sendremind);
        tv_sendquestion = findViewById(R.id.tv_sendquestion);
        tv_sendarticle = findViewById(R.id.tv_sendarti);
        tv_medicalfolder = findViewById(R.id.tv_medicalfolder);
        tv_sendshortcut = findViewById(R.id.tv_shortcut);
        linearLayout = findViewById(R.id.ll_input_content);
        mInputMoreView = findViewById(R.id.more_groups);
        mSendAudioButton = findViewById(R.id.chat_voice_input);
        mAudioInputSwitchButton = findViewById(R.id.voice_input_switch);
        mEmojiInputButton = findViewById(R.id.face_btn);
        mMoreInputButton = findViewById(R.id.more_btn);
        mSendTextButton = findViewById(R.id.send_btn);
        mTextInput = findViewById(R.id.chat_message_input);
        btn_lingdang = findViewById(R.id.remind);

        // 子类实现所有的事件处理
        init();
    }


    public void setbottomMargin(int heigth) {
        LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.bottomMargin = heigth;
        linearLayout.setLayoutParams(layoutParams);
    }

    protected void assembleActions() {
        mInputMoreActionList.clear();
        InputMoreActionUnit actionUnit;
        Log.e(TAG, "assembleActions: " + chatType);
        switch (chatType) {
            /***
             * 健康管理
             */

            //健康计划
            case CHAT_TYPE_HEALTH:
                if (!mHealthPlanDisable) {
                    actionUnit = new InputMoreActionUnit() {
                        @Override
                        public void onAction(String chatInfoId, int chatType) {
                            startHealthPlan();
                        }
                    };
                    actionUnit.setIconResId(R.drawable.icon_jkjh);
                    actionUnit.setTitleId(R.string.health);
                    mInputMoreActionList.add(actionUnit);
                }

//           健康评估
                if (!mHealthAnalyseDisable) {
                    actionUnit = new InputMoreActionUnit() {
                        @Override
                        public void onAction(String chatInfoId, int chatType) {
                            startHealthAnalyse();
                        }
                    };
                    actionUnit.setIconResId(R.drawable.icon_jkpg);
                    actionUnit.setTitleId(R.string.analyse);
                    mInputMoreActionList.add(actionUnit);
                }

//                健康消息
                if (!mHealthMsgDisable) {
                    actionUnit = new InputMoreActionUnit() {
                        @Override
                        public void onAction(String chatInfoId, int chatType) {
                            startHealthMsg();
                        }
                    };
                    actionUnit.setIconResId(R.drawable.icon_jkxx);
                    actionUnit.setTitleId(R.string.heal_msg);
                    mInputMoreActionList.add(actionUnit);
                }

                //健康档案
                if (!mHealthFilesDisable) {
                    actionUnit = new InputMoreActionUnit() {
                        @Override
                        public void onAction(String chatInfoId, int chatType) {
                            startHealthFiles();
                        }
                    };
                    actionUnit.setIconResId(R.drawable.icon_files);
                    actionUnit.setTitleId(R.string.health_files);
                    mInputMoreActionList.add(actionUnit);
                }


                break;


            /**
             * 虚拟诊间
             */
            case CHAT_TYPE_VIDEO:
                //视频看诊
                if (!mHealthVideoDisable) {
                    actionUnit = new InputMoreActionUnit() {
                        @Override
                        public void onAction(String chatInfoId, int chatType) {
                            startVideoCommunicate();
                        }
                    };
                    actionUnit.setIconResId(R.drawable.icon_video_communicate);
//                    actionUnit.setIconResId(R.drawable.icon_spkz);
                    actionUnit.setTitleId(R.string.video_communicate);
                    if (showVedio){
                        mInputMoreActionList.add(actionUnit);
                    }
                }

                //书写病历
                if (!mHealthVideoDisable) {
                    actionUnit = new InputMoreActionUnit() {
                        @Override
                        public void onAction(String chatInfoId, int chatType) {
                            writeConsultConclusion();
                        }
                    };
                    actionUnit.setIconResId(R.drawable.icon_write);
                    actionUnit.setTitleId(R.string.write_conclusion);
//                    mInputMoreActionList.add(actionUnit);
                }

                //拨打电话
                if (!mHealthVideoDisable) {
                    actionUnit = new InputMoreActionUnit() {
                        @Override
                        public void onAction(String chatInfoId, int chatType) {
                            callphone();
                        }
                    };
                    actionUnit.setIconResId(R.drawable.icon_bddh);
                    actionUnit.setTitleId(R.string.call_phone);
                    if (showCall) {
                        mInputMoreActionList.add(actionUnit);
                    }
                }

                break;


            /**
             * 这里后续可能会加医生好友模块  逻辑先不做
             */
            case CHAT_TYPE_DocFriends:
                break;


            default:
                break;

        }


        //资料上传暂时不做
//        if (!mHealthUploadDisable) {
//            actionUnit = new InputMoreActionUnit() {
//                @Override
//                public void onAction(String chatInfoId, int chatType) {
//                    startUploadData();
//                }
//            };
//            actionUnit.setIconResId(R.drawable.icon_zlsc);
//            actionUnit.setTitleId(R.string.data_upload);
//            mInputMoreActionList.add(actionUnit);
//        }

        //腾讯原来的封装
//        if (!mSendPhotoDisable) {
//            actionUnit = new InputMoreActionUnit() {
//                @Override
//                public void onAction(String chatInfoId, int chatType) {
//                    startSendPhoto();
//                }
//            };
//            actionUnit.setIconResId(R.drawable.ic_more_picture);
//            actionUnit.setTitleId(R.string.pic);
//            mInputMoreActionList.add(actionUnit);
//        }
//
//        if (!mCaptureDisable) {
//            actionUnit = new InputMoreActionUnit() {
//                @Override
//                public void onAction(String chatInfoId, int chatType) {
//                    startCapture();
//                }
//            };
//            actionUnit.setIconResId(R.drawable.ic_more_camera);
//            actionUnit.setTitleId(R.string.photo);
//            mInputMoreActionList.add(actionUnit);
//        }
//
//        if (!mVideoRecordDisable) {
//            actionUnit = new InputMoreActionUnit() {
//                @Override
//                public void onAction(String chatInfoId, int chatType) {
//                    startVideoRecord();
//                }
//            };
//            actionUnit.setIconResId(R.drawable.ic_more_video);
//            actionUnit.setTitleId(R.string.video);
//            mInputMoreActionList.add(actionUnit);
//        }
//
//        if (!mSendFileDisable) {
//            actionUnit = new InputMoreActionUnit() {
//                @Override
//                public void onAction(String chatInfoId, int chatType) {
//                    startSendFile();
//                }
//            };
//            actionUnit.setIconResId(R.drawable.ic_more_file);
//            actionUnit.setTitleId(R.string.file);
//            mInputMoreActionList.add(actionUnit);
//        }

        addActionsFromListeners();
        mInputMoreActionList.addAll(mInputMoreCustomActionList);
    }

    private void addActionsFromListeners() {
        if (mChatInfo == null) {
            return;
        }

        for (TUIChatControllerListener chatListener : TUIKitListenerManager.getInstance().getTUIChatListeners()) {
            List<IBaseAction> actionList = chatListener.onRegisterMoreActions();
            if (actionList == null) {
                continue;
            }
            for (final IBaseAction action : actionList) {
                final InputMoreActionUnit actionUnit;
                if (action instanceof InputMoreActionUnit) {
                    actionUnit = (InputMoreActionUnit) action;
                } else {
                    continue;
                }
                // 让注册者决定当前聊天类型下此 Action 是否应该显示
                if (!actionUnit.isEnable(mChatInfo.getType())) {
                    continue;
                }
                actionUnit.setChatInfo(mChatInfo);
                mInputMoreActionList.add(actionUnit);
            }
        }
    }

    protected boolean checkPermission(int type) {
        if (!PermissionUtils.checkPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return false;
        }
        if (!PermissionUtils.checkPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return false;
        }
        if (type == SEND_FILE || type == SEND_PHOTO) {
            return true;
        } else if (type == CAPTURE) {
            return PermissionUtils.checkPermission(mActivity, Manifest.permission.CAMERA);
        } else if (type == AUDIO_RECORD) {
            return PermissionUtils.checkPermission(mActivity, Manifest.permission.RECORD_AUDIO);
        } else if (type == VIDEO_RECORD) {
            return PermissionUtils.checkPermission(mActivity, Manifest.permission.CAMERA)
                    && PermissionUtils.checkPermission(mActivity, Manifest.permission.RECORD_AUDIO);
        }
        return true;
    }

    protected abstract void init();

    protected abstract void startHealthPlan();

    protected abstract void startHealthAnalyse();

    protected abstract void startHealthMsg();

    protected abstract void startHealthFiles();

    protected abstract void startVideoCommunicate();

    protected abstract void writeConsultConclusion();

    protected abstract void callphone();

    protected abstract void startUploadData();

    protected abstract void startSendPhoto();

    protected abstract void startCapture();

    protected abstract void startVideoRecord();

    protected abstract void startSendFile();

    @Override
    public void disableAudioInput(boolean disable) {
        mAudioInputDisable = disable;
        if (disable) {
            mAudioInputSwitchButton.setVisibility(GONE);
        } else {
            mAudioInputSwitchButton.setVisibility(VISIBLE);
        }
    }

    @Override
    public void disableEmojiInput(boolean disable) {
        mEmojiInputDisable = disable;
        if (disable) {
            mEmojiInputButton.setVisibility(GONE);
        } else {
            mEmojiInputButton.setVisibility(VISIBLE);
        }
    }

    @Override
    public void disableMoreInput(boolean disable) {
        mMoreInputDisable = disable;
        if (disable) {
            mMoreInputButton.setVisibility(GONE);
            mSendTextButton.setVisibility(VISIBLE);
        } else {
            mMoreInputButton.setVisibility(VISIBLE);
            mSendTextButton.setVisibility(GONE);
        }
    }

    @Override
    public void replaceMoreInput(BaseInputFragment fragment) {
        mMoreInputEvent = fragment;
    }

    @Override
    public void replaceMoreInput(OnClickListener listener) {
        mMoreInputEvent = listener;
    }

    @Override
    public void disableSendPhotoAction(boolean disable) {
        mSendPhotoDisable = disable;
    }

    @Override
    public void disableCaptureAction(boolean disable) {
        mCaptureDisable = disable;
    }

    @Override
    public void disableVideoRecordAction(boolean disable) {
        mVideoRecordDisable = disable;
    }

    @Override
    public void disableSendFileAction(boolean disable) {
        mSendFileDisable = disable;
    }

    @Override
    public void addAction(InputMoreActionUnit action) {
        mInputMoreCustomActionList.add(action);
    }

    @Override
    public EditText getInputText() {
        return mTextInput;
    }

    protected void showMoreInputButton(int visibility) {
        if (mMoreInputDisable) {
            return;
        }
        mMoreInputButton.setVisibility(visibility);
    }

    protected void showSendTextButton(int visibility) {
        if (mMoreInputDisable) {
            mSendTextButton.setVisibility(VISIBLE);
        } else {
            mSendTextButton.setVisibility(visibility);
        }
    }

    protected void showEmojiInputButton(int visibility) {
        if (mEmojiInputDisable) {
            return;
        }
        mEmojiInputButton.setVisibility(visibility);
    }

    public void clearCustomActionList() {
        mInputMoreCustomActionList.clear();
    }

    public void setChatInfo(ChatInfo chatInfo) {
        mChatInfo = chatInfo;
    }

    public ChatInfo getChatInfo() {
        return mChatInfo;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }
}
