package com.mobo.daymatter.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.mobo.daymatter.utils.PunchClockUtils;

/**
 * 打卡信息，时间单位均为分钟
 */
public class PunchClockBean implements Parcelable {
    public static final int REPEAT_ONE_DAY = 0;
    public static final int REPEAT_ONE_WEEK = 1;
    public static final int REPEAT_ONE_TIMES = 2;
    public static final int REPEAT_FOREVER = 3;

    public static final int TIME_NONE = 2;
    public static final int TIME_UP = 1;
    public static final int TIME_DOWN = 0;

    private int time = 25; // 每次打卡时间长度,单位分钟
    private int timeMode; // 时间模式，点击直接算打卡、倒计时界面确认打卡、正计时几面打卡
    private String name; // 打卡名字
    private int colorPos; // 颜色位置
    private int bitmapPos; // 图片位置
    private int repeatMode; // 重复模式: 不重复、每天重复、每周重复、永远有效
    private PunchClockTargetMode targetMode = new PunchClockTargetMode();

    public PunchClockBean() {}

    protected PunchClockBean(Parcel in) {
        time = in.readInt();
        timeMode = in.readInt();
        name = in.readString();
        colorPos = in.readInt();
        bitmapPos = in.readInt();
        repeatMode = in.readInt();
        targetMode = in.readParcelable(PunchClockTargetMode.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(time);
        dest.writeInt(timeMode);
        dest.writeString(name);
        dest.writeInt(colorPos);
        dest.writeInt(bitmapPos);
        dest.writeInt(repeatMode);
        dest.writeParcelable(targetMode, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PunchClockBean> CREATOR = new Creator<PunchClockBean>() {
        @Override
        public PunchClockBean createFromParcel(Parcel in) {
            return new PunchClockBean(in);
        }

        @Override
        public PunchClockBean[] newArray(int size) {
            return new PunchClockBean[size];
        }
    };

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(int timeMode) {
        this.timeMode = timeMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColorPos() {
        return colorPos;
    }

    public void setColorPos(int colorPos) {
        this.colorPos = colorPos;
    }

    public int getBitmapPos() {
        return bitmapPos;
    }

    public void setBitmapPos(int bitmapPos) {
        this.bitmapPos = bitmapPos;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }

    public PunchClockTargetMode getTargetMode() {
        return targetMode;
    }

    public void setTargetMode(PunchClockTargetMode targetMode) {
        this.targetMode = targetMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PunchClockBean)) return false;
        PunchClockBean clockBean = (PunchClockBean) o;
        return getTime() == clockBean.getTime() &&
                getTimeMode() == clockBean.getTimeMode() &&
                getColorPos() == clockBean.getColorPos() &&
                getBitmapPos() == clockBean.getBitmapPos() &&
                getRepeatMode() == clockBean.getRepeatMode() &&
                getName().equals(clockBean.getName()) &&
                getTargetMode().equals(clockBean.getTargetMode());
    }

    @Override
    public int hashCode() {
        return targetMode.hashCode() + 31 * (name.hashCode() +
                31 * (time + 31 * (bitmapPos + 31 * (colorPos + 31 * (timeMode + 31 * repeatMode)))));
    }

    // 存储key值，用来标记记录
    public String getStoreKey() {
        String str = toString();
        if (repeatMode == REPEAT_ONE_WEEK) {
            str += PunchClockUtils.getStartWeek();
        } else {
            str += PunchClockUtils.getCurDay();
        }
        return str;
    }

    @NonNull
    @Override
    public String toString() {
        return targetMode.toString() + "-" + name + "-" +
                time + "-" + timeMode + "-" + bitmapPos + "-" +
                colorPos + "-" + repeatMode;
    }

    public static PunchClockBean copy(PunchClockBean bean) {
        PunchClockBean result = new PunchClockBean();
        result.time = bean.time;
        result.timeMode = bean.timeMode;
        result.name = bean.name;
        result.colorPos = bean.colorPos;
        result.bitmapPos = bean.bitmapPos;
        result.repeatMode = bean.repeatMode;
        result.targetMode = new PunchClockTargetMode();
        result.targetMode.setMode(bean.getTargetMode().getMode());
        result.targetMode.setCount(bean.getTargetMode().getCount());
        result.targetMode.setTimes(bean.getTargetMode().getTimes());
        return result;
    }
}
