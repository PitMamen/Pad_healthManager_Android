package com.bitvalue.health.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * @author created by bitvalue
 * @data : 05/24
 */
public class VersionUtils {


    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;

        } catch (Exception e) {
            Log.e("TAG", "getVersionCode: " + e.getMessage());
        }

        return code;
    }

    public static String getPackgeVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packgeName = "";

        try {
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            packgeName = info.versionName;

        } catch (Exception e) {
            Log.e("TAG", "getPackgeName: " + e.getMessage());
        }
        return packgeName;
    }



    public static String getPackgeAppId(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packgeName = "";

        try {
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            packgeName = info.packageName;

        } catch (Exception e) {
            Log.e("TAG", "getPackgeAppId: " + e.getMessage());
        }
        return packgeName;
    }

}
