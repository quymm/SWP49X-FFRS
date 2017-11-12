package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author MinhQuy
 */
public class DateTimeUtils {
    public static Date convertFromStringToTime(String timeString){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//        format.setTimeZone(TimeZone.getTimeZone("UTC+7"));
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
//        format.setTimeZone(TimeZone.getTimeZone("UTC+7"));
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
//        format.setTimeZone(TimeZone.getTimeZone("UTC+7"));
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
//        format.setTimeZone(TimeZone.getTimeZone("UTC+7"));
        return format.format(time);
    }

    public static String formatDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//        format.setTimeZone(TimeZone.getTimeZone("UTC+7"));
        return format.format(date);
    }

    public static String returnDayInWeek(Date date){
        SimpleDateFormat format = new SimpleDateFormat("EE");
        return format.format(date);
    }

    public static int timeToIntInTimeSlot(Date time){
        String sTime = formatTime(time);
        String[] parts = sTime.split(":");
        Integer hh = Integer.parseInt(parts[0]);
        Integer mm = Integer.parseInt(parts[1]);
        return hh*2 + (mm==30?1:0);
    }

    public static Date intToTimeInTimeSlot(int iTime){
        int hh = iTime/2;
        int mm = iTime%2;
        String shh = hh<10?"0"+Integer.toString(hh):Integer.toString(hh);
        String smm = mm==1?"30":"00";
        return convertFromStringToTime(shh+":"+smm);
    }
}
