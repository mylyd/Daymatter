package com.mobo.daymatter.beans;

/**
 * @author : ydli
 * @time : 20-5-26 下午6:19
 * @description 喝水记录管理bean
 */
public class DrinkBean {
    private long time;//喝水时间 ,//亦用于排序使用，最新记录排在最上面
    private int value;//喝水量
    private int total;//喝水时设置的容量
    private int capacity;//喝水总量
    private String unit;//单位

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public DrinkBean(long time, int value, int total, int capacity, String unit) {
        this.time = time;
        this.value = value;
        this.total = total;
        this.capacity = capacity;
        this.unit = unit;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
