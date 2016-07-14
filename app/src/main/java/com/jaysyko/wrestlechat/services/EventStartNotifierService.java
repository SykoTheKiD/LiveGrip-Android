package com.jaysyko.wrestlechat.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activities.LoginActivity;

public class EventStartNotifierService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationManager mManager = (NotificationManager) this.getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);

        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingNotificationIntent);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        mManager.notify(0, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
