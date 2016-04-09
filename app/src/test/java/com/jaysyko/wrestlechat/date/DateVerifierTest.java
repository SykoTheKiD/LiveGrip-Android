package com.jaysyko.wrestlechat.date;

import com.jaysyko.wrestlechat.R;

import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

public class DateVerifierTest {

    @Test
    public void goLiveEventOverTest() {
        Long startTime = 1459556217000L;
        Long endTime = 1459729017000L;
        String start = new Timestamp(startTime).toString();
        String end = new Timestamp(endTime).toString();
        LiveStatus status = DateVerifier.goLive(start, end);
        boolean live = status.goLive();
        int reason = status.getReason();
        assertEquals(R.string.event_over, reason);
        assertEquals(false, live);
    }

    @Test
    public void goLiveEventNotStarted() {
        Long startTime = 1461457017000L;
        Long endTime = 1461629817000L;

        String start = new Timestamp(startTime).toString();
        String end = new Timestamp(endTime).toString();
        LiveStatus status = DateVerifier.goLive(start, end);
        boolean live = status.goLive();
        int reason = status.getReason();

        assertEquals(R.string.online_status_not_live, reason);
        assertEquals(false, live);
    }

    @Test
    public void goLiveEventStarted() {
        Long startTime = 1459903031000L;
        Long endTime = 1460767031000L;

        String start = new Timestamp(startTime).toString();
        String end = new Timestamp(endTime).toString();
        LiveStatus status = DateVerifier.goLive(start, end);
        boolean live = status.goLive();
        int reason = status.getReason();

        assertEquals(1, reason);
        assertEquals(true, live);
    }

}
