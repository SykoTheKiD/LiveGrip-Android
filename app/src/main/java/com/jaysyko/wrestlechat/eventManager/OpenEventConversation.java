package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.content.Intent;

import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.activities.EventInfoActivity;
import com.jaysyko.wrestlechat.activities.MessagingActivity;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.Events;
import com.parse.ParseObject;

public class OpenEventConversation {

    public static void openConversation(ParseObject event, Context context) {
        com.jaysyko.wrestlechat.date.LiveStatus status = DateVerifier.goLive(event.getLong(Events.START_TIME), event.getLong(Events.END_TIME));
        if (status.goLive()) {
            CurrentActiveEvent.getInstance().setCurrentEvent(event);
            Intent intent = new Intent(context, MessagingActivity.class);
            context.startActivity(intent);
        } else {
            Dialog.makeToast(context, context.getString(status.getReason()));
        }
    }

    public static void openEventInfo(ParseObject event, Context context) {
        CurrentActiveEvent.getInstance().setCurrentEvent(event);
        Intent intent = new Intent(context, EventInfoActivity.class);
        context.startActivity(intent);
    }
}
