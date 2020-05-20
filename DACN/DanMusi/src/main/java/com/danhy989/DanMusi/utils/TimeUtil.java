package com.danhy989.DanMusi.utils;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public static Date addSecondToCurrentDate(int second){
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND,second);
        return calendar.getTime();
    }
}
