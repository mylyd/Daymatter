package com.mobo.daymatter.beans.drinkrecord;

/**
 * @author : ydli
 * @time : 20-5-28 上午11:13
 * @description 喝水周期 @喝水目标
 */
public class RecordTotalBean {
    //当前年月日 例 20200528 组成一个唯一id
    private String recordTime;
    //喝水总量
    private int recordWater;

    public RecordTotalBean(String recordTime, int recordWater) {
        this.recordTime = recordTime;
        this.recordWater = recordWater;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public int getRecordWater() {
        return recordWater;
    }

    public void setRecordWater(int recordWater) {
        this.recordWater = recordWater;
    }
}
