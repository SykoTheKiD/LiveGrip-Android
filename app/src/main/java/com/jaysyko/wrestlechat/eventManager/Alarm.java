package com.jaysyko.wrestlechat.eventManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.receivers.EventStartReceiver;

import java.sql.Timestamp;

/**
 * @author Jay Syko on 2016-07-12.
 */
class Alarm {
    private static final int FACTOR = 1000000;
    private static final String TAG = Alarm.class.getSimpleName();
    static void setAlarm(Event event, Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Timestamp timestamp = Timestamp.valueOf(event.getEventStartTime());
        long milliseconds = timestamp.getTime() + (timestamp.getNanos() / FACTOR);
        Intent myIntent = new Intent(context, EventStartReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC, milliseconds, pendingIntent);
        NotifyListStore.getInstance().add(event, context);
        event.setNotify(true);
        eLog.i(TAG, "Alarm Set for " + event.toString());
    }

    static void cancelAlarm(Event event, Context context){
        NotifyListStore.getInstance().remove(event, context);
        eLog.i(TAG, "Alarm cancelled for " + event.toString());
    }

    static void cancelAlarm(PendingIntent intent, Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(intent);
        eLog.i(TAG, "Alarm Cancelled for Intent");
    }
}
