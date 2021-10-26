package com.bitvalue.healthmanage.util;

import android.os.SystemClock;

import java.util.HashMap;

public class ClickUtils {

    // 两次点击按钮之间的点击间隔不能少于2000毫秒
    private static long lastTime;
    private static final int MIN_CLICK_DELAY_TIME = 2000;
    private static HashMap<Integer, Long> sLastClickTimeMap = new HashMap<>();
    /** 数组的长度为2代表只记录双击操作 */
    private static final long[] TIME_ARRAY = new long[2];

    public static boolean isFastClick() {
        boolean flag;
        long curClickTime = System.currentTimeMillis();
        flag = curClickTime - lastTime > 1000 ? false : true;
        lastTime = curClickTime;
        return flag;
    }


    public static void clear() {
        sLastClickTimeMap.clear();
    }

    private static Long getLastClickTime(int viewId) {
        Long lastClickTime = sLastClickTimeMap.get(viewId);
        if (lastClickTime == null) {
            lastClickTime = 0L;
        }
        return lastClickTime;
    }





    /**
     * 是否在短时间内进行了双击操作
     */
    public static boolean isOnDoubleClick() {
        // 默认间隔时长
        return isOnDoubleClick(1500);
    }

    /**
     * 是否在短时间内进行了双击操作
     */
    public static boolean isOnDoubleClick(int time) {
        System.arraycopy(TIME_ARRAY, 1, TIME_ARRAY, 0, TIME_ARRAY.length - 1);
        TIME_ARRAY[TIME_ARRAY.length - 1] = SystemClock.uptimeMillis();
        return TIME_ARRAY[0] >= (SystemClock.uptimeMillis() - time);
    }

}
