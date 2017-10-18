package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author MinhQuy
 */
public class DateTimeUtils {
    public static Date convertFromStringToTime(String timeString){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date time = format.parse(timeString);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date convertFromStringToDate(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date convertFromStringToDateTime(String dateTimeString){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            Date dateTime = format.parse(dateTimeString);
            return dateTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatTime(Date time){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(time);
    }

    public static String returnDayInWeek(Date date){
        SimpleDateFormat format = new SimpleDateFormat("EE");
        return format.format(date);
    }

    public static int timeConvert (Date time){
        String[] s = time.toString().split(":", 3);
//        String hour = s[0];
//        String minute = s[1];
        int hour = Integer.parseInt(s[0])*2 + (Integer.parseInt(s[1]) == 30 ? 1:0);
        return hour;
    }
}
