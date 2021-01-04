package com.mobo.daymatter.manager;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobo.daymatter.beans.DateBean;
import com.mobo.daymatter.beans.ReminderBean;
import com.mobo.daymatter.beans.RepeatMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 管理提醒数据
 */
public class ReminderManager {
    private static final String REMINDER_DATA = "reminder_data";
    private ArrayList<ReminderBean> sReminderList;

    public static ReminderManager get() {
        return Inner.sInstance;
    }

    public ReminderManager init(Context context) {
        if (sReminderList == null) {
            SharedPreferenceManager.getInstance().init(context);
            String data = SharedPreferenceManager.getInstance().getString(REMINDER_DATA, "");
            if (!TextUtils.isEmpty(data)) {
                sReminderList = new Gson().fromJson(data, new TypeToken<ArrayList<ReminderBean>>() {
                }.getType());
            } else {
                sReminderList = new ArrayList<>();
            }
        }
        return this;
    }

    /**
     * 获取提醒列表
     *
     * @return
     */
    public List<ReminderBean> getReminderList() {
        if (sReminderList.isEmpty()) {
            ReminderBean bean = new ReminderBean();
            bean.setName("Saturday");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2020);
            calendar.set(Calendar.MONTH, 3);
            calendar.set(Calendar.DAY_OF_MONTH, 25);
            bean.setOriginalTime(calendar.getTimeInMillis());
            bean.setReminderTime(calendar.getTimeInMillis());
            bean.setRepeatMode(RepeatMode.ONE_WEEK);
            bean.setDefault(true);
            sReminderList.add(bean);
        }
        return sReminderList;
    }

    /**
     * 增加一个提醒
     *
     * @param bean
     */
    public void addReminder(ReminderBean bean) {
        if (bean != null) {
            if (bean.isNeedTop()) {
                sReminderList.add(0, bean);
            } else {
                sReminderList.add(bean);
            }
            SharedPreferenceManager.getInstance().putStringAndCommit(REMINDER_DATA,
                    new Gson().toJson(sReminderList, new TypeToken<ArrayList<ReminderBean>>() {
                    }.getType()));
        }
    }

    /**
     * 删除一个提醒
     *
     * @param bean
     */
    public void deleteReminder(ReminderBean bean) {
        if (delete(bean)) {
            SharedPreferenceManager.getInstance().putStringAndCommit(REMINDER_DATA,
                    new Gson().toJson(sReminderList, new TypeToken<ArrayList<ReminderBean>>() {
                    }.getType()));
        }
    }

    private boolean delete(ReminderBean bean) {
        if (bean != null) {
            int removeIndex = -1;
            for (int i = 0; i < sReminderList.size(); i++) {
                if (sReminderList.get(i).equals(bean)) {
                    removeIndex = i;
                    break;
                }
            }
            if (removeIndex != -1) {
                sReminderList.remove(removeIndex);
                return true;
            }
        }
        return false;
    }

    /**
     * 修改；由于数据之间变动，没有关联；所以提供旧的，和新的
     *
     * @param oldBean 被编辑前数据
     * @param newBean 被编辑后数据
     */
    public void editReminder(ReminderBean oldBean, ReminderBean newBean) {
        delete(oldBean);
        addReminder(newBean);
    }


    private static class Inner {
        private static final ReminderManager sInstance = new ReminderManager();
    }
}
