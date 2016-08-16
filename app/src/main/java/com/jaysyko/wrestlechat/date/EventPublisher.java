package com.jaysyko.wrestlechat.date;

import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private static final String UTC = "UTC";
    private static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final TimeZone DEFAULT_TIMEZONE = null;

    /**
     * Returns a LiveStatus object stating whether or not we should go live with an event
     * @param startTime Long
     * @param endTime Long
     * @return LiveStatus
     */
    public static EventStatus goLive(String startTime, String endTime) {
        SimpleDateFormat formatter = new SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone(UTC));
        Date startTimeDate, endTimeDate;
        try {
            EventStatus eventStatus;
            startTimeDate = formatter.parse(startTime);
            endTimeDate = formatter.parse(endTime);
            TimeZone.setDefault(TimeZone.getTimeZone(UTC));
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC));
            Date currentTime = calendar.getTime();
            Boolean notStarted = currentTime.before(startTimeDate);
            Boolean ended = currentTime.after(endTimeDate);
            if (notStarted) {
                eventStatus =  new EventStatus(false, EventStatus.EVENT_NOT_STARTED);
            }else if (ended) {
                eventStatus = new EventStatus(false, EventStatus.EVENT_OVER);
            }else{
                eventStatus = new EventStatus(true, EventStatus.EVENT_STARTED);
            }
            TimeZone.setDefault(DEFAULT_TIMEZONE);
            return eventStatus;
        } catch (ParseException e) {
            eLog.e(TAG, e.getMessage());
            return new EventStatus(false, EventStatus.EVENT_NOT_STARTED);
        }
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
            TimeZone.setDefault(DEFAULT_TIMEZONE);
            DATE_FORMAT.setTimeZone(TimeZone.getDefault());
            formattedTime = DATE_FORMAT.format(value);
        } catch (ParseException e) {
            eLog.e(TAG, e.getMessage());
        }
        return formattedTime;
    }
}