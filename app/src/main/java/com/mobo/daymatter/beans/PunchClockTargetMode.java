package com.mobo.daymatter.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * 目标模式
 */
public class PunchClockTargetMode implements Parcelable {
    public static final int MODE_NONE = 2;
    public static final int MODE_COUNT = 0; // 次数
    public static final int MODE_TIMES = 1; // 时间，分钟为单位

    private int mode;
    private int count = 1;
    private int times = 25;

    public PunchClockTargetMode() {}

    protected PunchClockTargetMode(Parcel in) {
        mode = in.readInt();
        count = in.readInt();
        times = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mode);
        dest.writeInt(count);
        dest.writeInt(times);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PunchClockTargetMode> CREATOR = new Creator<PunchClockTargetMode>() {
        @Override
        public PunchClockTargetMode createFromParcel(Parcel in) {
            return new PunchClockTargetMode(in);
        }

        @Override
        public PunchClockTargetMode[] newArray(int size) {
            return new PunchClockTargetMode[size];
        }
    };

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PunchClockTargetMode)) return false;
        PunchClockTargetMode that = (PunchClockTargetMode) o;
        return getMode() == that.getMode() &&
                getCount() == that.getCount() &&
                getTimes() == that.getTimes();
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (mode == MODE_COUNT) {
            return count * 31;
        } else if (mode == MODE_TIMES) {
            return times;
        }
        return hashCode;
    }

    @NonNull
    @Override
    public String toString() {
        String str = "none";
        if (mode == MODE_COUNT) {
            return "count" + count;
        } else if (mode == MODE_TIMES) {
            return "times"+times;
        }
        return str;
    }
}
