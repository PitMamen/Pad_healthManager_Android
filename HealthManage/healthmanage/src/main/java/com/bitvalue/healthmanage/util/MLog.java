package com.bitvalue.healthmanage.util;

import android.util.Log;


/**
 * Created by Administrator on 2016/12/2.
 */

public class MLog {
//    public static boolean DEBUG = BuildConfig.LOG_DEBUG;
    public static boolean DEBUG = true;
    private static final String TAG = "MLog_";

    public static void e(String tag, String str) {
        if (DEBUG)
            Log.e(TAG + tag, str);
    }

    public static void e(Object str) {
        if (DEBUG)
            Log.e(TAG, String.valueOf(str));
    }
}
