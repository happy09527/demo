package com.example.demo.user.client;

import com.example.demo.user.mapper.UserMapper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageCallback implements MqttCallback {

    @Autowired
    private UserMapper userMapper;

    //丢失对服务端连接后触发的回调函数
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("丢失服务端连接\n");
    }

    //应用收到消息后触发
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msg = new String(message.getPayload());
        msg = msg.substring(1,msg.length()-1);
        System.out.println("订阅者订阅到了消息"+topic+","+message.getId()+',' + msg +"\n");
        String[] a = msg.split("/");
        String type = a[0];
        String cid = a[a.length-1];
        userMapper.logInsert(type,msg,cid);
    }
    //消息发布完成触发
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        int messageId = token.getMessageId();
        String[] topics  = token.getTopics();
        System.out.println("消息发布完成"+messageId+","+topics+"\n");
    }
}
