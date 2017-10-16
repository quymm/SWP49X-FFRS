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

    public static String formatDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(date);
    }

    public static String returnDayInWeek(Date date){
        SimpleDateFormat format = new SimpleDateFormat("EE");
        return format.format(date);
    }
}
