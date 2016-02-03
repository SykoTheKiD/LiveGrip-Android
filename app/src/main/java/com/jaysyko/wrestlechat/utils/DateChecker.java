package com.jaysyko.wrestlechat.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateChecker {
    private static final String STRING_DATE_FORMAT = "EEE, dd MMM HH:mm a";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(STRING_DATE_FORMAT);
    private static final String TIME_ZONE = "UTC";

    public static boolean goLive(String date) {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        try {
            Date dateObj = DATE_FORMAT.parse(date);
            return goLive(dateObj.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean goLive(Long millis) {
        // String UTC to Date object
        Date date = new Date(millis);
        // get Current time in UTC in a Date Object
        Date currentTime = new Date();
        // return currentTime.after(string date)
        return date.after(currentTime);
    }

    public static String format(Long millis) {
        Date date = new Date(millis);
        return DATE_FORMAT.format(date);
    }
}
