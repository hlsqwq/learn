package com.hls.template;

import static com.hls.enumeration.meg.LOG;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hls.configuration.Mqttconfig;
import com.hls.handle.DebugHandle;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MqttTemplate implements MqttCallback {
    private Map<String, List<CallBack>>map;
    private MqttClient mqttClient;
    public MqttTemplate(Mqttconfig mqttconfig) throws MqttException {
        map=new HashMap<>();
        MqttClient mqttClient = new MqttClient(mqttconfig.getServerUrl(),mqttconfig.getClientId(),
                new MemoryPersistence());
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(mqttconfig.getUserName());
        mqttConnectOptions.setPassword(mqttconfig.getPasswd().toCharArray());
        mqttConnectOptions.setCleanSession(true);
        mqttClient.setCallback(this);
        mqttClient.connect(mqttConnectOptions);
        this.mqttClient=mqttClient;
        DebugHandle.getInstance().obtainMessage(LOG.ordinal(),"mqtt服务器连接成功").sendToTarget();
    }

    void subscript(String topic,CallBack callBack) throws MqttException {
        if (!map.containsKey(topic)) {
            mqttClient.subscribe(topic);
            map.put(topic,new ArrayList<>());
        }
        map.get(topic).add(callBack);
    }

    public void send(String topic,byte []payload) throws MqttException {
        send(topic,0,payload);
    }

    public void send(String topic,int qos,byte []payload) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload);
        mqttMessage.setQos(qos);
        mqttClient.publish(topic,mqttMessage);
    }




    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        List<CallBack> callBacks = map.get(topic);
        if(callBacks==null || callBacks.isEmpty()){
            return;
        }
        String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
        for (CallBack callBack : callBacks) {
            callBack.receive(topic,payload);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
