package com.mobo.daymatter.beans;

public class PunchHistoryItem extends PunchClockRecorder {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static PunchHistoryItem copy(PunchClockRecorder recorder) {
        PunchHistoryItem item = new PunchHistoryItem();
        item.setTime(recorder.getTime());
        item.setUsedTime(recorder.getUsedTime());
        return item;
    }
}
