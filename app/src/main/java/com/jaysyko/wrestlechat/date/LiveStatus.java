package com.jaysyko.wrestlechat.date;

import com.jaysyko.wrestlechat.R;

public class LiveStatus {

    public static final int EVENT_NOT_STARTED = R.string.online_status_not_live;
    public static final int EVENT_OVER = R.string.event_over;
    public static final int EVENT_STARTED = 1;

    private boolean live;
    private int reason;


    public LiveStatus(boolean live, int reason) {
        this.live = live;
        this.reason = reason;
    }

    public boolean goLive() {
        return live;
    }

    public int getReason() {
        return reason;
    }
}
