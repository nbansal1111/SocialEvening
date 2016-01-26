package com.project.socialevening.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by nitin on 27/08/15.
 */
public class TimePickerDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {
    TimeChangeListener timeChangeListener;

    public TimeChangeListener getTimeChangeListener() {
        return timeChangeListener;
    }

    public void setTimeChangeListener(TimeChangeListener timeChangeListener) {
        this.timeChangeListener = timeChangeListener;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Log.d("Time", "" + i + ":" + i1);
        timeChangeListener.onTimeChanged(timePicker, i, i1);
    }

    public static interface TimeChangeListener {
        public void onTimeChanged(TimePicker timePicker, int i, int i1);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        return new android.app.TimePickerDialog(getActivity(), this, hour, min, true);
    }
}
