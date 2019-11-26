package com.example.diary;

import android.os.Build;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AssistantUtil {
    static DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    public static String timeFormat(int hh, int mm) {
        String s = "";
        if (hh>12) {
            s += (hh%12);
            s += ":";
            s += mm >= 10? mm: ('0'+Integer.toString(mm)) ;
            s += "\nPM";
        } else {
            s += (hh%12);
            s += ":";
            s += mm >= 10? mm: ('0'+Integer.toString(mm));
            s += "\nAM";
        }
        return s;
    };

    public static String getTimeNow() {
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        return timeFormat(hour, minute);
    }

    public static String getDateNow() {
        Date date = new Date();
        return DATE_FORMATTER.format(date);
    }
}
