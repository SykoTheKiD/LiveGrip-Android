package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.content.Intent;

import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.activities.EventInfoActivity;
import com.jaysyko.wrestlechat.activities.MessagingActivity;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.mqtt.MQTTConnection;

import org.eclipse.paho.client.mqttv3.MqttException;

public class OpenEvent {

    public static void openConversation(Event event, Context context) {
        com.jaysyko.wrestlechat.date.LiveStatus status = DateVerifier.goLive(event.getEventStartTime(), event.getEventEndTime());
        if (status.goLive()) {
            CurrentActiveEvent.getInstance().setCurrentEvent(event);
            try {
                MQTTConnection.getInstance().getClient().subscribe(event.getEventID());
            } catch (MqttException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(context, MessagingActivity.class);
            context.startActivity(intent);
        } else {
            Dialog.makeToast(context, context.getString(status.getReason()));
        }
    }

    public static void openEventInfo(Event event, Context context) {
        CurrentActiveEvent.getInstance().setCurrentEvent(event);
        Intent intent = new Intent(context, EventInfoActivity.class);
        context.startActivity(intent);
    }
}
