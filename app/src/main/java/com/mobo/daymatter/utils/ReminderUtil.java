package com.mobo.daymatter.utils;

import android.content.Context;
import android.util.Log;

import com.mobo.daymatter.R;
import com.mobo.daymatter.beans.DateBean;
import com.mobo.daymatter.beans.LeftBean;
import com.mobo.daymatter.beans.ReminderBean;
import com.mobo.daymatter.beans.RepeatMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 纪念日相关转换方法
 */
public final class ReminderUtil {
    private ReminderUtil() {
    }

    private static final long ONE_DAY = 24 * 60 * 60 * 1000;
    private static final String REMINDER_TARGET_FORMAT = "yyyy-MM-dd";

    // 相差天数
//    public static int getLeftDay(ReminderBean bean) {
//        if (bean == null) {
//            return 0;
//        }
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(bean.getDate().getDay(), bean.getDate().getMonth(), bean.getDate().getDay());
//        return (int) ((calendar.getTimeInMillis() - System.currentTimeMillis()) / ONE_DAY);
//    }

//    /**
//     * 剩余天数肯定大于等于0
//     *
//     * @param context
//     * @param bean
//     * @return
//     */
//    public static int getValidLeftDay(Context context, ReminderBean bean) {
//        if (bean == null) {
//            return 0;
//        }
//        ReminderBean newBean = updateReminderBean(bean);
//        if (newBean != null) {
//            //ReminderManager.get(context).editReminder(bean, newBean); //需不需要变化看需求
//            return getLeftDay(newBean);
//        } else {
//            return getLeftDay(bean);
//        }
//    }

    private static int diffDay(long startTime, long endTime) {
        return (int) (startTime / ONE_DAY - endTime / ONE_DAY);
    }

    /**
     * 根据重复类型更新时间
     * 日期过期后就更改为下一个周期的日期
     *
     * @param bean 未更新数据
     * @return 更新后的结果，null表示未更新
     */
    public static ReminderBean updateReminderBean(ReminderBean bean) {
        // 如果是不重复提醒就不更改提醒时间
        if (bean == null || bean.getRepeatMode() == RepeatMode.NO_REPEAT) {
            return bean;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(bean.getReminderTime());

        // 如果时间没过期就不更改提醒时间
        long currentTimeMillis = System.currentTimeMillis();
        int diffDay = diffDay(currentTimeMillis, calendar.getTimeInMillis());
        if (diffDay <= 0) {
            return bean;
        }

        // 如果时间过期就更改提醒时间为下一个提醒时间
        while (diffDay > 0) {
            if (bean.getRepeatMode() == RepeatMode.ONE_WEEK) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            } else if (bean.getRepeatMode() == RepeatMode.ONE_MOTH) {
                calendar.add(Calendar.MONTH, 1);
            } else {
                calendar.add(Calendar.YEAR, 1);
            }
            diffDay = diffDay(currentTimeMillis, calendar.getTimeInMillis());
        }

        bean.setReminderTime(calendar.getTimeInMillis());
        return bean;
    }

    /**
     * 根据提醒存储数据转换为展示信息
     *
     * @param context
     * @param bean
     * @return
     */
    public static LeftBean getLeftBean(Context context, ReminderBean bean) {
        LeftBean leftBean = new LeftBean();
        if (bean == null) {
            return leftBean;
        }

        //1.是否检查已经到期，到期后是否根据重复类型更改纪念日的日期
        bean = updateReminderBean(bean);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(bean.getReminderTime());

        //2.计算提醒日与今天相差几天
        int diffDay = diffDay(System.currentTimeMillis(), calendar.getTimeInMillis());
        leftBean.setDiffDay(diffDay);
        leftBean.setDay(Math.abs(diffDay));
        leftBean.setTitle(bean.getName() +" "+
                context.getString(diffDay <= 0 ? R.string.matter_left_day : R.string.matter_already_day));

        //3.拼接目标日
        leftBean.setTarget(context.getString(R.string.matter_target)
                + getReminderTargetFormat(context, calendar));

        return leftBean;
    }

    public static String getReminderTargetFormat(Context context, Calendar calendar) {
        if (calendar == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(REMINDER_TARGET_FORMAT, Locale.getDefault());
        return format.format(calendar.getTime()) + " "
                + context.getResources().getStringArray(R.array.week_name)[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }
}
