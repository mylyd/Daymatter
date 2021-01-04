package com.mobo.daymatter.beans;

/**
 * 打卡记录，以打卡信息+时间为key值区分
 */
public class PunchClockRecorder {
    private long time; // 打卡时间
    private int usedTime; // 这次打卡消耗时间，以分钟为单位

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
    }
}
