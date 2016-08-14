package com.jaysyko.wrestlechat.eventManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activities.EventInfoActivity;
import com.jaysyko.wrestlechat.activities.MessagingActivity;
import com.jaysyko.wrestlechat.activities.VideoFeedActivity;
import com.jaysyko.wrestlechat.analytics.MessagingTracker;
import com.jaysyko.wrestlechat.date.EventPublisher;
import com.jaysyko.wrestlechat.date.EventStatus;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.Event;

public final class OpenEvent {
    private static final Handler handler = new Handler();
    public static Event openConversation(final Event event, final Activity activity) {
        final boolean eventNotify = event.isNotify();
        final EventStatus status = EventPublisher.goLive(event.getEventStartTime(), event.getEventEndTime());
        String notifyMessage = eventNotify ? "Cancel Notification" : "Set Notification";
        String alertHeader = (status.getReason() == R.string.event_over) ? " The event is over, select an action" : "The event has not started, select an action";
        handler.post(new Runnable() {
            @Override
            public void run() {
                MessagingTracker.trackEvent(event);
            }
        });
        if (status.goLive()) {
            Intent intent;
            CurrentActiveEvent.getInstance().setCurrentEvent(event);
            if(event.getVideo() != null){
                intent = new Intent(activity, VideoFeedActivity.class);
            }else{
                intent = new Intent(activity, MessagingActivity.class);
            }
            activity.startActivity(intent);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle(status.getReason());
            alertDialogBuilder.setMessage(alertHeader);

            alertDialogBuilder.setPositiveButton("See Event Info", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    openEventInfo(event, activity);
                }
            });

            alertDialogBuilder.setNegativeButton(notifyMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(status.getReason() == R.string.online_status_not_live){
                        if(eventNotify){
                            event.setNotify(false);
                            Alarm.cancelAlarm(event, activity);
                        }else{
                            event.setNotify(true);
                            Alarm.setAlarm(event, activity);
                        }
                    }else{
                        Dialog.makeToast(activity, activity.getString(R.string.event_over));
                    }
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return event;
    }

    public static void openEventInfo(Event event, Activity activity) {
        CurrentActiveEvent.getInstance().setCurrentEvent(event);
        Intent intent = new Intent(activity, EventInfoActivity.class);
        activity.startActivity(intent);
    }
}
