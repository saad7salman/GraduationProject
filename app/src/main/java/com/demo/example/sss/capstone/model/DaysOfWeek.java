package com.demo.example.sss.capstone.model;

/**
 * Created by SSS on 3/6/18.
 */

public class DaysOfWeek {

    public String day,fromTime,toTime;
    public boolean isClosed;
    public String provID;
    public DaysOfWeek() {
    }

    public DaysOfWeek(String day, String fromTime, String toTime, boolean isClosed) {
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.isClosed = isClosed;
    }
    public DaysOfWeek(String day, String fromTime, String toTime, boolean isClosed,String provID) {
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.isClosed = isClosed;
        this.provID = provID;
    }
}
