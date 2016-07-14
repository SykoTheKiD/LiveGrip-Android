package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activities.EventInfoActivity;
import com.jaysyko.wrestlechat.activities.MessagingActivity;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.Event;

public final class OpenEvent {

    public static void openConversation(final Event event, final Context context) {
        final boolean eventNotify = event.isNotify();
        final com.jaysyko.wrestlechat.date.LiveStatus status = DateVerifier.goLive(event.getEventStartTime(), event.getEventEndTime());
        String notifyMessage = eventNotify ? "Cancel Notification" : "Set Notification";
        String alertHeader = (status.getReason() == R.string.event_over) ? " The event is over, select an action" : "The event has not started, select an action";
        if (status.goLive()) {
            CurrentActiveEvent.getInstance().setCurrentEvent(event);
            Intent intent = new Intent(context, MessagingActivity.class);
            context.startActivity(intent);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(status.getReason());
            alertDialogBuilder.setMessage(alertHeader);

            alertDialogBuilder.setPositiveButton("See Event Info", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    openEventInfo(event, context);
                }
            });

            alertDialogBuilder.setNegativeButton(notifyMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(status.getReason() == R.string.online_status_not_live){
                        if(eventNotify){
                            Alarm.cancelAlarm(event, context);
                        }else{
                            Alarm.setAlarm(event, context);
                        }
                    }
                    Dialog.makeToast(context, context.getString(R.string.event_over));
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
