package com.mobo.daymatter.beans.drinkrecord;

/**
 * @author : ydli
 * @time : 20-5-28 上午11:13
 * @description 喝水周期 @喝水次数
 */
public class RecordNumBean {
    //当前年月 例 202005 组成一个唯一id （次数以月为单位）
    private String recordTime;
    //喝水时间点
    private long recordCurrentTimeMillis;

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public long getRecordNum() {
        return recordCurrentTimeMillis;
    }

    public void setRecordNum(int recordNum) {
        this.recordCurrentTimeMillis = recordNum;
    }

    public RecordNumBean(String recordTime, long currentTimeMillis) {
        this.recordTime = recordTime;
        this.recordCurrentTimeMillis = currentTimeMillis;
    }
}
