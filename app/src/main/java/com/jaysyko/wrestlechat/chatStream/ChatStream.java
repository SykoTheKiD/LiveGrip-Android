package com.jaysyko.wrestlechat.chatStream;

import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
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
public class ChatStream implements MqttCallback {

    private static final ChatStream instance = new ChatStream();
    private MqttClient client;

    private ChatStream() {
        connect();
    }

    public static ChatStream getInstance() {
        return instance;
    }

    private MqttClient connect() {
        try {
            client = new MqttClient(DBConstants.MQTT_BROKER_URL, CurrentActiveUser.getInstance().getUserID());
            client.connect();
            client.setCallback(this);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public void send(Message message) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBody().getBytes());
        try {
            client.publish(CurrentActiveEvent.getInstance().getCurrentEvent().getEventID(), mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String room) {
        try {
            client.subscribe(room);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
