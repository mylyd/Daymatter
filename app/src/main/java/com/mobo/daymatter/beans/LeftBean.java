package com.mobo.daymatter.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 对纪念日还剩余或者过去多少天的一个描述
 */
public class LeftBean implements Parcelable {
    private String title; //描述
    private int day; // 天数，正数
    private int diffDay; // 天数，过期了就是负数
    private String target; // 目标描述

    protected LeftBean(Parcel in) {
        title = in.readString();
        day = in.readInt();
        diffDay = in.readInt();
        target = in.readString();
    }

    public LeftBean() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(day);
        dest.writeInt(diffDay);
        dest.writeString(target);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LeftBean> CREATOR = new Creator<LeftBean>() {
        @Override
        public LeftBean createFromParcel(Parcel in) {
            return new LeftBean(in);
        }

        @Override
        public LeftBean[] newArray(int size) {
            return new LeftBean[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDiffDay() {
        return diffDay;
    }

    public void setDiffDay(int diffDay) {
        this.diffDay = diffDay;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
