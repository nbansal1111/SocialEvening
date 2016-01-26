package com.project.socialevening.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nitin on 26/01/16.
 */
public class DateTimeUtil {

    public static Date getDate(int additionalAmount, int fieldType) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(fieldType, additionalAmount);
        return c.getTime();
    }

    public static Date getDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date date = c.getTime();
        return date;
    }

    public static Date getTime(int hour, int min) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        c.add(Calendar.HOUR_OF_DAY, -c.get(Calendar.HOUR_OF_DAY));
        c.add(Calendar.MINUTE, -c.get(Calendar.MINUTE));
        c.add(Calendar.SECOND, -c.get(Calendar.SECOND));
        c.add(Calendar.HOUR_OF_DAY, hour);
        c.add(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static String getDateString(Date date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.toString();
    }

    public static String getTimeString(Date date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.toString();
    }
}
