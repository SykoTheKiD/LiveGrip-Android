package com.jaysyko.wrestlechat.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateVerifier {
    private static final String STRING_DATE_FORMAT = "EEE, MMM dd hh:mm a";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(STRING_DATE_FORMAT);
    private static final String TIME_ZONE = "UTC";

//    public static boolean goLive(String date) {
//        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
//        try {
//            Date dateObj = DATE_FORMAT.parse(date);
//            return goLive(dateObj.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    public static Live goLive(Long startTime, Long endTime) {
        // String UTC to Date object
        Date startTimeDate = new Date(startTime);
        Date endTimeDate = new Date(endTime);
        // get Current time in UTC in a Date Object
        Date currentTime = new Date();
        Boolean started = currentTime.before(startTimeDate);
        Boolean ended = currentTime.after(endTimeDate);
        if (started) {
            return new Live(false, Live.EVENT_NOT_STARTED);
        }
        if (ended) {
            return new Live(false, Live.EVENT_OVER);
        }
        return new Live(true, Live.EVENT_STARTED);
    }

    public static String format(Long millis) {
        Date date = new Date(millis);
        return DATE_FORMAT.format(date);
    }
}
