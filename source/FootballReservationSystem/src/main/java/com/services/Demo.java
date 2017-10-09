package com.services;

import com.utils.DateTimeUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Demo {
    public static void main(String[] args) throws IOException, ParseException {
        int ch;
        String abc = "";
        System.out.print("Input date ");
        while((ch = System.in.read()) != '\n'){
            abc += (char) ch;
        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dayOfWeek = new SimpleDateFormat("EE");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

//        Date date = DateTimeUtils.convertFromStringToDate(abc);
        Date time = DateTimeUtils.convertFromStringToTime(abc);
        System.out.println(time);
        System.out.println(time.getTime());
        Date time2 = new Date(time.getTime() + 60000);
        System.out.println(timeFormat.format(time));
        System.out.println((int) (time2.getTime()-time.getTime())/3000);
    }
}
