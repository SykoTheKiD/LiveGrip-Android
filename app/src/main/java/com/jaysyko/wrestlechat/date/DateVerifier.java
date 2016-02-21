package com.jaysyko.wrestlechat.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DateVerifier.java
 * Tools to check whether event should go live
 *
 * @author Jay Syko
 */

public class DateVerifier {
    private static final String STRING_DATE_FORMAT = "EEE, MMM dd hh:mm a";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.CANADA);

    /**
     * Returns a LiveStatus object stating whether or not we should go live with an event
     * @param startTime Long
     * @param endTime Long
     * @return LiveStatus
     */
    public static LiveStatus goLive(Long startTime, Long endTime) {
        // String UTC to Date object
        Date startTimeDate = new Date(startTime);
        Date endTimeDate = new Date(endTime);
        // get Current time in UTC in a Date Object
        Date currentTime = new Date();
        Boolean started = currentTime.before(startTimeDate);
        Boolean ended = currentTime.after(endTimeDate);
        if (started) {
            return new LiveStatus(false, LiveStatus.EVENT_NOT_STARTED);
        }
        if (ended) {
            return new LiveStatus(false, LiveStatus.EVENT_OVER);
        }
        return new LiveStatus(true, LiveStatus.EVENT_STARTED);
    }

    /**
     * Format a Millis time to a Human Readable date
     * @param millis Long
     * @return date: String
     */
    public static String format(Long millis) {
        Date date = new Date(millis);
        return DATE_FORMAT.format(date);
    }
}


//    private static final String TIME_ZONE = "UTC";

//    public static Long goLive(String date) {
//        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
//        try {
//            Date dateObj = DATE_FORMAT.parse(date);
//            return goLive(dateObj.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }