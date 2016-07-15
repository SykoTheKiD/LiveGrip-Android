package com.jaysyko.wrestlechat.eventManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activities.EventInfoActivity;
import com.jaysyko.wrestlechat.activities.MessagingActivity;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.date.LiveStatus;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.Event;

public final class OpenEvent {

    public static void openConversation(final Event event, final Activity activity, final View view) {
        final boolean eventNotify = event.isNotify();
        final LiveStatus status = DateVerifier.goLive(event.getEventStartTime(), event.getEventEndTime());
        String notifyMessage = eventNotify ? "Cancel Notification" : "Set Notification";
        String alertHeader = (status.getReason() == R.string.event_over) ? " The event is over, select an action" : "The event has not started, select an action";
        if (status.goLive()) {
            CurrentActiveEvent.getInstance().setCurrentEvent(event);
            Intent intent = new Intent(activity, MessagingActivity.class);
            activity.startActivity(intent);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle(status.getReason());
            alertDialogBuilder.setMessage(alertHeader);

            alertDialogBuilder.setPositiveButton("See Event Info", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    openEventInfo(event, activity, view);
                }
            });

            alertDialogBuilder.setNegativeButton(notifyMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(status.getReason() == R.string.online_status_not_live){
                        if(eventNotify){
                            Alarm.cancelAlarm(event, activity);
                        }else{
                            Alarm.setAlarm(event, activity);
                        }
                    }
                    Dialog.makeToast(activity, activity.getString(R.string.event_over));
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public static void openEventInfo(Event event, Activity activity, View view) {
        CurrentActiveEvent.getInstance().setCurrentEvent(event);
        Intent intent = new Intent(activity, EventInfoActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                // the context of the activity
                activity,
                // For each shared element, add to this method a new Pair item,
                // which contains the reference of the view we are transitioning *from*,
                // and the value of the transitionName attribute
                new Pair<>(view.findViewById(R.id.event_image),
                        activity.getString(R.string.transition_name_circle)),
                new Pair<>(view.findViewById(R.id.event_title),
                        activity.getString(R.string.transition_name_name)),
                new Pair<>(view.findViewById(R.id.event_location),
                        activity.getString(R.string.transition_name_phone))
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }
}
