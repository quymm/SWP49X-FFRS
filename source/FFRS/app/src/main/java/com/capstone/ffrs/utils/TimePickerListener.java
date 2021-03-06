package com.capstone.ffrs.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by HuanPMSE61860 on 10/6/2017.
 */

public class TimePickerListener implements View.OnClickListener {

    private Context context;
    private EditText edit;
    private Date minTime, maxTime, date;

    public TimePickerListener(Context context, EditText edit) {
        this.context = context;
        this.edit = edit;
    }

    public Date getMinTime() {
        return minTime;
    }

    public void setMinTime(Date minTime) {
        this.minTime = minTime;
    }

    public Date getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Date maxTime) {
        this.maxTime = maxTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void onClick(View v) {
        CustomTimePickerDialog mTimePickerDialog;
        int hour = -1;
        int minute = -1;
        Calendar mCurrentTime = Calendar.getInstance();
        if (!edit.getText().toString().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            Date time = null;
            try {
                time = sdf.parse(edit.getText().toString());
            } catch (ParseException e) {
                Log.d("Exception", e.getMessage());
            }
            mCurrentTime.setTime(time);
            hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
            minute = mCurrentTime.get(Calendar.MINUTE);
        } else if (date != null) {
            if (date.getDate() == new Date().getDate()) {
                if (minTime != null || maxTime != null) {
                    LocalTime time = LocalTime.now();
                    if (minTime != null) {
                        LocalTime minLocalTime = LocalDateTime.fromDateFields(minTime).toLocalTime();
                        if (time.isBefore(minLocalTime)) {
                            hour = minTime.getHours();
                            minute = minTime.getMinutes();
                        } else {
                            hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                            minute = mCurrentTime.get(Calendar.MINUTE);
                        }
                    }
                    if (maxTime != null) {
                        LocalTime maxLocalTime = LocalDateTime.fromDateFields(maxTime).toLocalTime();
                        if (time.isAfter(maxLocalTime)) {
                            hour = maxTime.getHours();
                            minute = maxTime.getMinutes();
                        } else {
                            hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                            minute = mCurrentTime.get(Calendar.MINUTE);
                        }
                    }
                } else {
                    hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                    minute = mCurrentTime.get(Calendar.MINUTE);
                }
            } else {
                if (minTime != null) {
                    hour = minTime.getHours();
                    minute = minTime.getMinutes();
                } else {
                    hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                    minute = mCurrentTime.get(Calendar.MINUTE);
                }
            }
        }

        mTimePickerDialog = new CustomTimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                edit.setText(selectedHour + ":" + (selectedMinute < 10 ? "0" + selectedMinute : selectedMinute));
            }
        }, hour, minute, true);
        if (minTime != null) {
            mTimePickerDialog.setMinTime(minTime);
        }
        if (maxTime != null) {
            mTimePickerDialog.setMaxTime(maxTime);
        }
        mTimePickerDialog.setTitle("Chọn giờ");

        mTimePickerDialog.show();

    }
}
