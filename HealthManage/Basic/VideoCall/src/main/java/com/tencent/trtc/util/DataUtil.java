package com.tencent.trtc.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
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

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
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
    public static void showNormalDialog(Activity context, String title, String message, String positive, String nagative, final OnNormalDialogClicker onNormalDialogClicker) {
        final MessageDialog messageDialog = new MessageDialog(context);
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

}
