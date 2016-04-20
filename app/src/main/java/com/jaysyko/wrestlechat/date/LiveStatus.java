package com.jaysyko.wrestlechat.date;

import com.jaysyko.wrestlechat.R;

/**
 * LiveStatus.java
 * Returns a boolean as to whether or not an event should go live or a reason as to why not
 *
 * @author Jay Syko
 */
public final class LiveStatus {

    public static final int EVENT_NOT_STARTED = R.string.online_status_not_live;
    public static final int EVENT_OVER = R.string.event_over;
    public static final int EVENT_STARTED = 1;

    private boolean live;
    private int reason;


    public LiveStatus(boolean live, int reason) {
        this.live = live;
        this.reason = reason;
    }

    /**
     * Returns whether or not an event should go live
     * @return Boolean
     */
    public boolean goLive() {
        return live;
    }

    /**
     * Returns reason why an event should not go live
     * @return int corresponsing to string in strings.xml
     */
    public int getReason() {
        return reason;
    }
}
