package com.jaysyko.wrestlechat.mqtt;

import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.utils.DBConstants;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by jarushaan on 2016-03-14
 */
public class MQTTConnection implements MqttCallback {

    private static final MQTTConnection instance = new MQTTConnection();
    private MqttClient client;

    private MQTTConnection() {
        connect();
    }

    public static MQTTConnection getInstance() {
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

    public MqttClient getClient() {
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
}
