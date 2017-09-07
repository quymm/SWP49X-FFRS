package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by stark on 09/06/2017.
 */
public class DateUtils {
    public static boolean isSameDay(Date d1, Date d2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(d1).equals(fmt.format(d2));
    }

    public static boolean isSameDay(String d1, String d2) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dFrom = formatter.parse(d1);
        Date dTo = formatter.parse(d2);
        return isSameDay(dFrom, dTo);
    }

    public static Date addMinutesToDate(int minutes, Date date) {
        final long ONE_MINUTE_IN_MILLIS = 60000; //millisecs

        long curTimeInMs = date.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    public static Date minusMinutesToDate(int minutes, Date date) {
        final long ONE_MINUTE_IN_MILLIS = 60000; //millisecs

        long curTimeInMs = date.getTime();
        Date afterAddingMins = new Date(curTimeInMs - (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
    public static Date stringToDate(String date, String pattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        Date result = formatter.parse(date);
        return result;
    }

    public static Date today() {
        Calendar date = new GregorianCalendar();

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }

    public static Date tomorrow() {
        Calendar date = new GregorianCalendar();

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        date.add(Calendar.DAY_OF_MONTH, 1);
        return date.getTime();
    }

}
