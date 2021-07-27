package com.bitvalue.healthmanage.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.bitvalue.healthmanage.app.AppApplication;
import com.google.gson.Gson;

/**
 * SharedPreferences 管理器
 */
public class SharedPreManager {
    public static final String PRE_NAME = "baseSp";

    private static SharedPreferences getSharePreferences(Context context) {
        return context.getSharedPreferences(PRE_NAME, 0);
    }

    private static SharedPreferences getSharePreferences() {
        return AppApplication.instance().getSharedPreferences(PRE_NAME, 0);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getSharePreferences(context);
        return preferences.edit();
    }

    private static SharedPreferences.Editor getEditor() {
        SharedPreferences preferences = getSharePreferences();
        return preferences.edit();
    }

    public static void putInt(String name, int value,Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(name, value);
        editor.commit();
    }

    public static void putBoolean(String name, boolean value,Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static void putLong(String name, Long value,Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putLong(name, value);
        editor.commit();
    }

    public static void putFloat(String name, float value,Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putFloat(name, value);
        editor.commit();
    }

    public static float getFloat(String name,float def,Context context) {
        return getSharePreferences(context).getFloat(name, def);
    }

    public static void putString(String name, String value,Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(name, value);
        editor.commit();
    }

    public static void putString(String name, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(name, value);
        editor.commit();
    }

    public static boolean getBoolean(String name, boolean defaultValue,Context context) {
        return getSharePreferences(context).getBoolean(name, defaultValue);
    }

    public static String getString(String name,Context context) {
        return getSharePreferences(context).getString(name, "");
    }

    public static String getString(String name) {
        return getSharePreferences().getString(name, "");
    }

    public static String getString(String name, String defValue,Context context) {
        return getSharePreferences(context).getString(name, defValue);
    }

    public static long getLong(String name,Context context) {
        return getSharePreferences(context).getLong(name, 0L);
    }

    public static int getInt(String name,Context context) {
        return getSharePreferences(context).getInt(name, 0);
    }

    /**
     * 获取json字符串进而转化成对象
     */
    public static <T> T getObject(String name, Class<T> clz,Context context) {
        String json = getString(name, context);
        if (json.length() == 0) {
            return null;
        }
        return new Gson().fromJson(json, clz);
    }

    /**
     * 以json字符串的方式存储对象
     */
    public static void putObject(String name, Object object,Context context) {
        String jsonStr = "";
        if (object != null) {
            jsonStr = new Gson().toJson(object);
        }
        putString(name, jsonStr,context);
    }

    /**
     * 以json字符串的方式存储对象
     */
    public static void putObject(String name, Object object) {
        String jsonStr = "";
        if (object != null) {
            jsonStr = new Gson().toJson(object);
        }
        putString(name, jsonStr);
    }

    public static class Constant{
        public static final String TOKEN = "Authorization";
        public static final String USER_NAME = "userName";
        public static final String USER_ID = "userId";
    }
}
