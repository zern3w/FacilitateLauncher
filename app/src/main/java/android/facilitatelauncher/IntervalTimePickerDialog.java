package android.facilitatelauncher;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zern3w on 8/3/2018 AD.
 */

public class IntervalTimePickerDialog extends TimePickerDialog {
    private static final String TAG = "IntervalPickerDialog";

    private final static int TIME_PICKER_INTERVAL = 15;
    private TimePicker timePicker;
    private final OnTimeSetListener callback;

    private int lastHour = -1;
    private int lastMinute = -1;

    public IntervalTimePickerDialog(Context context, int themeId, OnTimeSetListener callBack,
                                    int hourOfDay, int minute, boolean is24HourView) {
        super(context, themeId, callBack, hourOfDay, minute / TIME_PICKER_INTERVAL,
                is24HourView);
        lastHour = hourOfDay;
        lastMinute = minute;
        this.callback = callBack;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == -2) return;
        if (callback != null && timePicker != null) {
            timePicker.clearFocus();
            callback.onTimeSet(timePicker, timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
        }
    }

    @Override
    protected void onStop() {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            this.timePicker = (TimePicker) findViewById(timePickerField.getInt(null));
            Field field = classForid.getField("minute");

            NumberPicker mMinuteSpinner = (NumberPicker) timePicker.findViewById(field.getInt(null));
            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            mMinuteSpinner.setDisplayedValues(displayedValues.toArray(new String[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        super.onTimeChanged(view, hourOfDay, minute);
        if (lastHour != hourOfDay && lastMinute != minute) {
            view.setCurrentHour(lastHour);
            lastMinute = minute;
        } else {
            lastHour = hourOfDay;
            lastMinute = minute;
        }
    }
}