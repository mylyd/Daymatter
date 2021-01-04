package com.mobo.daymatter.utils;

import com.mobo.daymatter.manager.DrinkManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PunchClockUtils {
    private static final String CLOCK_KEY_FORMAT = "-yyyy-MM-dd";
    private static final String CLOCK_STRING_FORMAT = "-%04d-%02d-%02d";
    private static final String TIME_HH_MM = "HH:mm";
    private static final String TIME_HH_MM_SS = "HH:mm:ss";
    private static final String TIME_YY_MM = "yyyyMM";
    private static final String TIME_YY_MM_DD = "yyyyMMdd";
    private static final String TIME_YY_MM_DD_DOT = "yyyy.MM.dd";

    private PunchClockUtils() {
    }

    // 获取今天的字符串
    public static String getCurDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(CLOCK_KEY_FORMAT);
        return format.format(calendar.getTime());
    }

    public static String getSomeDay(int year, int month, int day) {
        return String.format(CLOCK_STRING_FORMAT, year, month, day);
    }

    public static String getSomeDay(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat(CLOCK_KEY_FORMAT);
        return format.format(calendar.getTime());
    }

    /**
     * 获取当前日期的前面一个星期天字符串
     *
     * @return
     */
    public static String getStartWeek() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week != 7) {
            calendar.add(Calendar.DAY_OF_WEEK, -week);
        }
        SimpleDateFormat format = new SimpleDateFormat(TIME_HH_MM);
        return format.format(calendar.getTime());
    }

    public static String getHHMM(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat(TIME_HH_MM);
        return format.format(calendar.getTime());
    }

    public static String getHHMMSS(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat(TIME_HH_MM_SS);
        return format.format(calendar.getTime());
    }

    public static String getYYMM(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat(TIME_YY_MM);
        return format.format(calendar.getTime());
    }

    public static String getYYMMDD(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat(TIME_YY_MM_DD);
        return format.format(calendar.getTime());
    }

    public static String getYYMMDDDot(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat(TIME_YY_MM_DD_DOT);
        return format.format(calendar.getTime());
    }

    public static long getTimeInMillis(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                hour, minute);
        return calendar.getTimeInMillis();
    }

    public static boolean getIsNoToday() {
        String time = DrinkManager.getDrinkDataTime();
        if (time == null) {
            DrinkManager.setDrinkDataTime(getYYMMDD(System.currentTimeMillis()));
            return false;
        } else {
            if (time.equals(getYYMMDD(System.currentTimeMillis()))) {
                return false;
            } else {
                DrinkManager.setDrinkDataTime(getYYMMDD(System.currentTimeMillis()));
                return true;
            }
        }
    }
}
