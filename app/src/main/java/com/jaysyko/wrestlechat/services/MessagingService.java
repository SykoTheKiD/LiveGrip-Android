package com.jaysyko.wrestlechat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.auth.UserKeys;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.models.Message;

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
 * MessagingService.java
 *
 * Implements the MQTT messaging system
 *
 * @author Jay Syko
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

    /**
     * Return an instance of the MQTT Android client
     *
     * @return MQTTAndroidClient
     */
    private MqttAndroidClient getClient() {
        return this.mClient;
    }

    /**
     * Returns a binder instance so public API can be used
     *
     * @param intent Connector intent
     * @return instance of the service binder
     */
    @Override
    public IBinder onBind(Intent intent) {
        connect();
        return this.mBinder;
    }


    /**
     * Connects the device the the MQTT Broker
     */
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

    /**
     * Method to alert when the connection to the broker is lost
     *
     * @param cause Cause for connection loss
     */
    @Override
    public void connectionLost(Throwable cause) {
        Log.i(TAG, "Connection Lost");
    }

    /**
     * Method that alerts when a new message has arrived
     *
     * @param topic Topic the message is from to ( chat event )
     * @param message The message that was sent to the topic
     * @throws Exception Catches JSON parse exceptions
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = message.toString();
        JSONObject messageJSON = new JSONObject(payload);
        Message newMessage = new Message(
                messageJSON.getString(Message.MessageJSONKeys.USERNAME.toString()),
                messageJSON.getString(Message.MessageJSONKeys.EVENT_NAME.toString()),
                messageJSON.getString(Message.MessageJSONKeys.BODY.toString()),
                messageJSON.getString(Message.MessageJSONKeys.PROFILE_IMAGE.toString())
        );
        mBinder.messageArrived(newMessage);
    }

    /**
     * Alerts when a message is successfully sent
     * @param token which is created on a successful delivery of a message
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i(TAG, "Delivered");
    }

    /**
     * Allows you to subscribe to an event and all the messages sent to it
     *
     */
    private void subscribe() {
        try {
            getClient().subscribe(room, 2, null, this);
        } catch (MqttException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Debug the connection
     * @param source of broker
     * @param message from broker
     */
    @Override
    public void traceDebug(String source, String message) {
        Log.d(TAG, source);
        Log.d(TAG, message);
    }

    @Override
    public void traceError(String source, String message) {

    }

    @Override
    public void traceException(String source, String message, Exception e) {

    }

    /**
     * API for a successful connection to broker
     * @param asyncActionToken Failure Token
     */
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        if (mIsConnecting) {
            mIsConnecting = false;
            subscribe();
        }
    }

    /**
     * API for when the connection to broker fails
     * @param asyncActionToken Token for broker connection fail
     * @param exception Reason for the failing to connect
     */
    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.e(TAG, "Error:".concat(exception.toString()));
    }

    /**
     * Send a message to a topic
     * @param body Body of message in JSON format that contains all the messages details
     */
    public void send(String body) {
        JSONObject payload = new JSONObject();
        CurrentActiveUser currentActiveUser = CurrentActiveUser.getCurrentUser();
        CurrentActiveEvent currentActiveEvent = CurrentActiveEvent.getInstance();
        try {
            payload.put(UserKeys.ID.toString(), currentActiveUser.getUserID());
            payload.put(Event.EventJSONKeys.ID.toString(), currentActiveEvent.getCurrentEvent().getEventID());
            payload.put(Message.MessageJSONKeys.BODY.toString(), body);
            payload.put(Event.EventJSONKeys.NAME.toString(), currentActiveEvent.getCurrentEvent().getEventName());
            payload.put(UserKeys.USERNAME.toString(), currentActiveUser.getUsername());
            payload.put(UserKeys.PROFILE_IMAGE.toString(), currentActiveUser.getCustomProfileImageURL());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        try {
            mClient.publish(room, payload.toString().getBytes(), 2, false, null, this);
        } catch (MqttException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Disconnects you from the MQTT Broker
     */
    public void disconnect() {
        try {
            this.mClient.disconnect();
            Log.e(TAG, "disconnected");
        } catch (MqttException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Disconnect Client from broker when service is destroyed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
    }
}
