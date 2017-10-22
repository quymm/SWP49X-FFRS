package com.capstone.ffrs.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.capstone.ffrs.entity.FieldTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by HuanPMSE61860 on 10/6/2017.
 */

public class TimePickerListener implements View.OnClickListener {

    private Context context;
    private EditText edit;
    private Date minTime, maxTime;

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

    @Override
    public void onClick(View v) {
        CustomTimePickerDialog mTimePickerDialog;
        int hour;
        int minute;
        if (minTime != null) {
            hour = minTime.getHours();
            minute = minTime.getMinutes();
        } else {
            Calendar mcurrentTime = Calendar.getInstance();
            if (!edit.getText().toString().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                Date time = null;
                try {
                    time = sdf.parse(edit.getText().toString());
                } catch (ParseException e) {
                    Log.d("Exception", e.getMessage());
                }
                mcurrentTime.setTime(time);
            }
            hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            minute = mcurrentTime.get(Calendar.MINUTE);
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
