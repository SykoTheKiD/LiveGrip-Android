package com.jaysyko.wrestlechat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class EventStartNotifierService extends Service {
    public EventStartNotifierService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
