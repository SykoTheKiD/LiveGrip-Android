package com.jaysyko.wrestlechat.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activities.LoginActivity;
import com.jaysyko.wrestlechat.utils.ImageTools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Listener for GCM Messages
 * @author Jay Syko
 */
public class GCMListenerService extends GcmListenerService {
    private static final String TAG = GCMListenerService.class.getSimpleName();
    private static final String MESSAGE = "message";
    private static final String TITLE = "title";
    private static final String TICKER_TEXT = "tickerText";
    private static final String URL = "url";
    private static final String SMALL = "small";
    private static final String NOTIFICATION_RECEIVED = "Notification received";
    private static final int NOTIFICATION_ID = 11221;

    /**
     * Alerts when a GCM message was received
     *
     * @param from GCM Server
     * @param data data that was passed in the GCM message
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.i(TAG, NOTIFICATION_RECEIVED);

        String message = data.getString(MESSAGE);
        String title = data.getString(TITLE);
        String tickerText = data.getString(TICKER_TEXT);
        String url = data.getString(URL);
        boolean small = data.getBoolean(SMALL);

        // Set an notification image
        Bitmap smallIconBitmap = getBitmapFromURL(url);
        if (small) {
            showSimpleNotifications(message, title, tickerText, smallIconBitmap);
        } else {
            showBigPictureStyleNotifications(message, title, tickerText, smallIconBitmap, smallIconBitmap);
        }
    }

    /**
     * Grab the Bitmap from the URL provided in the GCM message
     * @param strURL URL to image (.jpg or .png only)
     * @return a Bitmap of the Image in the URL
     */
    Bitmap getBitmapFromURL(String strURL) {
        if (ImageTools.isLinkToImage(strURL)) {
            try {
                URL url = new URL(strURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return null;
    }

    /**
     * Set notification type to a large image in the Notification menus
     * @param message Main Notification title
     * @param title Byline under main title
     * @param tickerText LockScreen text
     * @param smallIcon App Icon
     * @param largeIcon Large Image Header
     */
    public void showBigPictureStyleNotifications(String message, String title, String tickerText, Bitmap smallIcon,
                                                 Bitmap largeIcon) {
        //Create notification object and set the content.
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setSmallIcon(R.drawable.logo);

        nb.setLargeIcon(smallIcon);
        nb.setContentTitle(title);
        nb.setContentText(message);
        nb.setTicker(tickerText);

        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(largeIcon);
        s.setSummaryText(message);
        nb.setStyle(s);

        Intent resultIntent = new Intent(this, LoginActivity.class);
        TaskStackBuilder TSB = TaskStackBuilder.create(this);
        TSB.addParentStack(LoginActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        TSB.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                TSB.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, nb.build());
    }

    /**
     * Show a small bar notification
     * @param message Main Notification title
     * @param title Byline under main title
     * @param tickerText LockScreen text
     * @param smallIcon App Icon
     */
    public void showSimpleNotifications(String message, String title, String tickerText, Bitmap smallIcon) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.logo);
        notificationBuilder.setLargeIcon(smallIcon);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setTicker(tickerText);

        NotificationCompat.BigTextStyle bigTextNotification = new NotificationCompat.BigTextStyle();
        bigTextNotification.bigText(message);
        bigTextNotification.setBigContentTitle(title);
        bigTextNotification.setSummaryText(getString(R.string.app_name));
        notificationBuilder.setStyle(bigTextNotification);

        Intent resultIntent = new Intent(this, LoginActivity.class);
        TaskStackBuilder TSB = TaskStackBuilder.create(this);
        TSB.addParentStack(LoginActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        TSB.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                TSB.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
