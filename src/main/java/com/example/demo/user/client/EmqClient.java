package com.example.demo.user.client;

import com.example.demo.user.enums.QosEnum;
import com.example.demo.user.properties.MqttProperties;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class EmqClient {
    private IMqttClient mqttClient;

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private MqttCallback mqttCallback;

    @PostConstruct
    public  void init(){   //初始化创建客户端
        MqttClientPersistence mqttClientPersistence = new MemoryPersistence();
        try {
            mqttClient = new MqttClient(mqttProperties.getBrokerUrl(),mqttProperties.getClientId(),mqttClientPersistence);
        } catch (MqttException e) {
            System.out.println("初始化客户端mqttClient对象失败"+e.getMessage()+"\n");
        }
    }

    public  void connect(String username, String password){    //连接emqx服务器需要账号和密码验证
        MqttConnectOptions options =  new MqttConnectOptions();    
        options.setAutomaticReconnect(true);  //自动重连
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setCleanSession(true);
        mqttClient.setCallback(mqttCallback);
        try {
            mqttClient.connect(options);
        } catch (MqttException e) {
            System.out.println("客户端连接服务端失败"+e.getMessage()+"\n");
        }
    }

    public void publish(String topic, String msg, QosEnum qos, boolean retain){   //发布主题消息
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(msg.getBytes());
        mqttMessage.setQos(qos.value());
        mqttMessage.setRetained(retain);
        try {
            mqttClient.publish(topic,mqttMessage);
        } catch (MqttException e) {
            System.out.println("发布消息失败"+e.getMessage()+','+topic+ "\n");
        }
    }

    public void subscribe(String topicFilter,QosEnum qos){   //订阅主题消息
        try {
            mqttClient.subscribe(topicFilter,qos.value());
        } catch (MqttException e) {
            System.out.println("订阅消息失败"+e.getMessage()+','+topicFilter+ "\n");
        }
    }

    public void unSubscribe(String topicFilter){   //取消订阅
        try {
            mqttClient.unsubscribe(topicFilter);
        } catch (MqttException e) {
            System.out.println("取消订阅失败"+e.getMessage()+','+topicFilter+ "\n");
        }
    }

    @PreDestroy
    public void  disConnect(){     //断开连接
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            System.out.println("断开连接失败"+e.getMessage()+ "\n");
        }
    }

    public void reConnect(){   //重连
        try {
            mqttClient.reconnect();
        } catch (MqttException e) {
            System.out.println("重连失败"+e.getMessage()+ "\n");
        }
    }
}
