package com.jaysyko.wrestlechat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.eventManager.CurrentActiveEvent;
import com.jaysyko.wrestlechat.eventManager.Messenger;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.network.BaseURL;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;

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
 * @author Jay Syko
 */
public class MessagingService extends Service implements MqttCallback, MqttTraceHandler, IMqttActionListener {

    public static final String PROFILE_IMAGE = "profile_image", DUMMY_PASSWORD = "password", USERNAME = "username", BODY = "body", ID = "id", NAME = "name";
    static final int QOS = 1;
    private static final String TAG = MessagingService.class.getSimpleName();
    private static final String CLIENT_ID = String.valueOf(SessionManager.getCurrentUser().getId());
    private static final String MOSQUITO_URL = BaseURL.getMosquittoURL();
    private static final int CONNECTION_TIMEOUT = 10000, KEEP_ALIVE_INTERVAL = 600000;
    private final MessagingServiceBinder mBinder = new MessagingServiceBinder(this);
    private MqttAndroidClient mClient;
    private boolean mIsConnecting;
    private int room = CurrentActiveEvent.getInstance().getCurrentEvent().getEventID();

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
            eLog.e(TAG, e.getMessage());
        }

    }

    /**
     * Method to alert when the connection to the broker is lost
     *
     * @param cause Cause for connection loss
     */
    @Override
    public void connectionLost(Throwable cause) {
        eLog.i(TAG, "Connection Lost");
    }

    /**
     * Method that alerts when a new message has arrived
     *
     * @param topic   Topic the message is from to ( chat event )
     * @param message The message that was sent to the topic
     * @throws Exception Catches JSON parse exceptions
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = message.toString();
        JSONObject messageJSON = new JSONObject(payload);
        Message newMessage = new Message(
                messageJSON.getString(USERNAME),
                messageJSON.getString(PROFILE_IMAGE),
                messageJSON.getString(BODY)
        );
        mBinder.messageArrived(newMessage);
    }

    /**
     * Alerts when a message is successfully sent
     * @param token which is created on a successful delivery of a message
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        eLog.i(TAG, "Delivered");
    }

    /**
     * Allows you to subscribe to an event and all the messages sent to it
     */
    private void subscribe() {
        try {
            getClient().subscribe(String.valueOf(room), 2, null, this);
        } catch (MqttException e) {
            eLog.e(TAG, e.getMessage());
        }
    }

    /**
     * Debug the connection
     * @param source of broker
     * @param message from broker
     */
    @Override
    public void traceDebug(String source, String message) {
        eLog.i(TAG, source);
        eLog.i(TAG, message);
    }

    /**
     * Debug the connection
     *
     * @param source  of broker
     * @param message from broker
     */
    @Override
    public void traceError(String source, String message) {
        eLog.e(TAG, source);
        eLog.e(TAG, message);
    }

    /**
     * Debug the connection
     * @param source of broker
     * @param message from broker
     */
    @Override
    public void traceException(String source, String message, Exception e) {
        eLog.e(TAG, source);
        eLog.e(TAG, message);
    }

    /**
     * API for a successful connection to broker
     *
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
        eLog.e(TAG, "Error:".concat(exception.toString()));
    }

    /**
     * Send a message to a topic
     *
     * @param body Body of message in JSON format that contains all the messages details
     */
    public void send(final String body) {
        final JSONObject payload = new JSONObject();
        final User currentActiveUser = SessionManager.getCurrentUser();
        final CurrentActiveEvent currentActiveEvent = CurrentActiveEvent.getInstance();
        final String username = currentActiveUser.getUsername();
        final String profileImage = currentActiveUser.getProfileImage();
        try {
            payload.put(ID, currentActiveUser.getId());
            payload.put(BODY, body);
            payload.put(NAME, currentActiveEvent.getCurrentEvent().getEventName());
            payload.put(USERNAME, username);
            payload.put(PROFILE_IMAGE, profileImage);
        } catch (JSONException e) {
            eLog.e(TAG, e.getMessage());
        }

        try {
            mClient.publish(String.valueOf(room), payload.toString().getBytes(), QOS, false, null, this);
        } catch (MqttException e) {
            eLog.e(TAG, e.getMessage());
        }
        Messenger.saveMessage(body);
    }

    /**
     * Disconnects you from the MQTT Broker
     */
    public void disconnect() {
        try {
            this.mClient.disconnect();
            eLog.i(TAG, "disconnected");
        } catch (MqttException e) {
            eLog.e(TAG, e.getMessage());
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
