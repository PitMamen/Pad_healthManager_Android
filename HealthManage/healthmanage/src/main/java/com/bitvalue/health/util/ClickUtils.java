package com.bitvalue.health.util;

import java.util.HashMap;

public class ClickUtils {

    // 两次点击按钮之间的点击间隔不能少于2000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 2000;
    private static HashMap<Integer, Long> sLastClickTimeMap = new HashMap<>();
    private static long lastTime = 0;


    public static boolean isFastClick(int viewId) {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        long lastClickTime = getLastClickTime(viewId);
        if ((curClickTime - lastClickTime) < MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        sLastClickTimeMap.put(viewId, curClickTime);
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


    public static boolean isFastClick() {
        long curClickTime = System.currentTimeMillis();
        lastTime = curClickTime;
        return curClickTime - lastTime > 1000;
    }

}
