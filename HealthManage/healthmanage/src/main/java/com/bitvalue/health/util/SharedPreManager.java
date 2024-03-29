package com.bitvalue.health.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.bitvalue.health.Application;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * SharedPreferences 管理器
 */
public class SharedPreManager {
    public static final String PRE_NAME = "baseSp";

    private static SharedPreferences getSharePreferences(Context context) {
        return context.getSharedPreferences(PRE_NAME, 0);
    }

    private static SharedPreferences getSharePreferences() {
        return Application.instance().getSharedPreferences(PRE_NAME, 0);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getSharePreferences(context);
        return preferences.edit();
    }

    private static SharedPreferences.Editor getEditor() {
        SharedPreferences preferences = getSharePreferences();
        return preferences.edit();
    }

    public static void putInt(String name, int value, Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(name, value);
        editor.commit();
    }

    public static void putBoolean(String name, boolean value, Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static void putLong(String name, Long value, Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putLong(name, value);
        editor.commit();
    }

    public static void putFloat(String name, float value, Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putFloat(name, value);
        editor.commit();
    }

    public static float getFloat(String name, float def, Context context) {
        return getSharePreferences(context).getFloat(name, def);
    }

    public static void putString(String name, String value, Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(name, value);
        editor.commit();
    }

    public static void putString(String name, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(name, value);
        editor.commit();
    }

    public static boolean getBoolean(String name, boolean defaultValue, Context context) {
        return getSharePreferences(context).getBoolean(name, defaultValue);
    }

    public static String getString(String name, Context context) {
        return getSharePreferences(context).getString(name, "");
    }

    public static String getString(String name) {
        return getSharePreferences().getString(name, "");
    }

    public static String getString(String name, String defValue, Context context) {
        return getSharePreferences(context).getString(name, defValue);
    }

    public static long getLong(String name, Context context) {
        return getSharePreferences(context).getLong(name, 0L);
    }

    public static int getInt(String name, Context context) {
        return getSharePreferences(context).getInt(name, 0);
    }

    /**
     * 获取json字符串进而转化成对象
     */
    public static <T> T getObject(String name, Class<T> clz, Context context) {
        String json = getString(name, context);
        if (json.length() == 0) {
            return null;
        }
        return new Gson().fromJson(json, clz);
    }

    /**
     * 以json字符串的方式存储对象
     */
    public static void putObject(String name, Object object, Context context) {
        String jsonStr = "";
        if (object != null) {
            jsonStr = new Gson().toJson(object);
        }
        putString(name, jsonStr, context);
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

    /***
     * 以json字符串的方式存储集合
     * @param UserId
     */
    public static void putStringList(String UserId) {
        List<String> userIdList = new ArrayList<>();
        userIdList.add(UserId);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(userIdList); //将List转换成Json
        SharedPreferences.Editor editor = getEditor();
        editor.putString("KEY_NewUserModel_LIST_DATA", jsonStr); //存入json串
        editor.commit();  //提交
    }

    /***
     * 以json字符串的方式获取集合
     * @param context
     */
    public static List<String> getStringList(Context context) {
        List<String> list = new ArrayList<>();
        String stringJson = getSharePreferences(context).getString("KEY_NewUserModel_LIST_DATA", "");
        if (EmptyUtil.isEmpty(stringJson)) {
            return list;
        }
        Gson gson = new Gson();
        list = gson.fromJson(stringJson, new TypeToken<List<String>>() {
        }.getType());
        return list;
    }

}
