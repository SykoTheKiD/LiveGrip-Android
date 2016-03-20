package com.jaysyko.wrestlechat.services.chatStream;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.utils.DBConstants;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by jarushaan on 2016-03-14
 */
public class ChatStream extends Service implements MqttCallback {
    public static final String TAG = ChatStream.class.getSimpleName();
    private final IBinder mBinder = new ChatStreamBinder(this);
    private MqttClient client;
    private Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate()");
        intent = new Intent(TAG);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind()");
        return mBinder;
    }

    public MqttClient connect() {
        Log.e(TAG, "connect()");
        try {
            client = new MqttClient(DBConstants.MQTT_BROKER_URL, CurrentActiveUser.getInstance().getUsername());
            client.connect();
            client.setCallback(this);
        } catch (MqttException e) {
            Log.e(TAG, e.getMessage());
        }
        return this.client;
    }

    @Override
    public void connectionLost(Throwable cause) {
        Dialog.makeToast(getApplicationContext(), cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.e(TAG, topic);
        Log.e(TAG, message.toString());
//        Message messageObject = new Message();
//        intent.putExtra(IntentKeys.MESSAGE_BROADCAST, messageObject);
//        sendBroadcast(intent);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public void send(Message message) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBody().getBytes());
        try {
            this.client.publish(CurrentActiveEvent.getInstance().getCurrentEvent().getEventID(), mqttMessage);
        } catch (MqttException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void subscribe(String room) {
        try {
            this.client.subscribe(room);
        } catch (MqttException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
