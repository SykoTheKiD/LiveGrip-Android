package com.jaysyko.wrestlechat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.auth.UserJSONKeys;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.EventJSONKeys;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.models.MessageJSONKeys;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttTraceHandler;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jarushaan on 2016-03-14
 */
public class MessagingService extends Service implements MqttCallback, MqttTraceHandler, IMqttActionListener {
    public static final String TAG = MessagingService.class.getSimpleName();
    private static final String MQTT_BROKER_URL = "192.168.33.10";
    private static final String MQTT_BROKER_PORT = "8080";
    private static final String CLIENT_ID = CurrentActiveUser.getCurrentUser().getUsername();
    private static final String PROTOCOL = "tcp://";
    private static final String MOSQUITO_URL = PROTOCOL + MQTT_BROKER_URL + ":" + MQTT_BROKER_PORT;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int KEEP_ALIVE_INTERVAL = 600000;
    private static final String DUMMY_PASSWORD = "password";
    private final MessagingServiceBinder mBinder = new MessagingServiceBinder(this);
    private MqttAndroidClient mClient;
    private boolean mIsConnecting;
    private String room = CurrentActiveEvent.getInstance().getCurrentEvent().getEventID();

    private MqttAndroidClient getClient() {
        return mClient;
    }

    @Override
    public IBinder onBind(Intent intent) {
        connect();
        return mBinder;
    }

    private void connect() {
        mClient = new MqttAndroidClient(this, MOSQUITO_URL, CLIENT_ID);
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(false);
        connectOptions.setConnectionTimeout(CONNECTION_TIMEOUT);
        connectOptions.setKeepAliveInterval(KEEP_ALIVE_INTERVAL);
        connectOptions.setUserName(CLIENT_ID);
        connectOptions.setPassword(DUMMY_PASSWORD.toCharArray());
        mClient.setCallback(this);
        mClient.setTraceCallback(this);
        mIsConnecting = true;

        try {
            mClient.connect(connectOptions, null, this);
        } catch (MqttException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.i(TAG, cause.getMessage());
        Dialog.makeToast(getApplicationContext(), cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = message.toString();
        JSONObject messageJSON = new JSONObject(payload);
        Log.e(TAG, messageJSON.toString());
        Message newMessage = new Message(
                messageJSON.getString(MessageJSONKeys.USERNAME.toString()),
                messageJSON.getString(MessageJSONKeys.EVENT_NAME.toString()),
                messageJSON.getString(MessageJSONKeys.BODY.toString()),
                messageJSON.getString(MessageJSONKeys.PROFILE_IMAGE.toString())
        );
        mBinder.messageArrived(newMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    private void subscribe() {
        try {
            getClient().subscribe(room, 2, null, this);
        } catch (MqttException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void traceDebug(String source, String message) {

    }

    @Override
    public void traceError(String source, String message) {

    }

    @Override
    public void traceException(String source, String message, Exception e) {

    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        if (mIsConnecting) {
            mIsConnecting = false;
            subscribe();
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.e(TAG, "Error:".concat(exception.toString()));
    }

    public void send(String body) {
        JSONObject payload = new JSONObject();
        CurrentActiveUser currentActiveUser = CurrentActiveUser.getCurrentUser();
        CurrentActiveEvent currentActiveEvent = CurrentActiveEvent.getInstance();
        try {
            payload.put(UserJSONKeys.ID.toString(), currentActiveUser.getUserID());
            payload.put(EventJSONKeys.ID.toString(), currentActiveEvent.getCurrentEvent().getEventID());
            payload.put(MessageJSONKeys.BODY.toString(), body);
            payload.put(EventJSONKeys.NAME.toString(), currentActiveEvent.getCurrentEvent().getEventName());
            payload.put(UserJSONKeys.USERNAME.toString(), currentActiveUser.getUsername());
            payload.put(UserJSONKeys.PROFILE_IMAGE.toString(), currentActiveUser.getCustomProfileImageURL());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        try {
            mClient.publish(room, payload.toString().getBytes(), 2, false, null, this);
        } catch (MqttException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
