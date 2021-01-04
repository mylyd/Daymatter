package com.mobo.daymatter.beans.drinkrecord;

/**
 * @author : ydli
 * @time : 20-5-28 上午11:13
 * @description 喝水周期 @喝水量
 */
public class RecordProgressBean {
    //当前年月日 例 20200528 组成一个唯一id
    private String recordTime;
    //喝水量
    private int recordProgress;

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public int getRecordProgress() {
        return recordProgress;
    }

    public void setRecordProgress(int recordProgress) {
        this.recordProgress = recordProgress;
    }

    public RecordProgressBean(String recordTime, int recordProgress) {
        this.recordTime = recordTime;
        this.recordProgress = recordProgress;
    }
}
