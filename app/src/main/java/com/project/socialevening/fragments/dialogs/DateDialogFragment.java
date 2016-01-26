package com.project.socialevening.fragments.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by nitin on 27/08/15.
 */
public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Date minDate;

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public DateSetListener getListener() {
        return listener;
    }

    public void setListener(DateSetListener listener) {
        this.listener = listener;
    }

    DateSetListener listener;

    public static interface DateSetListener {
        public void onDateSet(int year, int month, int day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        pYear = year;
        pDay = day;
        pMonth = month;
        if (null != listener) {
            listener.onDateSet(year, month, day);
        }
    }

    int pYear;
    int pDay;
    int pMonth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        if (null != minDate) {
            c.setTime(minDate);
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
}
