package com.jaysyko.wrestlechat.date;

import java.sql.Timestamp;
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

    private static Date toMillisDate(Timestamp timestamp) {
        long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
        return new java.util.Date(milliseconds);
    }

    /**
     * Returns a LiveStatus object stating whether or not we should go live with an event
     * @param startTime Long
     * @param endTime Long
     * @return LiveStatus
     */
    public static LiveStatus goLive(String startTime, String endTime) {
        // String UTC to Date object
        Date startTimeDate = toMillisDate(Timestamp.valueOf(startTime));
        Date endTimeDate = toMillisDate(Timestamp.valueOf(endTime));
        // get Current time in UTC in a Date Object
        Date currentTime = new Date();
        Boolean notStarted = currentTime.before(startTimeDate);
        Boolean ended = currentTime.after(endTimeDate);
        if (notStarted) {
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