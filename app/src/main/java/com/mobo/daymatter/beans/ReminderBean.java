package com.mobo.daymatter.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Objects;

/**
 * 纪念日数据
 */
public class ReminderBean implements Parcelable {
    private boolean isDefault;//是否是预置数据
    private String name;
    private long originalTime;//创建时的初始提醒时间
    private long reminderTime;//提醒时间
    private boolean isGregorian = true; // 默认是公历
    private boolean isNeedTop = false; // 是否需要放置最顶端
    private int repeatMode;

    public ReminderBean() {
        this.isDefault = false;
    }

    protected ReminderBean(Parcel in) {
        name = in.readString();
        originalTime = in.readLong();
        reminderTime = in.readLong();
        isDefault = in.readByte() != 0;
        isGregorian = in.readByte() != 0;
        isNeedTop = in.readByte() != 0;
        repeatMode = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(originalTime);
        dest.writeLong(reminderTime);
        dest.writeByte((byte) (isDefault ? 1 : 0));
        dest.writeByte((byte) (isGregorian ? 1 : 0));
        dest.writeByte((byte) (isNeedTop ? 1 : 0));
        dest.writeInt(repeatMode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReminderBean> CREATOR = new Creator<ReminderBean>() {
        @Override
        public ReminderBean createFromParcel(Parcel in) {
            return new ReminderBean(in);
        }

        @Override
        public ReminderBean[] newArray(int size) {
            return new ReminderBean[size];
        }
    };

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOriginalTime() {
        return originalTime;
    }

    public void setOriginalTime(long originalTime) {
        this.originalTime = originalTime;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean isGregorian() {
        return isGregorian;
    }

    public void setGregorian(boolean gregorian) {
        isGregorian = gregorian;
    }

    public boolean isNeedTop() {
        return isNeedTop;
    }

    public void setNeedTop(boolean needTop) {
        isNeedTop = needTop;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(int repeatMode) {
        if (this.repeatMode == repeatMode) return;
        this.repeatMode = repeatMode;
        // 重复模式更改后将提醒时间重置为初始时间，在第一次显示提醒时间时会重新计算正确的提醒时间
        this.reminderTime = this.originalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReminderBean)) return false;
        ReminderBean that = (ReminderBean) o;
        return isGregorian() == that.isGregorian() &&
                getName().equals(that.getName()) &&
                getOriginalTime() == that.getOriginalTime();
    }

    @Override
    public int hashCode() {
        return name.hashCode() + Long.valueOf(originalTime).hashCode() + Boolean.valueOf(isGregorian).hashCode();
    }

    public static ReminderBean copy(ReminderBean bean) {
        ReminderBean newBean = new ReminderBean();
        newBean.name = bean.name;
        newBean.originalTime = bean.originalTime;
        newBean.reminderTime = bean.reminderTime;
        newBean.isGregorian = bean.isGregorian;
        newBean.repeatMode = bean.repeatMode;
        newBean.isNeedTop = bean.isNeedTop;
        newBean.isDefault = bean.isDefault;
        return newBean;
    }
}
