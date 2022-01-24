package com.bitvalue.health.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TimeUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final SimpleDateFormat MM_DD_HH_MM_FORMAT = new SimpleDateFormat("MM/dd' 'HH:mm");
    public static final SimpleDateFormat YY_MM_DD_HH_MM_SS_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final SimpleDateFormat YY_MM_DD_FORMAT_2 = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat YY_MM_DD_FORMAT_3 = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat YY_MM_DD_FORMAT_4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat YY_MM_DD_FORMAT_5 = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat YY_MM_DD_FORMAT_7 = new SimpleDateFormat("MM-dd HH:mm");
    @SuppressLint("NewApi")
//    public static final SimpleDateFormat YY_MM_DD_FORMAT_5 = new SimpleDateFormat("YYYYMMddhhmmss");
    public static final SimpleDateFormat YY_MM_DD_FORMAT_6 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        if (timeInMillis <= 0) {
            return "";
        }
        return dateFormat.format(new Date(timeInMillis));
    }

    public static String formatDate_YY_MM_DD_HH_MM_SS_FORMAT(Date date) {
        return YY_MM_DD_HH_MM_SS_FORMAT.format(date);
    }

    public static String formatDate_YY_MM_DD_FORMAT_3(Date date) {
        return YY_MM_DD_FORMAT_3.format(date);
    }

    public static String parseString2StringWithSlash(String dateString) {
        return String.format("%s/%s/%s", dateString.substring(0, 4), dateString.substring(4, 6), dateString.substring(6));
    }

    public static String parseString2StringWithLevel(String dateString) {
        return String.format("%s-%s-%s", dateString.substring(0, 4), dateString.substring(4, 6), dateString.substring(6));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, YY_MM_DD_FORMAT_2);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }


    public static String getCurrenTime() {
        return YY_MM_DD_FORMAT_5.format(new Date());
    }

    public static String getCurrenTimeYMDHMS() {
        return YY_MM_DD_FORMAT_3.format(new Date());
    }


    public static String getCurrentTimeMinute(){
        return YY_MM_DD_FORMAT_7.format(new Date());
    }



    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static int getDateCount(long start, long end) {
        long time = end - start;
        int dayCount = (int) (time / (1000 * 60 * 60 * 24));
        return dayCount;
    }

    public static String formatDate(Date date) {
        return DEFAULT_DATE_FORMAT.format(date);
    }

    public static String formatDate_DD_HH_MM(Date date) {
        return MM_DD_HH_MM_FORMAT.format(date);
    }

    public static Date parseDate(String dateString, SimpleDateFormat simpleDateFormat) {
        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String dateString) throws ParseException {
        return YY_MM_DD_FORMAT_3.parse(dateString);
    }


    public static String formatTime(String dateString) {
        Date date = null;
        try {
            date = YY_MM_DD_FORMAT_3.parse(dateString);
        } catch (ParseException e) {
            Log.e("TTT", "日期转换异常---" );
            return dateString;
        }
        return YY_MM_DD_FORMAT_3.format(date);
    }

    public static long formatTimeToLong(String dateString) {

        try {
            Date  date = YY_MM_DD_FORMAT_3.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            Log.e("TTT", "日期转换异常---" );
            return 0;
        }
    }
    /**
     * @param mss 要转换的毫秒数
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {
        //long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;

        String time = "00:00:00";

        if (hours > 0) {
            time += ":" + String.format("%02d", hours);
        } else {
            time = "00";
        }

        if (minutes > 0) {
            time += ":" + String.format("%02d", minutes);
        } else {
            time += ":" + "00";
        }

//        if(seconds > 0 ) {
//            time += ":" + String.format("%02d", seconds);
//        }
//        else {
//            time += ":" + "00";
//        }

        return time;
    }

    /**
     * @param begin 时间段的开始
     * @param end   时间段的结束
     * @return 输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展示
     * @author fy.zhang
     */
    public static String formatDuring(Date begin, Date end) {
        return formatDuring(end.getTime() - begin.getTime());
    }
}
