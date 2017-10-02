package com.services;

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

        Date date = format.parse(abc);
        System.out.println(date);
        System.out.println(format.format(date));
        System.out.println(dayOfWeek.format(date));
    }
}
