package com.team.boeboard.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHandler {

    //String类型时间转化成Date
    public Date StringToDate(String str_time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

        Date date = formatter.parse(str_time);

        return date;
    }

    /**
     * 两个date类型时间比较
     * date1小于date2返回-1，date1大于date2返回1，相等返回0
     */
    public int CompareTime(Date date1,Date date2){
        int result = date1.compareTo(date2);
        return result;
    }
}
