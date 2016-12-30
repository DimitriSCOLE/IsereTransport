package com.mazzone.isere.transport.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;

import java.util.Locale;

public class DateUtil {

    public static final String TIME_PATTERN = "HH:mm";

    public static DateTime minutesToDateTime(int minutes, String timezoneId) {
        //Get Current Date
        MutableDateTime dateTimeNow = MutableDateTime.now();
        dateTimeNow.setZone(DateTimeZone.forID(timezoneId));
        dateTimeNow.setTime(0);
        dateTimeNow.addMinutes(minutes);
        return dateTimeNow.toDateTime();
    }

    public static DateTime nowWithoutSecondMinusOneMinute() {
        MutableDateTime dateTimeNow = MutableDateTime.now();
        dateTimeNow.setSecondOfMinute(0);
        dateTimeNow.setMillisOfSecond(0);
        dateTimeNow.addMinutes(-1);
        return dateTimeNow.toDateTime();
    }

    public static String dateToString(DateTime dateTime, String pattern) {
        MutableDateTime m = dateTime.toMutableDateTime();
        m.setZone(DateTimeZone.getDefault());
        return m.toString(pattern, Locale.getDefault());
    }
}
