package com.mobo.daymatter.manager;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.mobo.daymatter.R;
import com.mobo.daymatter.activities.DayMatterActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 进行新增提醒
 */
public class NotificationManager {
    public static final String IS_FROM_NOTIFICATION = "is_from_notification";
    private static final String NOTIFICATION_SWITCH_STATUS = "notification_switch_status";
    private static final String NOTIFICATION_DAY = "NOTIFICATION_DAY";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private int notificationId = 1;
    private android.app.NotificationManager manager;

    private NotificationManager() {
    }

    public static NotificationManager get() {
        return Inner.sManager;
    }

    public boolean isNeedNotification() {
        if (!isNotificationOn()) {
            return false;
        }
        String date = SharedPreferenceManager.getInstance().getString(NOTIFICATION_DAY, "");
        if (TextUtils.isEmpty(date)) {
            return true;
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return !TextUtils.equals(date, format.format(calendar.getTime()));
    }

    public void setTodayNoticed() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        SharedPreferenceManager.getInstance().putStringAndCommit(NOTIFICATION_DAY, format.format(calendar.getTime()));
    }

    public boolean isNotificationOn() {
        return SharedPreferenceManager.getInstance().getBool(NOTIFICATION_SWITCH_STATUS, true);
    }

    public void saveNotificationStatus(boolean isOn) {
        SharedPreferenceManager.getInstance().putBoolAndCommit(NOTIFICATION_SWITCH_STATUS, isOn);
    }

    public void showNotification(Context context, String title, String content) {
        PendingIntent intent = createPendingIntent(context, Notification.FLAG_AUTO_CANCEL, DayMatterActivity.class);
        showNotification(context, title, content, intent);
    }

    public void showNotification(Context context, String title, String content, PendingIntent intent) {
        Bitmap iconBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_start_icon);
        showNotification(context, title, content, iconBitmap, intent);
    }

    /**
     * 喝水功能推送
     *
     * @param context
     */
    public void showNotification(Context context) {
        if (!isNotificationOn()) {
            return;
        }
        PendingIntent intent = createPendingIntent(context, DrinkManager.NOTIFICATION_ITEM_ID,
                Notification.FLAG_AUTO_CANCEL, DayMatterActivity.class);
        if (manager == null) {
            manager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        showNotification(context, intent);
    }

    /**
     * 喝水功能推送
     *
     * @param context
     */
    public void showNotification(Context context, android.app.NotificationManager manager) {
        if (!isNotificationOn()) {
            return;
        }
        PendingIntent intent = createPendingIntent(context, DrinkManager.NOTIFICATION_ITEM_ID,
                Notification.FLAG_AUTO_CANCEL, DayMatterActivity.class);
        if (manager != null) {
            this.manager = manager;
        }
        showNotification(context, intent);
    }

    /**
     * 喝水功能推送
     *
     * @param context
     * @param intent
     */
    public void showNotification(Context context, PendingIntent intent) {
        showNotification(context, String.format(context.getString(R.string.drinking_water_value),
                DrinkManager.getDrinkProgress(), DrinkManager.getDrinkTotal(),
                DrinkManager.getUnitType() ? DrinkManager.UNIT_ML : DrinkManager.UNIT_FLOZ), intent);
    }

    /**
     * 喝水功能推送
     *
     * @param context
     * @param remoteContent
     * @param intent
     */
    public void showNotification(Context context, String remoteContent, PendingIntent intent) {
        String appName = context.getString(R.string.app_name);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, appName);
        builder.setContentTitle(appName)//设置通知栏标题
                .setContentText(remoteContent) //设置通知栏显示内容
                //.setNumber(number) //设置通知集合的数量
                .setTicker(context.getString(R.string.drinking)) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_MAX) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_start_icon));
        if (intent != null) {
            builder.setContentIntent(intent); //设置通知栏点击意图
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// 8.0及以上
            createNotificationChannel(manager, appName, appName, android.app.NotificationManager.IMPORTANCE_DEFAULT);
        }
        manager.notify(DrinkManager.NOTIFICATION_ID, builder.build());
    }

    public void showNotification(Context context, String title, String content,
                                 Bitmap iconBitmap, PendingIntent intent) {
        String appName = context.getString(R.string.app_name);
        android.app.NotificationManager nm = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, appName);
        mBuilder.setContentTitle(appName)//设置通知栏标题
                .setContentText(content) //设置通知栏显示内容
                //.setNumber(number) //设置通知集合的数量
                .setTicker(title) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_start_icon)//设置通知小ICON
                //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_home_3d));
                .setLargeIcon(iconBitmap);
        if (intent != null) {
            mBuilder.setContentIntent(intent); //设置通知栏点击意图
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// 8.0及以上
            createNotificationChannel(nm, appName, appName, android.app.NotificationManager.IMPORTANCE_DEFAULT);
        }
        nm.notify(notificationId, mBuilder.build());
        notificationId++;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(android.app.NotificationManager notificationManager, String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        notificationManager.createNotificationChannel(channel);
    }

    public PendingIntent createPendingIntent(Context context, int flags, Class<?> activity) {
        if (activity == null) {
            return null;
        }
        Intent intent = new Intent();
        intent.putExtra(IS_FROM_NOTIFICATION, true);
        intent.setClass(context, activity);
        return PendingIntent.getActivity(context, 1, intent, flags);
    }

    public PendingIntent createPendingIntent(Context context, String item, int flags, Class<?> activity) {
        if (activity == null) {
            return null;
        }
        Intent intent = new Intent();
        intent.putExtra(item, true);
        intent.setClass(context, activity);
        return PendingIntent.getActivity(context, DrinkManager.NOTIFICATION_ID, intent, flags);
    }

    private static final class Inner {
        private static final NotificationManager sManager = new NotificationManager();
    }
}
