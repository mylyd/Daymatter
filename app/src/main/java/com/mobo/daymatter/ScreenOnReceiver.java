package com.mobo.daymatter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.mobo.daymatter.beans.LeftBean;
import com.mobo.daymatter.beans.PunchClockBean;
import com.mobo.daymatter.beans.PunchClockRecorder;
import com.mobo.daymatter.beans.PunchClockTargetMode;
import com.mobo.daymatter.beans.ReminderBean;
import com.mobo.daymatter.manager.NotificationManager;
import com.mobo.daymatter.manager.PunchClockManager;
import com.mobo.daymatter.manager.ReminderManager;
import com.mobo.daymatter.tracker.FirebaseTracker;
import com.mobo.daymatter.tracker.MyTracker;
import com.mobo.daymatter.utils.MoboLogger;
import com.mobo.daymatter.utils.ReminderUtil;

import java.util.ArrayList;
import java.util.List;

public class ScreenOnReceiver extends BroadcastReceiver {
    private static ScreenOnReceiver mReceiver;
    private Context mContext;
    private Handler mHandler;

    public ScreenOnReceiver(Context mContext) {
        this.mContext = mContext;
        mHandler = new Handler(mContext.getMainLooper());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        MoboLogger.debug("DayNotificationManager", "screen is on!");
        if (NotificationManager.get().isNeedNotification()) {
            showReminder();
            showPunch();
        }
    }

    private void showPunch() {
        List<PunchClockBean> punchClockBeans = PunchClockManager.get().getDetailData();
        if (punchClockBeans == null || punchClockBeans.isEmpty()) {
            return;
        }
        String title = mContext.getString(R.string.matter_tab_record);
        String content;
        int alreadyCount = 0;
        for (PunchClockBean bean : punchClockBeans) {
            if (bean.getTargetMode().getMode() == PunchClockTargetMode.MODE_NONE) {
                continue;
            }
            ArrayList<PunchClockRecorder> recorders = PunchClockManager.get().getRecorderByMode(bean);
            if (recorders == null || recorders.isEmpty()) {
                alreadyCount = 0;
            } else {
                alreadyCount = recorders.size();
            }
            content = null;
            if (bean.getTargetMode().getMode() == PunchClockTargetMode.MODE_TIMES) {
                alreadyCount = bean.getTargetMode().getTimes() - alreadyCount * bean.getTime();
                if (alreadyCount > 0) {
                    content = mContext.getString(R.string.matter_punch_notification_minute, bean.getTargetMode().getTimes());
                }
            } else {
                alreadyCount = bean.getTargetMode().getCount() - alreadyCount;
                if (alreadyCount > 0) {
                    content = mContext.getString(R.string.matter_punch_notification, bean.getTargetMode().getCount());
                }
            }
            if (content != null) {
                MoboLogger.debug("DayNotificationManager", "punch title =" + title + ", content =" + content);
                FirebaseTracker.get().track(MyTracker.EVENT_PUSH_REMIND_SHOW);
                NotificationManager.get().setTodayNoticed();
                NotificationManager.get().showNotification(mContext, title, content);
            }
        }
    }

    private void showReminder() {
        List<ReminderBean> reminderBeans = ReminderManager.get().getReminderList();
        if (reminderBeans == null || reminderBeans.isEmpty()) {
            return;
        }
        LeftBean temp;
        String title = mContext.getString(R.string.matter_tab_reminder);
        String unit = mContext.getString(R.string.matter_day);
        for (ReminderBean bean : reminderBeans) {
            temp = ReminderUtil.getLeftBean(mContext, bean);
            if (temp.getDiffDay() == 0 && temp.getDiffDay() == 1) {
                MoboLogger.debug("DayNotificationManager", "reminder title =" + title);
                FirebaseTracker.get().track(MyTracker.EVENT_PUSH_REMIND_SHOW);
                NotificationManager.get().setTodayNoticed();
                NotificationManager.get().showNotification(mContext, title,
                        temp.getTitle() + temp.getDay() + unit);
            }
        }
    }

    public static void registerScreenOn(Context context) {
        if (context != null) {
            if (mReceiver == null) {
                mReceiver = new ScreenOnReceiver(context);
            }
            context.registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        }
    }


    public static void unRegisterScreenOn(Context context) {
        if (context != null) {
            context.unregisterReceiver(mReceiver);
        }
    }
}
