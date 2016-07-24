package com.jaysyko.wrestlechat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jaysyko.wrestlechat.services.EventStartNotifierService;

public class EventStartReceiver extends BroadcastReceiver {
    public EventStartReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, EventStartNotifierService.class);
        context.startService(service);
    }
}
