package com.capstone.ffrs.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.capstone.ffrs.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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

    CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                  int hourOfDay, int minute, boolean is24HourView) {
        super(context, R.style.TimePickerTheme, null, hourOfDay,
                minute / TIME_PICKER_INTERVAL, is24HourView);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            // Fix TimePicker spinner mode error in API 24
            fixTimePickerNougat(context, hourOfDay, minute, is24HourView);
        }

        mTimeSetListener = listener;

        lastSavedHour = hourOfDay;
        lastSavedMinute = minute / TIME_PICKER_INTERVAL;
    }

    void setMinTime(Date minTime) {
        this.minTime = minTime;
    }

    void setMaxTime(Date maxTime) {
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
                    Intent intent = new Intent("time-picker-message");
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
                validateTimePicker(view, hourOfDay);
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
            mHourSpinner.setWrapSelectorWheel(false);

            validateTimePicker(mTimePicker, lastSavedHour);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateTimePicker(TimePicker view, int hourOfDay) {
        if (minTime != null && hourOfDay <= minTime.getHours()) {
            if (minTime.getMinutes() == 0) {
                setMinutePicker(false, false);
                return;
            } else if (minTime.getMinutes() == 30) {
                lastSavedMinute = 1;
                view.setCurrentMinute(1);
                setMinutePicker(true, false);
                return;
            }
        }
        if (maxTime != null && hourOfDay >= maxTime.getHours()) {
            if (maxTime.getMinutes() == 0) {
                lastSavedMinute = 0;
                view.setCurrentMinute(0);
                setMinutePicker(false, true);
                return;
            } else if (maxTime.getMinutes() == 30) {
                setMinutePicker(false, false);
                return;
            }
        }
        setMinutePicker(false, false);
    }

    private void setMinutePicker(boolean isMinThirty, boolean isMaxZero) {
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");

            Field field = classForid.getField("minute");
            NumberPicker minuteSpinner = (NumberPicker) mTimePicker
                    .findViewById(field.getInt(null));
            List<String> displayedValues = new ArrayList<>();
            if (minTime != null && maxTime != null && minTime.getHours() == maxTime.getHours() && minTime.getMinutes() == maxTime.getMinutes()) {
                minuteSpinner.setDisplayedValues(null);
                minuteSpinner.setMinValue(minTime.getMinutes() / TIME_PICKER_INTERVAL);
                minuteSpinner.setMaxValue(minTime.getMinutes() / TIME_PICKER_INTERVAL);
                displayedValues.add(String.format("%02d", minTime.getMinutes()));
            } else if (isMinThirty) {
                minuteSpinner.setDisplayedValues(null);
                minuteSpinner.setMinValue(1);
                minuteSpinner.setMaxValue(1);
                displayedValues.add(String.format("%02d", 30));
            } else if (isMaxZero) {
                minuteSpinner.setDisplayedValues(null);
                minuteSpinner.setMinValue(0);
                minuteSpinner.setMaxValue(0);
                displayedValues.add(String.format("%02d", 0));
            } else {
                minuteSpinner.setDisplayedValues(null);
                minuteSpinner.setMinValue(0);
                minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
                for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                    displayedValues.add(String.format("%02d", i));
                }
            }
            minuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[displayedValues.size()]));
            minuteSpinner.setWrapSelectorWheel(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fixTimePickerNougat(Context context, int hourOfDay, int minute, boolean is24HourView) {
        try {
            final Field field = this.findField(
                    TimePickerDialog.class,
                    TimePicker.class,
                    "mTimePicker"
            );
            mTimePicker = (TimePicker) field.get(this);
            final Class<?> delegateClass = Class.forName("android.widget.TimePicker$TimePickerDelegate");
            final Field delegateField = this.findField(
                    TimePicker.class,
                    delegateClass,
                    "mDelegate"
            );

            final Object delegate = delegateField.get(mTimePicker);
            final Class<?> spinnerDelegateClass = Class.forName("android.widget.TimePickerSpinnerDelegate");

            if (delegate.getClass() != spinnerDelegateClass) {
                delegateField.set(mTimePicker, null);
                mTimePicker.removeAllViews();

                final Constructor spinnerDelegateConstructor =
                        spinnerDelegateClass.getDeclaredConstructor(
                                TimePicker.class,
                                Context.class,
                                AttributeSet.class,
                                int.class,
                                int.class
                        );
                spinnerDelegateConstructor.setAccessible(true);

                final Object spinnerDelegate = spinnerDelegateConstructor.newInstance(
                        mTimePicker,
                        context,
                        null,
                        android.R.attr.timePickerStyle,
                        0
                );
                delegateField.set(mTimePicker, spinnerDelegate);

                mTimePicker.setIs24HourView(is24HourView);
                mTimePicker.setCurrentHour(hourOfDay);
                mTimePicker.setCurrentMinute(minute);
                mTimePicker.setOnTimeChangedListener(this);
            }
        } catch (Exception e) { /* Do nothing */ }
    }

    private Field findField(Class objectClass, Class fieldClass, String expectedName) {
        try {
            final Field field = objectClass.getDeclaredField(expectedName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) { /* Ignore */ }

        // Search for it if it wasn't found under the expectedName.
        for (final Field field : objectClass.getDeclaredFields()) {
            if (field.getType() == fieldClass) {
                field.setAccessible(true);
                return field;
            }
        }

        return null;
    }
}