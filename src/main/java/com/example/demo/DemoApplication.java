package com.example.demo;

import com.example.demo.user.client.EmqClient;
import com.example.demo.user.enums.QosEnum;
import com.example.demo.user.properties.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @Autowired
    private EmqClient emqClient;

    @Autowired
    private MqttProperties mqttProperties;


    @PostConstruct
    public void init(){
        //连接服务端
        emqClient.connect(mqttProperties.getUsername(),mqttProperties.getPassword());
        //订阅一个主题
        emqClient.subscribe("testtopic/#", QosEnum.Qos2);
        /*new Thread(()->{
            while (true){
                emqClient.publish("testtopic/1","/log/1/售货机/1号/3/状态正常/1/",QosEnum.Qos2,false);
                emqClient.publish("testtopic/2","/log/2/监视器/1号摄像头/3/状态正常/2/",QosEnum.Qos2,false);
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }
}
