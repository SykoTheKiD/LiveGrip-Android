package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.jaysyko.wrestlechat.activities.EventInfoActivity;
import com.jaysyko.wrestlechat.activities.MessagingActivity;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.models.Event;

public class OpenEvent {



    public static void openConversation(final Event event, final Context context) {
        final boolean eventNotify = event.isNotify();
        com.jaysyko.wrestlechat.date.LiveStatus status = DateVerifier.goLive(event.getEventStartTime(), event.getEventEndTime());
        String notifyMessage = eventNotify ? "Cancel Notification" : "Set Notification";
        if (status.goLive()) {
            CurrentActiveEvent.getInstance().setCurrentEvent(event);
            Intent intent = new Intent(context, MessagingActivity.class);
            context.startActivity(intent);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(status.getReason());
            alertDialogBuilder.setMessage("The event has not started, select an action");

            alertDialogBuilder.setPositiveButton("See Event Info", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    openEventInfo(event, context);
                }
            });

            alertDialogBuilder.setNegativeButton(notifyMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(eventNotify){
                        Alarm.cancelAlarm(event, context);
                    }else{
                        Alarm.setAlarm(event, context);
                    }
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public static void openEventInfo(Event event, Context context) {
        CurrentActiveEvent.getInstance().setCurrentEvent(event);
        Intent intent = new Intent(context, EventInfoActivity.class);
        context.startActivity(intent);
    }
}
