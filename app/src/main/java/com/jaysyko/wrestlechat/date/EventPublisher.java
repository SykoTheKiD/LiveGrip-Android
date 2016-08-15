package com.jaysyko.wrestlechat.date;

import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * DateVerifier.java
 * Tools to check whether event should go live
 *
 * @author Jay Syko
 */

public class EventPublisher {
    private static final String TAG = EventPublisher.class.getSimpleName();
    private static final String STRING_DATE_FORMAT = "EEE, MMM dd hh:mm a";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(STRING_DATE_FORMAT, Locale.getDefault());
    public static final String UTC = "UTC";
    public static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Returns a LiveStatus object stating whether or not we should go live with an event
     * @param startTime Long
     * @param endTime Long
     * @return LiveStatus
     */
    public static EventStatus goLive(String startTime, String endTime) {
        Date startTimeDate = Timestamp.valueOf(startTime);
        Date endTimeDate = Timestamp.valueOf(endTime);
        eLog.i(TAG, startTimeDate.toString());
        eLog.i(TAG, endTimeDate.toString());
        // String UTC to Date object
        Date currentTime = new Date();
        Boolean notStarted = currentTime.before(startTimeDate);
        Boolean ended = currentTime.after(endTimeDate);
        if (notStarted) {
            eLog.i(TAG, "Not Started");
            return new EventStatus(false, EventStatus.EVENT_NOT_STARTED);
        }
        if (ended) {
            eLog.i(TAG, "Over");
            return new EventStatus(false, EventStatus.EVENT_OVER);
        }
        eLog.i(TAG, "STARTED");
        return new EventStatus(true, EventStatus.EVENT_STARTED);
    }

    /**
     * Format a Millis time to a Human Readable date
     * @param stringDate String
     * @return date: String
     */
    public static String format(String stringDate) {
        String formattedTime = StringResources.NULL_TEXT;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault());
            formatter.setTimeZone(TimeZone.getTimeZone(UTC));
            Date value = formatter.parse(stringDate);

            DATE_FORMAT.setTimeZone(TimeZone.getDefault());
            formattedTime = DATE_FORMAT.format(value);
            eLog.i(TAG, formattedTime);
        } catch (ParseException e) {
            eLog.e(TAG, e.getMessage());
        }
        return formattedTime;
    }
}