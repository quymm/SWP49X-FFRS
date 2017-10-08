package com.capstone.ffrs.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by HuanPMSE61860 on 10/6/2017.
 */

public class TimePickerListener implements View.OnClickListener {

    private Context context;
    private EditText edit;

    public TimePickerListener(Context context, EditText edit) {
        this.context = context;
        this.edit = edit;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        CustomTimePickerDialog mTimePicker;
        mTimePicker = new CustomTimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                edit.setText(selectedHour + ":" + (selectedMinute < 10 ? "0" + selectedMinute : selectedMinute));
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }
}
