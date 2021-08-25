package com.bitvalue.healthmanage.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bitvalue.healthmanage.widget.tasks.bean.SavePlanApi;

/**
 * Created by Administrator on 2018/2/12.
 */

public class DataUtil {
    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 字符串空判断
     *
     * @param arg
     * @return true为空（包括“null”） false 反之
     */
    public static boolean isNullOrAirForString(String arg) {
        if (arg == null || "".equals(arg.trim()) || "null".equals(arg)) {
            return true;
        }
        return false;
    }

    public static boolean hideInputBoard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
        return false;
    }

    public interface OnNormalDialogClicker {
        void onPositive();

        void onNegative();
    }

    public interface OnNormalInputDialogClicker {
        void onPositive(String input, Dialog dialog);

        void onNegative(Dialog dialog);
    }

    /**
     * 弹出基本的对话框
     * 对MessageDialog再封装一层
     */
    public static void showNormalDialog(Activity context, String title, String message, String positive, String nagative, OnNormalDialogClicker onNormalDialogClicker) {
        MessageDialog messageDialog = new MessageDialog(context);
        messageDialog.setOnExecuteClickListener(new MessageDialog.OnExecuteClickListener() {
            @Override
            public void onNegativeClick() {
                messageDialog.dismiss();
                if (onNormalDialogClicker != null) {
                    onNormalDialogClicker.onNegative();
                }
            }

            @Override
            public void onPositiveClick() {
                messageDialog.dismiss();
                if (onNormalDialogClicker != null) {
                    onNormalDialogClicker.onPositive();
                }
            }
        });
        messageDialog.setTitle(title);
        messageDialog.setMessage(message);
        messageDialog.setNegativeTxt(nagative);
        messageDialog.setPositiveTxt(positive);

        if (messageDialog != null && !messageDialog.isShowing()) {
            messageDialog.show();
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public static void showSignDialog(Activity activity){
//        SignDialog signDialog = new SignDialog(activity);
//        signDialog.setOnExecuteClickListener();
//    }

//    /**
//     * 弹出基本的对话框
//     * 对MessageDialog再封装一层
//     */
//    public static void showNormalDialogNoCancel(Activity context, String title, String message, String positive, String nagative, OnNormalDialogClicker onNormalDialogClicker) {
//        MessageDialogCenter messageDialog = new MessageDialogCenter(context);
//        messageDialog.setOnExecuteClickListener(new MessageDialogCenter.OnExecuteClickListener() {
//            @Override
//            public void onNegativeClick() {
//                messageDialog.dismiss();
//                if (onNormalDialogClicker != null) {
//                    onNormalDialogClicker.onNegative();
//                }
//            }
//
//            @Override
//            public void onPositiveClick() {
//                messageDialog.dismiss();
//                if (onNormalDialogClicker != null) {
//                    onNormalDialogClicker.onPositive();
//                }
//            }
//        });
//        messageDialog.setTitle(title);
//        messageDialog.setMessage(message);
//        messageDialog.setNegativeTxt(nagative);
//        messageDialog.setPositiveTxt(positive);
//        messageDialog.setCanceledOnTouchOutside(false);
//        messageDialog.setCancelable(false);
//
//        if (messageDialog != null && !messageDialog.isShowing()) {
//            messageDialog.show();
//        }
//    }

    /**
     * 弹出基本的对话框
     * 对MessageDialog再封装一层
     */
    public static void showNormalDialog(Activity context, String title, String message, String positive, String nagative, OnNormalDialogClicker onNormalDialogClicker, boolean isForce) {
        MessageDialog messageDialog = new MessageDialog(context);
        messageDialog.setOnExecuteClickListener(new MessageDialog.OnExecuteClickListener() {
            @Override
            public void onNegativeClick() {
                messageDialog.dismiss();
                if (onNormalDialogClicker != null) {
                    onNormalDialogClicker.onNegative();
                }
            }

            @Override
            public void onPositiveClick() {
                messageDialog.dismiss();
                if (onNormalDialogClicker != null) {
                    onNormalDialogClicker.onPositive();
                }
            }
        });
        messageDialog.setTitle(title);
        messageDialog.setMessage(message);
        messageDialog.setNegativeTxt(nagative);
        messageDialog.setPositiveTxt(positive);
        if (isForce) {
            messageDialog.setCancelable(false);
            messageDialog.setCanceledOnTouchOutside(false);
            messageDialog.getTxt_negative().setVisibility(View.GONE);
        }

        if (messageDialog != null && !messageDialog.isShowing()) {
            messageDialog.show();
        }
    }

//    /**
//     * 弹出基本的对话框
//     * 对MessageDialog再封装一层
//     */
//    public static void showNormalEditDialog(Activity context, String title, String message, String hint, String positive, String nagative, OnNormalInputDialogClicker inputDialogClicker) {
//        MsgInputDialog messageDialog = new MsgInputDialog(context);
//        messageDialog.setOnExecuteClickListener(new MsgInputDialog.OnExecuteClickListener() {
//            @Override
//            public void onNegativeClick() {
//
//                if (inputDialogClicker != null) {
//                    inputDialogClicker.onNegative(messageDialog);
//                }
//            }
//
//            @Override
//            public void onPositiveClick(String input) {
//
//                if (inputDialogClicker != null) {
//                    inputDialogClicker.onPositive(input, messageDialog);
//                }
//            }
//        });
//        messageDialog.setTitle(title);
//        messageDialog.setMessage(message);
//        messageDialog.setNegativeTxt(nagative);
//        messageDialog.setPositiveTxt(positive);
//        messageDialog.setTextHint(hint);
//        if (messageDialog != null && !messageDialog.isShowing()) {
//            messageDialog.show();
//        }
//    }

    /**
     * 收藏回调
     */
    public interface CollectListener {
        void onCollectSuccess(String data);

        void onCancelCollectSuccess();
    }

    public static SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO getNotNullData(SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO templateTaskContentDTO) {
        SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO noNullData = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO();

        if (templateTaskContentDTO.taskType!= null && !templateTaskContentDTO.taskType.isEmpty()) {
            noNullData.taskType = templateTaskContentDTO.taskType;
        }

        if (templateTaskContentDTO.contentId!= null && !templateTaskContentDTO.contentId.isEmpty()) {
            noNullData.contentId = templateTaskContentDTO.contentId;
        }

        if (templateTaskContentDTO.id!= null && !templateTaskContentDTO.id.isEmpty()) {
            noNullData.id = templateTaskContentDTO.id;
        }

        if (templateTaskContentDTO.taskType!= null && !templateTaskContentDTO.taskType.isEmpty()) {
            noNullData.taskType = templateTaskContentDTO.taskType;
        }

        noNullData.contentDetail = new SavePlanApi.TemplateTaskDTO.TemplateTaskContentDTO.ContentDetailDTO();
        if (templateTaskContentDTO.contentDetail.id != null && !templateTaskContentDTO.contentDetail.id.isEmpty()) {
            noNullData.contentDetail.id = templateTaskContentDTO.contentDetail.id;
        }

        if (templateTaskContentDTO.contentDetail.questName != null && !templateTaskContentDTO.contentDetail.questName.isEmpty()) {
            noNullData.contentDetail.questName = templateTaskContentDTO.contentDetail.questName;
        }

        if (templateTaskContentDTO.contentDetail.questId != null && !templateTaskContentDTO.contentDetail.questId.isEmpty()) {
            noNullData.contentDetail.questId = templateTaskContentDTO.contentDetail.questId;
        }

        if (templateTaskContentDTO.contentDetail.knowUrl != null && !templateTaskContentDTO.contentDetail.knowUrl.isEmpty()) {
            noNullData.contentDetail.knowUrl = templateTaskContentDTO.contentDetail.knowUrl;
        }

        if (templateTaskContentDTO.contentDetail.knowContent != null && !templateTaskContentDTO.contentDetail.knowContent.isEmpty()) {
            noNullData.contentDetail.knowContent = templateTaskContentDTO.contentDetail.knowContent;
        }

        if (templateTaskContentDTO.contentDetail.articleId != null && !templateTaskContentDTO.contentDetail.articleId.isEmpty()) {
            noNullData.contentDetail.articleId = templateTaskContentDTO.contentDetail.articleId;
        }

        if (templateTaskContentDTO.contentDetail.title != null && !templateTaskContentDTO.contentDetail.title.isEmpty()) {
            noNullData.contentDetail.title = templateTaskContentDTO.contentDetail.title;
        }

        if (templateTaskContentDTO.contentDetail.remindName != null && !templateTaskContentDTO.contentDetail.remindName.isEmpty()) {
            noNullData.contentDetail.remindName = templateTaskContentDTO.contentDetail.remindName;
        }

        if (templateTaskContentDTO.contentDetail.remindContent != null && !templateTaskContentDTO.contentDetail.remindContent.isEmpty()) {
            noNullData.contentDetail.remindContent = templateTaskContentDTO.contentDetail.remindContent;
        }

        if (templateTaskContentDTO.contentDetail.picList != null && !templateTaskContentDTO.contentDetail.picList.isEmpty()) {
            noNullData.contentDetail.picList = templateTaskContentDTO.contentDetail.picList;
        }

        if (templateTaskContentDTO.contentDetail.voiceList != null && !templateTaskContentDTO.contentDetail.voiceList.isEmpty()) {
            noNullData.contentDetail.voiceList = templateTaskContentDTO.contentDetail.voiceList;
        }

        if (templateTaskContentDTO.contentDetail.videoList != null && !templateTaskContentDTO.contentDetail.videoList.isEmpty()) {
            noNullData.contentDetail.videoList = templateTaskContentDTO.contentDetail.videoList;
        }
        return noNullData;
    }
}
