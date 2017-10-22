package com.capstone.ffrs.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomTimePickerDialog extends TimePickerDialog {

    private final static int TIME_PICKER_INTERVAL = 30;
    private TimePicker mTimePicker;
    private final OnTimeSetListener mTimeSetListener;
    private int lastSavedHour;
    private int lastSavedMinute;
    private Date minTime, maxTime;


    public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                  int hourOfDay, int minute, boolean is24HourView) {
        super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay,
                minute / TIME_PICKER_INTERVAL, is24HourView);
        mTimeSetListener = listener;

        lastSavedHour = hourOfDay;
        lastSavedMinute = minute / TIME_PICKER_INTERVAL;
    }

    public void setMinTime(Date minTime) {
        this.minTime = minTime;
        lastSavedHour = minTime.getHours();
        lastSavedMinute = minTime.getMinutes() / TIME_PICKER_INTERVAL;
    }

    public void setMaxTime(Date maxTime) {
        this.maxTime = maxTime;
    }

    @Override
    public void updateTime(int hourOfDay, int minuteOfHour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTimePicker.setHour(hourOfDay);
            mTimePicker.setMinute(minuteOfHour / TIME_PICKER_INTERVAL);
        } else {
            mTimePicker.setCurrentHour(hourOfDay);
            mTimePicker.setCurrentMinute(minuteOfHour / TIME_PICKER_INTERVAL);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case BUTTON_POSITIVE:
                if (mTimeSetListener != null) {
                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                    Intent intent = new Intent("timepicker-message");
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        Log.d("LASTHOUR", lastSavedHour + "");
        Log.d("HOUR", hourOfDay + "");
        Log.d("LASTMINUTE", lastSavedMinute + "");
        Log.d("MINUTE", minute + "");
        // Fix on 30 minute intervals time picker
        if (lastSavedHour != hourOfDay) {
            if (lastSavedMinute != minute) {
                lastSavedMinute = minute;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.setHour(lastSavedHour);
                    view.setMinute(lastSavedMinute);
                } else {
                    view.setCurrentHour(lastSavedHour);
                    view.setCurrentMinute(lastSavedMinute);
                }
            } else {
                lastSavedHour = hourOfDay;
            }
        }
        lastSavedMinute = minute;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
            mTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

            Field hourField = classForid.getField("hour");

            NumberPicker mHourSpinner = (NumberPicker) mTimePicker
                    .findViewById(hourField.getInt(null));

            Calendar calendar = Calendar.getInstance();
            if (minTime != null) {
                calendar.setTime(minTime);
                mHourSpinner.setMinValue(calendar.get(Calendar.HOUR_OF_DAY));
            }
            if (maxTime != null) {
                calendar.setTime(maxTime);
                mHourSpinner.setMaxValue(calendar.get(Calendar.HOUR_OF_DAY));
            }

            Field field = classForid.getField("minute");
            NumberPicker minuteSpinner = (NumberPicker) mTimePicker
                    .findViewById(field.getInt(null));
            minuteSpinner.setMinValue(0);
            minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[displayedValues.size()]));
            minuteSpinner.setWrapSelectorWheel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}