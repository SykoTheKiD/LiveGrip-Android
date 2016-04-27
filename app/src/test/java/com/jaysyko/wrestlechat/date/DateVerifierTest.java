package com.jaysyko.wrestlechat.date;

import com.jaysyko.wrestlechat.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateVerifierTest {

    @Test
    public void goLiveEventOverTest() {
        String start = "2010-04-16 04:08:17";
        String end = "2011-04-16 04:08:20";
        LiveStatus status = DateVerifier.goLive(start, end);
        boolean live = status.goLive();
        int reason = status.getReason();
        assertEquals(R.string.event_over, reason);
        assertEquals(false, live);
    }

    @Test
    public void goLiveEventNotStarted() {
        String start = "2018-04-16 04:08:17";
        String end = "2019-04-16 04:08:20";
        LiveStatus status = DateVerifier.goLive(start, end);
        boolean live = status.goLive();
        int reason = status.getReason();

        assertEquals(R.string.online_status_not_live, reason);
        assertEquals(false, live);
    }

    @Test
    public void goLiveEventStarted() {

        String start = "2015-04-16 04:08:17";
        String end = "2017-04-16 04:08:20";
        LiveStatus status = DateVerifier.goLive(start, end);
        boolean live = status.goLive();
        int reason = status.getReason();

        assertEquals(1, reason);
        assertEquals(true, live);
    }

}
