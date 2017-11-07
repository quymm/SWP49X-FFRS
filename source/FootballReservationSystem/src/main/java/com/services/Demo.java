package com.services;

import com.dto.CordinationPoint;
import com.utils.DateTimeUtils;
import com.utils.MapUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Demo {
    public static void main(String[] args) throws IOException, ParseException {
//        int ch;
//        String abc = "";
//        System.out.print("Input date ");
//        while((ch = System.in.read()) != '\n'){
//            abc += (char) ch;
//        }
//        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//        SimpleDateFormat dayOfWeek = new SimpleDateFormat("EE");
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
//        String b = "10.8567888";
//        Double bDouble = Double.parseDouble(b);

//        CordinationPoint cordinationPoint1 = new CordinationPoint();
//        cordinationPoint1.setLongitude("10.797507");
//        cordinationPoint1.setLatitude("106.675181");
//
//        CordinationPoint cordinationPoint2 = new CordinationPoint();
//        cordinationPoint2.setLongitude("10.796088");
//        cordinationPoint2.setLatitude("106.677798");

//        System.out.println(MapUtils.calculateDistanceBetweenTwoPoint(cordinationPoint1, cordinationPoint2));

//        Date date = DateTimeUtils.convertFromStringToDate(abc);
//        Date dateTime = DateTimeUtils.convertFromStringToDateTime(abc);
//        System.out.println(bDouble);
//        System.out.println(format.format(dateTime));
//        System.out.println(timeFormat.format(dateTime));
        double a = 3.6;
        System.out.println((int)Math.rint(3.3));
    }
}
