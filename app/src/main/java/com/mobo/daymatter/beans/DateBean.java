package com.mobo.daymatter.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Objects;

/**
 * 日期相关信息，区分是否公历、阴历、时间是否被调
 */
public class DateBean implements Parcelable {
    private int year;
    private int month;
    private int day;
    private int week; // 星期的索引

    public DateBean() {}

    protected DateBean(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        week = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(week);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DateBean> CREATOR = new Creator<DateBean>() {
        @Override
        public DateBean createFromParcel(Parcel in) {
            return new DateBean(in);
        }

        @Override
        public DateBean[] newArray(int size) {
            return new DateBean[size];
        }
    };

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void change(Calendar calendar) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateBean)) return false;
        DateBean dateBean = (DateBean) o;
        return getYear() == dateBean.getYear() &&
                getMonth() == dateBean.getMonth() &&
                getDay() == dateBean.getDay() &&
                getWeek() == dateBean.getWeek();
    }

    @Override
    public int hashCode() {
        return year + (day + (month + week * 31) * 31) * 31;
    }

    public static DateBean copy(DateBean bean) {
        DateBean newBean = new DateBean();
        newBean.year = bean.year;
        newBean.month = bean.month;
        newBean.day = bean.day;
        newBean.week = bean.week;
        return newBean;
    }
}
